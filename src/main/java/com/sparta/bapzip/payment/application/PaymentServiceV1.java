package com.sparta.bapzip.payment.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.order.domain.repository.OrderRepository;
import com.sparta.bapzip.payment.domain.entity.PaymentEntity;
import com.sparta.bapzip.payment.domain.entity.PaymentStatusEnum;
import com.sparta.bapzip.payment.infrastructure.config.payment.TossPaymentsConfig;
import com.sparta.bapzip.payment.domain.repository.PaymentRepository;
import com.sparta.bapzip.payment.presentation.dto.request.PaymentRequest;
import com.sparta.bapzip.payment.presentation.dto.response.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceV1 {
    private final TossPaymentsConfig tossConfig;
    private final PaymentRepository paymentRepository;
//    private final OrderRepository orderRepository;

    public PaymentEntity getPaymentByOrderId(UUID orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new GlobalException(ErrorCode.PAYMENT_NOT_FOUND));
    }
    /**
     * PaymentEntity 생성
     *
     * @param paymentRequest 주문자의 카드 정보가 담긴 PaymentRequest
     * @return toss 결제 승인 응답이 담긴 PaymentResponseDto
     * @throws GlobalException ORDER_NOT_FOUND 에러 발생 시
     */
    @Transactional
    public PaymentResponseDto createPaymentWithCard(UUID orderId, PaymentRequest paymentRequest) {
        // orderId, 주문 가게, 주문 메뉴, 총 금액 필요
        PaymentEntity payment = null;
//       // OrderEntity 조회 구현 이후 주석 해제
//        OrderEntity order =orderRepository.findById(orderId).orElse(null);
//        if(order == null){
//              throw new GlobalException(ErrorCode.ORDER_NOT_FOUND);
//        } else {
//            payment = PaymentEntity.builder()
//                    .order(order)
//                    .totalAmount(order.getTotalAmount())
//                    .status(PaymentStatusEnum.IN_PROGRESS)
//                    .build();
//            paymentRepository.save(payment);
//            StringBuilder sb = new StringBuilder();
//            sb.append(order.getOrderMenuList().get(0).getMenu().getShop().getName()+"의 "+order.getOrderMenuList().get(0).getMenuName());
//
//            if( order.getOrderMenuList().size()>2){
//                sb.append(" 외 "+(order.getOrderMenuList().size()-1)+"건");
//            }
//            paymentRequest.setOrderId(order.getId().toString());
//            paymentRequest.setOrderName(sb.toString());
//            paymentRequest.setAmount(order.getTotalAmount());
//        }
        // 임시 결제 정보
//        String orderIdStr = UUID.randomUUID().toString();
        String orderName = "테스트 결제";
        int amount = 1000000;
        UUID tempOrderId = UUID.fromString("fb64f420-6c70-49fa-806f-a5bc775b89db");
        payment = PaymentEntity.builder()
                    .order(
                            OrderEntity.builder()
                                    .id(tempOrderId)
                                    .totalAmount(amount)
                                    .build()
                    )
                    .totalAmount(amount)
                    .status(PaymentStatusEnum.IN_PROGRESS)
                    .build();
        paymentRepository.save(payment);
        // PaymentRequest 생성
        paymentRequest.setOrderId(orderId.toString());
        paymentRequest.setOrderName(orderName);
        paymentRequest.setAmount(amount);

        PaymentResponseDto paymentResponse = createPayment(paymentRequest);

        if (paymentResponse != null && paymentResponse.getPaymentKey() != null) {
            payment.updatePaymentConfirmResult(
                    paymentResponse.getPaymentKey(),
                    PaymentStatusEnum.SUCCESS,
                    paymentResponse.getApprovedAt()
            );
        } else {
            payment.updatePaymentConfirmResult(null, PaymentStatusEnum.FAILED, null);
        }
        paymentRepository.save(payment);
        return paymentResponse;
    }
    /**
     * 결제 승인 요청 메소드
     *
     * @param paymentRequest 카드 정보와 주문정보 가 담긴 PaymentRequest
     * @return toss 결제 승인 응답이 담긴 PaymentResponseDto
     */
    public PaymentResponseDto createPayment(PaymentRequest paymentRequest) {
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        WebClient client = WebClient.builder()
                .baseUrl("https://api.tosspayments.com/v1/payments/key-in")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " +
                        Base64.getEncoder().encodeToString((tossConfig.getSecretKey() + ":").getBytes()))
                .build();

        JsonNode response = client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paymentRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        resp -> resp.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(JsonNode.class)
                .block();

        if (response != null && response.has("paymentKey")) {
            String paymentKey = response.get("paymentKey").asText();
            log.info("Toss paymentKey generated: {}", paymentKey);
            if (response.get("approvedAt").asText() != null) {
                paymentResponseDto.setApprovedAt(OffsetDateTime.parse(response.get("approvedAt").asText()).toLocalDateTime());
            }
            paymentResponseDto.setPaymentKey(paymentKey);
            paymentResponseDto.setTotalPrice(paymentRequest.getAmount());
            paymentResponseDto.setStatus("SUCCESS");

            if (response.get("status").asText().equals("DONE")) {
                paymentResponseDto.setStatus("SUCCESS");
            } else if (response.get("status").asText().equals("DONE")) {
                paymentResponseDto.setStatus("FAIL");
            } else if (response.get("status").asText().equals("ABORTED")) {
                paymentResponseDto.setStatus("FAIL");
            }
            return paymentResponseDto;
        }

        throw new IllegalStateException("paymentKey not found in Toss response: " + response);
    }
    /**
     * 결제 취소 요청 및 상태 업데이트
     *
     * @param orderId 와 취소 사유
     * @return toss 결제 취소 승인 응답이 담긴 PaymentResponseDto
     */
    @Transactional
    public PaymentResponseDto cancelPayment(UUID orderId, String cancelReason) {
        PaymentEntity paymentEntity = getPaymentByOrderId(orderId);
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setCancelReason(cancelReason);
        if (paymentEntity.getStatus() != PaymentStatusEnum.SUCCESS) {
            throw new GlobalException(ErrorCode.PAYMENT_CANCELLATION_NOT_ALLOWED);
        }
        WebClient client = WebClient.builder()
                .baseUrl("https://api.tosspayments.com//v1/payments/" + paymentEntity.getPaymentKey() + "/cancel")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " +
                        Base64.getEncoder().encodeToString((tossConfig.getSecretKey() + ":").getBytes()))
                .build();

        JsonNode response = client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paymentRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        resp -> resp.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(JsonNode.class)
                .block();
        PaymentResponseDto paymentResponseDto= new PaymentResponseDto();
        if (response != null && response.get("status").asText().equals("CANCELED")) {
            String canceledAt = response.path("cancels").get(0).path("canceledAt").asText();
            PaymentEntity payment = getPaymentByOrderId(orderId);
            payment.updatePaymentCancelResult(PaymentStatusEnum.CANCELED, cancelReason, OffsetDateTime.parse(canceledAt).toLocalDateTime());
            paymentRepository.save(payment);

            paymentResponseDto.setCanceledAt(OffsetDateTime.parse(canceledAt).toLocalDateTime());
            paymentResponseDto.setStatus(PaymentStatusEnum.CANCELED.toString());
            paymentResponseDto.setOrderId(paymentEntity.getOrder().getId().toString());
            paymentResponseDto.setTotalPrice(paymentEntity.getTotalAmount());
        }
        return paymentResponseDto;
    }
}
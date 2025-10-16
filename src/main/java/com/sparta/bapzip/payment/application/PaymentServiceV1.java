package com.sparta.bapzip.payment.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.order.domain.repository.OrderRepository;
import com.sparta.bapzip.payment.domain.entity.PaymentEntity;
import com.sparta.bapzip.payment.domain.entity.PaymentStatusEnum;
import com.sparta.bapzip.payment.domain.exception.PaymentException;
import com.sparta.bapzip.payment.infrastructure.config.payment.TossPaymentsConfig;
import com.sparta.bapzip.payment.domain.repository.PaymentRepository;
import com.sparta.bapzip.payment.presentation.dto.request.*;
import com.sparta.bapzip.payment.presentation.dto.response.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceV1 {
    private final TossPaymentsConfig tossConfig;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentEntity getPaymentByOrderId(UUID orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentException(ErrorCode.PAYMENT_NOT_FOUND));
    }
    /**
     * PaymentEntity 생성
     *
     * @param requestDto 주문자의 카드 정보가 담긴 PaymentCreateRequest
     * @return toss 결제 승인 응답이 담긴 PaymentResponseDto
     * @throws PaymentException ORDER_NOT_FOUND
     */
    private void testCardInfo(PaymentCreateRequest requestDto) {
        requestDto.setCardNumber("4111111111111111");
        requestDto.setCardExpirationYear("28");
        requestDto.setCardExpirationMonth("12");
        requestDto.setCardPassword("01");
        requestDto.setCustomerIdentityNumber("860824");
    }
    @Transactional
    public PaymentResponseDto createPayment(PaymentCreateRequest paymentCreateRequest) {
        OrderEntity order = orderRepository.findById(paymentCreateRequest.getOrderId()).orElseThrow(() -> new PaymentException(ErrorCode.ORDER_NOT_FOUND));
        PaymentEntity payment = PaymentEntity.builder()
                .order(order)
                .totalAmount(order.getTotalPrice())
                .status(PaymentStatusEnum.IN_PROGRESS)
                .build();
        payment.markCreated(order.getUser().getId());
        paymentRepository.save(payment);
        StringBuilder sb = new StringBuilder();
        if(order.getOrderMenuList() == null || order.getOrderMenuList().isEmpty()){
            throw new PaymentException(ErrorCode.MENUS_NOT_FOUND_IN_ORDER);
        } else {
            sb.append(order.getOrderMenuList().get(0).getMenu().getShop().getName()+"의 "+order.getOrderMenuList().get(0).getMenu().getName());

            if( order.getOrderMenuList().size()>=2){
                sb.append(" 외 "+(order.getOrderMenuList().size()-1)+"건");
            }
        }
        if(haveCardInfo(paymentCreateRequest)){
            testCardInfo(paymentCreateRequest);
        }
        paymentCreateRequest.setOrderName(sb.toString());
        paymentCreateRequest.setAmount(order.getTotalPrice());
        PaymentResponseDto response = createTossCardPayment(paymentCreateRequest);
        if (response != null && response.getPaymentKey() != null) {
            payment.updatePaymentConfirmResult(
                    response.getPaymentKey(),
                    PaymentStatusEnum.SUCCESS,
                    response.getApprovedAt()
            );
        } else {
            payment.updatePaymentConfirmResult(null, PaymentStatusEnum.FAILED, null);
        }
        payment.markUpdated(order.getUser().getId());
        paymentRepository.save(payment);
        return response;
    }


    private boolean haveCardInfo(PaymentCreateRequest requestDto) {
        return requestDto.getCardNumber() != null && !requestDto.getCardNumber().isBlank()
                && requestDto.getCardExpirationYear() != null && !requestDto.getCardExpirationYear().isBlank()
                && requestDto.getCardExpirationMonth() != null && !requestDto.getCardExpirationMonth().isBlank()
                && requestDto.getCardPassword() != null && !requestDto.getCardPassword().isBlank()
                && requestDto.getCustomerIdentityNumber() != null && !requestDto.getCustomerIdentityNumber().isBlank();
    }

    /**
     * Toss 결제 승인 요청
     */
    public PaymentResponseDto createTossCardPayment(PaymentCreateRequest paymentCreateRequest) {
        WebClient client = buildTossClient();

        JsonNode response = client.post()
                .uri("/key-in")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paymentCreateRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        resp -> resp.bodyToMono(String.class)
                                .map(body -> {
                                    log.error("Toss API error: {}", body);
                                    return new PaymentException(ErrorCode.PAYMENT_REQUEST_FAILED);
                                }))
                .bodyToMono(JsonNode.class)
                .block();
        if (response == null || !response.hasNonNull("paymentKey")) {
            throw new PaymentException(ErrorCode.PAYMENT_KEY_MISSING);
        }
        return mapPaymentResponse(response, paymentCreateRequest);
    }
    /**
     * 결제 취소 요청 및 상태 업데이트
     * @param userId 주문을 취소처리하는 유저 식별자
     * @param orderId 주문 식별자
     * @param cancelReason 취소 사유
     * @return 결제 취소 응답 DTO
     */
    @Transactional
    public PaymentResponseDto cancelPayment(Long userId, UUID orderId, String cancelReason) {
        PaymentEntity payment = getPaymentByOrderId(orderId);

        if (payment.getStatus() != PaymentStatusEnum.SUCCESS) {
            throw new PaymentException(ErrorCode.INVALID_PAYMENT_STATUS);
        }

        // Toss 취소 요청 DTO
        PaymentCancelRequest paymentCancelRequest = new PaymentCancelRequest(
                payment.getOrder().getId(),
                payment.getPaymentKey(),
                cancelReason
        );

        // WebClient 호출
        WebClient client = buildTossClient();
        JsonNode response = client.post()
                .uri("/{paymentKey}/cancel", payment.getPaymentKey())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paymentCancelRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        resp -> resp.bodyToMono(String.class)
                                .map(body -> {
                                    log.error("Toss cancel API error: {}", body);
                                    return new PaymentException(ErrorCode.PAYMENT_CANCEL_FAILED);
                                }))
                .bodyToMono(JsonNode.class)
                .block();

        if (response == null || !response.hasNonNull("status")) {
            throw new PaymentException(ErrorCode.PAYMENT_CANCEL_FAILED);
        }

        // Toss 응답 → DTO 변환
        PaymentResponseDto dto = mapPaymentResponse(response, paymentCancelRequest);

        if (PaymentStatusEnum.CANCELED.name().equals(dto.getStatus())) {
            LocalDateTime canceledAt = dto.getCanceledAt();
            payment.updatePaymentCancelResult(PaymentStatusEnum.CANCELED, cancelReason, canceledAt);
            payment.markUpdated(userId);
            log.info("Payment={}",payment);
            paymentRepository.save(payment);
            dto.setOrderId(payment.getOrder().getId().toString());
            dto.setTotalPrice(payment.getTotalAmount());

            log.info("Payment canceled successfully. orderId={}, paymentKey={}", orderId, payment.getPaymentKey());
        } else {
            payment.markUpdated(userId);
            paymentRepository.save(payment);
            throw new PaymentException(ErrorCode.PAYMENT_CANCEL_FAILED);
        }

        return dto;
    }

    /**
     * WebClient 공통 설정
     */
    private WebClient buildTossClient() {
        return WebClient.builder()
                .baseUrl("https://api.tosspayments.com/v1/payments")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " +
                        Base64.getEncoder().encodeToString((tossConfig.getSecretKey() + ":").getBytes()))
                .build();
    }

    /**
     * Toss 응답 → DTO 변환
     */
    private PaymentResponseDto mapPaymentResponse(JsonNode response, Object request) {
        PaymentResponseDto dto = new PaymentResponseDto();

        // 1. 공통 필드 매핑
        dto.setPaymentKey(response.path("paymentKey").asText(null));

        String approvedAtStr = response.path("approvedAt").asText(null);
        dto.setApprovedAt(parseDateTime(approvedAtStr));

        // 총 결제 금액 (response > request 우선)
        int totalAmount = response.path("totalAmount").asInt(0);
        if (totalAmount > 0) {
            dto.setTotalPrice(totalAmount);
        } else if (request instanceof PaymentCreateRequest createReq && createReq.getAmount() > 0) {
            dto.setTotalPrice(createReq.getAmount());
        }

        // 2. 취소 관련 필드
        JsonNode cancelsNode = response.path("cancels");
        if (cancelsNode.isArray() && cancelsNode.size() > 0) {
            JsonNode cancelInfo = cancelsNode.get(0);
            String canceledAtStr = cancelInfo.path("canceledAt").asText(null);
            dto.setCanceledAt(parseDateTime(canceledAtStr));
        }

        // 3. 상태 매핑
        PaymentStatusEnum mappedStatus = switch (response.path("status").asText("")) {
            case "DONE" -> PaymentStatusEnum.SUCCESS;
            case "CANCELED" -> PaymentStatusEnum.CANCELED;
            case "ABORTED", "FAILED" -> PaymentStatusEnum.FAILED;
            default -> PaymentStatusEnum.FAILED;
        };
        dto.setStatus(mappedStatus.name());

        // 4. 요청 객체 기반 orderId 매핑
        if (request instanceof PaymentCancelRequest cancelReq && cancelReq.getOrderId() != null) {
            dto.setOrderId(cancelReq.getOrderId().toString());
        } else if (request instanceof PaymentCreateRequest createReq && createReq.getOrderId() != null) {
            dto.setOrderId(String.valueOf(createReq.getOrderId()));
        }

        log.info("[Toss] paymentKey={}, status={}",dto.getPaymentKey(), dto.getStatus());
        log.info("PaymentResponseDto={}", dto);
        return dto;
    }
    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isBlank()) {
            return null;
        }
        try {
            return OffsetDateTime.parse(dateTimeStr).toLocalDateTime();
        } catch (Exception e) {
            log.error("Failed to parse date: {}", dateTimeStr, e);
            return null;
        }
    }
}
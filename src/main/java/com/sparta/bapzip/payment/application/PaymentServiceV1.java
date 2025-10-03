package com.sparta.bapzip.payment.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.sparta.bapzip.payment.infrastructure.config.payment.TossPaymentsConfig;
import com.sparta.bapzip.payment.domain.repository.PaymentRepository;
import com.sparta.bapzip.payment.presentation.dto.request.PaymentRequest;
import com.sparta.bapzip.payment.presentation.dto.response.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceV1 {

    private final WebClient tossWebClient;
    private final TossPaymentsConfig tossConfig;
    private final PaymentRepository paymentRepository;

    public PaymentResponseDto createPayment(PaymentRequest paymentRequest) { //paymentKey 발급
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setRequestedAt(LocalDateTime.now());
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
            paymentResponseDto.setRequestedAt(OffsetDateTime.parse(response.get("requestedAt").asText()).toLocalDateTime());
            if(response.get("approvedAt").asText()!=null){
                paymentResponseDto.setApprovedAt(OffsetDateTime.parse(response.get("approvedAt").asText()).toLocalDateTime());
            }
            paymentResponseDto.setPaymentKey(paymentKey);
            paymentResponseDto.setOrderId(paymentRequest.getOrderId());
            paymentResponseDto.setTotalPrice(paymentRequest.getAmount());
            paymentResponseDto.setStatus("SUCCESS");
            if(response.get("status").asText().equals("DONE")){
                paymentResponseDto.setStatus("SUCCESS");
            } else if(response.get("status").asText().equals("DONE")){
                paymentResponseDto.setStatus("FAIL");
            } else if(response.get("status").asText().equals("ABORTED")){
                paymentResponseDto.setStatus("FAIL");
            }
            return paymentResponseDto;
        }

        throw new IllegalStateException("paymentKey not found in Toss response: " + response);
    }
}
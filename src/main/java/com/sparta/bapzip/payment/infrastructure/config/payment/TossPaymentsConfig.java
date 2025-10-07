package com.sparta.bapzip.payment.infrastructure.config.payment;
import io.netty.channel.ChannelOption;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Base64;

@Configuration
@Getter
@Setter
public class TossPaymentsConfig {
    @Value("${payment.toss.secret.key}")
    private String secretKey;

    private String apiUrl = "https://api.tosspayments.com/v1";
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
    @Bean
    public WebClient tossWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000) // 연결 타임아웃 2초
                .responseTimeout(Duration.ofSeconds(30));           // 응답 타임아웃 30초
        return WebClient.builder()
                .baseUrl(apiUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodeSecretKey(secretKey))
                .build();
    }

    private String encodeSecretKey(String secretKey) {
        return Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
    }

}

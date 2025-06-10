package com.pmt.statusInterpreter.aiSvc;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PmtDataHelperSvc {

    private final WebClient webClient = WebClient.create("http://localhost:8080");

    public String getPaymentById(String id) {
        return webClient.get()
                .uri("/api/payments/{id}", id)
                .retrieve()
                .bodyToMono(String.class).block();
    }
}

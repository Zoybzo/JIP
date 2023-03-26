package com.example.springdemo.service.Impl;

import com.example.springdemo.model.TestModel;
import com.example.springdemo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    WebClient webclient;

    @Override
    public TestModel test() {
        Mono<Void> result = webclient.post().uri("/hello")
                .retrieve()
                .bodyToMono(Void.class);
        return null;
    }
}

package com.example.springdemo.service.Impl;

import com.example.springdemo.logic.Impl.AsyncLogicImpl;
import com.example.springdemo.model.TestModel;
import com.example.springdemo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    WebClient webclient;

    @Override
    public Mono<String> test() {
        Mono<String> result = webclient.post().uri("/hello")
                .retrieve()
                .bodyToMono(String.class);
        return result;
    }
}

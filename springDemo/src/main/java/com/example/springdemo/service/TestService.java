package com.example.springdemo.service;

import com.example.springdemo.model.TestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public interface TestService {

    public Mono<String> test();

}

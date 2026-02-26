package com.yu.provider;

import org.apache.dubbo.config.annotation.DubboService;

import java.util.concurrent.CompletableFuture;

@DubboService
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }

    @Override
    public String sayHello2(String name) {
        return "";
    }

    @Override
    public CompletableFuture<String> sayHelloAsync(String name) {
        return DemoService.super.sayHelloAsync(name);
    }
}
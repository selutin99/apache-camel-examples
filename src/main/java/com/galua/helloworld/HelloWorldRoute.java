package com.galua.helloworld;

import org.apache.camel.builder.RouteBuilder;

public class HelloWorldRoute extends RouteBuilder {

    @Override
    public void configure() {
        System.out.println("Hello world");
    }
}

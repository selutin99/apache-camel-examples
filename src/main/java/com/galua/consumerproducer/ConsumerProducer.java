package com.galua.consumerproducer;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ConsumerProducer {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
            from("direct:start")
            .process(new Processor() {
                public void process(Exchange exchange) throws Exception {
                    // case #4
                    System.out.println("Processor in route");

                    // case #5
                    String message = exchange.getIn().getBody(String.class);
                    message = message + " | by galua";
                    exchange.getOut().setBody(message);
                }
            })
            .to("seda:end");
            }
        });

        context.start();

        ProducerTemplate producerTemplate = context.createProducerTemplate();
        producerTemplate.sendBody("direct:start", "Test message");

        ConsumerTemplate consumerTemplate = context.createConsumerTemplate();
        String message = consumerTemplate.receiveBody("seda:end", String.class);

        System.out.println(message);

        context.stop();
    }
}

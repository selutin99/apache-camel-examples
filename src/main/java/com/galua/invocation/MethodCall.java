package com.galua.invocation;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;

import javax.jms.ConnectionFactory;

public class MethodCall {

    public static void main(String[] args) throws Exception {
        // case #10
        MyService myService = new MyService();

        SimpleRegistry registry = new SimpleRegistry();
        registry.put("myService", myService);

        CamelContext context = new DefaultCamelContext(registry);

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                //.to("class:com.galua.invocation.MyService?method=doSomething");
                .to("bean:myService?method=doSomething");
            }
        });

        context.start();

        ProducerTemplate producerTemplate = context.createProducerTemplate();
        producerTemplate.sendBody("direct:start", "Hello");
    }
}

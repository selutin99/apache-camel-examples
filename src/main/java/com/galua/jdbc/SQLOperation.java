package com.galua.jdbc;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;

public class SQLOperation {

    public static void main(String[] args) throws Exception {

        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/camel_tutorial?useSSL=false&allowPublicKeyRetrieval=true");
        dataSource.setUser("root");
        dataSource.setPassword("root");

        SimpleRegistry registry = new SimpleRegistry();
        registry.put("myDataSource", dataSource);

        CamelContext context = new DefaultCamelContext(registry);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                .to("jdbc:myDataSource")
                .bean(new ResultHandler(), "printResult");
            }
        });

        context.start();

        ProducerTemplate producerTemplate = context.createProducerTemplate();
        producerTemplate.sendBody(
                "direct:start",
                "select * from customer"
        );
    }
}

package com.dt.student.register.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdditionalTomcatConnectorConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> additionalConnectorCustomizer(
            @Value("${server.port}") int serverPort,
            @Value("${app.additional-port:0}") int additionalPort
    ) {
        return factory -> {
            if (additionalPort <= 0 || additionalPort == serverPort) {
                return;
            }

            Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
            connector.setScheme("http");
            connector.setPort(additionalPort);
            factory.addAdditionalTomcatConnectors(connector);
        };
    }
}

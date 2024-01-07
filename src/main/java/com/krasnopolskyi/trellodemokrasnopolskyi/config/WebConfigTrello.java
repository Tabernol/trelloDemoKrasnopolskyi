package com.krasnopolskyi.trellodemokrasnopolskyi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfigTrello {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

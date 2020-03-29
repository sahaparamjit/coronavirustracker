package com.herokuapp.coronatrackingapp.configuration;

import com.herokuapp.coronatrackingapp.utils.CustomResponseErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateWithErrorHandling {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(1000))
                .errorHandler(new CustomResponseErrorHandler())
                .build();
    }
}

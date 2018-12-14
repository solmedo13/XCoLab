package org.xcolab.util.autoconfigure;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public abstract class AbstractFeignConfig {

    @Bean
    public ErrorDecoder getErrorDecoder() {
        return new XColabErrorDecoder();
    }
}
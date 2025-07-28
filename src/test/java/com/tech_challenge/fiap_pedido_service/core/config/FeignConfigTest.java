package com.tech_challenge.fiap_pedido_service.core.config;

import feign.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FeignConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void feignLoggerLevelBeanExistsAndIsFull() {
        Logger.Level feignLoggerLevel = applicationContext.getBean(Logger.Level.class);
        assertNotNull(feignLoggerLevel);
        assertEquals(Logger.Level.FULL, feignLoggerLevel);
    }
}

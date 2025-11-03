package com.tqs.zeromonos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ZeroMonosApplicationTests {

    @Autowired
    private ZeroMonosApplication app;

    @Test
    void contextLoads() {
        // Verifica se a aplicação principal foi injetada com sucesso
        assert app != null;
    }
}

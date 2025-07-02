package com.pmt.statusInterpreter.aiSvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
public class ThreadPoolConfig {
    @Bean(name = "kafkaListenerExecutor")
    public Executor kafkaListenerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);
        executor.setMaxPoolSize(100);
        executor.setThreadNamePrefix("KafkaListener-");
        executor.initialize();
        return executor;
    }
}


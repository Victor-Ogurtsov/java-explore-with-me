package ru.practicum;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class Config {

    private final EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactoryBean() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public RestTemplate restTemplateBean() {
        return new RestTemplate();
    }
}
package kr.bb.product.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "kr.bb.product.repository.jpa")
public class JpaConfiguration {}

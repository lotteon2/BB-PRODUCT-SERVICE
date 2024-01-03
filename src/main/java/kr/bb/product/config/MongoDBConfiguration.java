package kr.bb.product.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableReactiveMongoAuditing
@EnableMongoRepositories(basePackages = "kr.bb.product.domain.**.mongo")
@RequiredArgsConstructor
public class MongoDBConfiguration {}

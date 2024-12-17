package com.ureca.picky_be.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class ElasticSearchConfig extends ElasticsearchConfiguration {
    @Value("${spring.data.elasticsearch.host}")
    private String host;

    @Value("${spring.data.elasticsearch.api-key}")
    private String apiKey;

    @Value("${spring.data.elasticsearch.username}")
    private String username;

    @Value("${spring.data.elasticsearch.password}")
    private String password;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(host)
                .usingSsl()
                .withBasicAuth(username, password)
                .build();
    }
}

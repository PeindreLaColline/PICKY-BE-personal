package com.ureca.picky_be.config;

import lombok.RequiredArgsConstructor;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.client.RestClient;

import javax.net.ssl.SSLContext;
import java.io.File;

@Configuration
@EnableElasticsearchRepositories
@RequiredArgsConstructor
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.data.elasticsearch.host}")
    private String host;

    @Value("${spring.data.elasticsearch.username}")
    private String username;

    @Value("${spring.data.elasticsearch.password}")
    private String password;

    @Value("${spring.data.elasticsearch.ssl.trust-store}")
    private String trustStorePath;

    @Value("${spring.data.elasticsearch.ssl.trust-store-password}")
    private String trustStorePassword;

    @Override
    public ClientConfiguration clientConfiguration() {
        try {
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(new File(trustStorePath), trustStorePassword.toCharArray())
                    .build();

            return ClientConfiguration.builder()
                    .connectedTo(host)
                    .usingSsl(sslContext)
                    .withBasicAuth(username, password)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to configure SSL for Elasticsearch", e);
        }
    }

}

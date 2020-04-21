package com.gc.elasticsearch.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ClientConfig {

//    @Bean
//    public TransportClient transportClient() throws UnknownHostException {
//        TransportClient client = new PreBuiltTransportClient(Settings.builder()
//                .put("cluster.name", "elasticsearch")
//                .build())
//                .addTransportAddress(new InetSocketTransportAddress(
//                        InetAddress.getByName("docker"), 9301));
//        return client;
//    }
}

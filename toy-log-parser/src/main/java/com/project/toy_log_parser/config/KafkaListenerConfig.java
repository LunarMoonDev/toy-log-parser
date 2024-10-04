package com.project.toy_log_parser.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.project.toy_log_parser.dto.KafkaDTO;

import java.util.Map;
import java.util.HashMap;

@Configuration
public class KafkaListenerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;

    @Value("${spring.kafka.security.protocol}")
    private String kafkaSecurityProtocol;

    @Value("${spring.kafka.sasl.jaas}")
    private String kafkaSaslJaas;

    @Value("${spring.kafka.sasl.mechanism}")
    private String kafkaSaslMechnism;

    @Value("${spring.kafka.client.dns.lookup}")
    private String kafkaClientDnsLookup;

    @Value("${spring.kafka.api.key}")
    private String kafkaApiKey;

    @Value("${spring.kafka.api.secret}")
    private String kafkaApiSecret;

    @Value("${spring.kafka.consumer.configuration.group.id}")
    private String kafkaConsumerConfigurationGroupId;

    @Value("${spring.kafka.consumer.configuration.enable-auto-commit}")
    private String kafkaConsumerConfigurationEnableAutoCommit;

    @Value("${spring.kafka.consumer.configuration.auto-commit-interval-ms}")
    private String kafkaConsumerConfigurationAutoCommitIntervalMs;

    @Value("${spring.kafka.consumer.configuration.isolation-level}")
    private String kafkaConsumerConfigurationIsolationLevel;

    @Value("${spring.kafka.consumer.configuration.listener-container-concurrency}")
    private Integer kafkaConsumerConfigurationListenerContainerConcurrency;

    @Value("${spring.kafka.consumer.fetch.min.bytes}")
    private Integer kafkaConsumerFetchMinBytes;

    @Value("${spring.kafka.consumer.fetch.max.bytes}")
    private Integer kafkaConsumerFetchMaxBytes;

    @Value("${spring.kafka.consumer.max.partition.fetch.bytes}")
    private Integer kafkaConsumerMaxPartitionFetchBytes;

    @Value("${spring.kafka.consumer.fetch.max.wait.ms}")
    private Integer kafkaConsumerFetchMaxWaitMs;

    @Value("${spring.kafka.consumer.max.poll.records}")
    private Integer kafkaConsumerMaxPollRecords;

    @Value("${kafka.report.client.id}")
    private String reportClientId;

    public Map<String, Object> consumerConfiguration() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaBootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, this.kafkaConsumerConfigurationGroupId);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, this.kafkaConsumerConfigurationIsolationLevel);
        properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, this.kafkaConsumerFetchMinBytes);
        properties.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, this.kafkaConsumerFetchMaxBytes);
        properties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, this.kafkaConsumerMaxPartitionFetchBytes);
        properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, this.kafkaConsumerFetchMaxWaitMs);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.kafkaConsumerMaxPollRecords);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, this.kafkaConsumerConfigurationEnableAutoCommit);
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,
                this.kafkaConsumerConfigurationAutoCommitIntervalMs);

        if (!this.kafkaApiKey.isEmpty() && !this.kafkaApiSecret.isEmpty()) {
            properties.put(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, this.kafkaSecurityProtocol);

            properties.put("sasl.jaas.config",
                    "org.apache.kafka.common.security.plain.PlainLoginModule " + "required username='"
                            + this.kafkaApiKey + "' password='" + this.kafkaApiSecret + "';");
            properties.put("sasl.mechanism", this.kafkaSaslMechnism);
            properties.put(AdminClientConfig.CLIENT_DNS_LOOKUP_CONFIG, this.kafkaClientDnsLookup);
        }
        return properties;
    }

    @Bean(name = "consumerFactory")
    public ConsumerFactory<String, KafkaDTO> consumerFactory() {
        Map<String, Object> properties = this.consumerConfiguration();
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, this.reportClientId);

        ErrorHandlingDeserializer<KafkaDTO> valueErrorHandlingDeserializer
                = new ErrorHandlingDeserializer<>(new JsonDeserializer<>(KafkaDTO.class));
        ErrorHandlingDeserializer<String> keyErrorHandlingDeserializer
                = new ErrorHandlingDeserializer<>(new StringDeserializer());


        return new DefaultKafkaConsumerFactory<>(properties, keyErrorHandlingDeserializer, valueErrorHandlingDeserializer);
    }

    @Bean(name = "kafkaListenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, KafkaDTO>> kafkaListenerContainerFactory(
            @Qualifier("consumerFactory") ConsumerFactory<String, KafkaDTO> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, KafkaDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(this.kafkaConsumerConfigurationListenerContainerConcurrency);
        factory.getContainerProperties().setObservationEnabled(true);
        factory.getContainerProperties().setMicrometerEnabled(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        return factory;
    }
}

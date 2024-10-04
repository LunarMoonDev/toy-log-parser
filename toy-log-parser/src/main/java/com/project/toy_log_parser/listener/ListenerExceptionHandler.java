package com.project.toy_log_parser.listener;

import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service(value = "listenerExceptionHandler")
@Slf4j
public class ListenerExceptionHandler implements KafkaListenerErrorHandler {

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException e) {
        log.error("Kafka listener exception: {}", e);
        return null;
    }
}

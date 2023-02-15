package com.ecommercemicroservices.notificationservice.consumer;

import com.ecommercemicroservices.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailConsumer {
    private final EmailService emailService;
    @KafkaListener(topics = "msg")
    public void processAlert(@Payload String dto) {
        log.debug("consume message for topic: msg" +dto);
//        if (dto.getRecipients().size() > 0)
//            mailService.sendSimpleEmail(dto.getTitle(), dto.getBody(), dto.getRecipients());
    }
}

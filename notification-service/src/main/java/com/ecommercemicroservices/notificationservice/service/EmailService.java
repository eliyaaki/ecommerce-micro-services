package com.ecommercemicroservices.notificationservice.service;

import java.util.List;

public interface EmailService {
    void sendSimpleEmail(String title, String body, List<String> recipients);
}

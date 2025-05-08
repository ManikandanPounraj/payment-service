package com.rak.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.rak.payment.model.NotificationRequest;

@FeignClient(name = "notification-service", url = "http://localhost:8085")
public interface NotificationClient {
    @PostMapping("/api/notify")
    void sendNotification(@RequestBody NotificationRequest req);
}

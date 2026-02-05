package org.ko4inage.notificationservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.inbox")
@Data
public class InboxProperties {
    private int batchSize;
    private long delayMs;
}

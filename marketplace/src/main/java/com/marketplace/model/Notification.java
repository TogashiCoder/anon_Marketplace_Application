package com.marketplace.model;

import com.marketplace.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Setter @Getter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Notification {
    private Long id;
    private String message;
    private LocalDateTime timestamp;
    private NotificationType notificationType;
    private Boolean isRead;
    private User receiver;
    private User sender;
    private Product newProduct;
}

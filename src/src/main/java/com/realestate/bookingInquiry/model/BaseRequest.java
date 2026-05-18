package com.ResellLK.demo.model.interaction;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class BaseRequest implements Notifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long propertyId;
    private Long buyerId;
    
    private LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public void sendNotification(String message) {
        // Mock notification logic
        System.out.println("Notification to User " + buyerId + ": " + message);
    }
}

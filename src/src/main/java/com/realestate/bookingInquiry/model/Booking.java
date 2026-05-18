package com.ResellLK.demo.model.interaction;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Booking extends BaseRequest implements Bookable {
    
    private LocalDateTime scheduledTime;
    
    // Encapsulation via State Machine logic
    private String status = "PENDING"; // PENDING -> CONFIRMED / CANCELLED

    @Override
    public void confirmBooking() {
        if ("PENDING".equals(this.status)) {
            this.status = "CONFIRMED";
            sendNotification("Your booking has been confirmed for " + scheduledTime);
        } else {
            throw new IllegalStateException("Cannot confirm booking from state: " + this.status);
        }
    }

    @Override
    public void cancelBooking() {
        if ("PENDING".equals(this.status) || "CONFIRMED".equals(this.status)) {
            this.status = "CANCELLED";
            sendNotification("Your booking has been cancelled.");
        } else {
            throw new IllegalStateException("Booking is already cancelled.");
        }
    }
}

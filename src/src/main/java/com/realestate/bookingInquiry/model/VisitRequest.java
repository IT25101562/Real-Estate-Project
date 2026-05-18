package com.ResellLK.demo.model.interaction;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Concrete subclass — represents a scheduled property viewing request.
 * Demonstrates INHERITANCE: extends the abstract Inquiry base class.
 * Demonstrates POLYMORPHISM: provides its own getDetails() and getResponse().
 * Implements BOOKABLE: state-machine methods confirmBooking() / cancelBooking().
 */
@Entity
@DiscriminatorValue("VISIT")
@Getter
@Setter
@NoArgsConstructor
public class VisitRequest extends Inquiry implements Bookable {

    private LocalDateTime scheduledTime;

    private String status = "PENDING"; // PENDING → CONFIRMED / CANCELLED

    public VisitRequest(Long propertyId, Long buyerId, String contactNumber, LocalDateTime scheduledTime) {
        setPropertyId(propertyId);
        setBuyerId(buyerId);
        setContactNumber(contactNumber);
        this.scheduledTime = scheduledTime;
    }

    // ── State Machine (Bookable interface) ──────────────────────────────────

    @Override
    public void confirmBooking() {
        if ("PENDING".equals(this.status)) {
            this.status = "CONFIRMED";
            System.out.println("[NOTIFICATION] Buyer #" + getBuyerId()
                    + ": Viewing for property #" + getPropertyId()
                    + " CONFIRMED for " + scheduledTime);
        } else {
            throw new IllegalStateException(
                    "Cannot confirm a viewing that is already: " + this.status);
        }
    }

    @Override
    public void cancelBooking() {
        if ("PENDING".equals(this.status) || "CONFIRMED".equals(this.status)) {
            this.status = "CANCELLED";
            System.out.println("[NOTIFICATION] Buyer #" + getBuyerId()
                    + ": Viewing for property #" + getPropertyId() + " CANCELLED.");
        } else {
            throw new IllegalStateException("Viewing is already cancelled.");
        }
    }

    // ── Polymorphism ────────────────────────────────────────────────────────

    @Override
    public String getDetails() {
        return "Visit Request | Property #" + getPropertyId()
                + " | Buyer #" + getBuyerId()
                + " | Scheduled: " + scheduledTime
                + " | Status: " + status;
    }

    @Override
    public String getResponse() {
        if ("CONFIRMED".equals(status)) {
            return "Your viewing for property #" + getPropertyId()
                    + " is CONFIRMED for " + scheduledTime + ".";
        } else if ("CANCELLED".equals(status)) {
            return "Your viewing for property #" + getPropertyId()
                    + " has been CANCELLED.";
        } else {
            return "Your viewing request for property #" + getPropertyId()
                    + " is PENDING seller confirmation.";
        }
    }
}

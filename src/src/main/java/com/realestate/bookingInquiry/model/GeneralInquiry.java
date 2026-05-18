package com.ResellLK.demo.model.interaction;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Concrete subclass — represents a text-based buyer inquiry to a seller.
 * Demonstrates INHERITANCE: extends the abstract Inquiry base class.
 * Demonstrates POLYMORPHISM: provides its own getDetails() and getResponse().
 */
@Entity
@DiscriminatorValue("GENERAL")
@Getter
@Setter
@NoArgsConstructor
public class GeneralInquiry extends Inquiry {

    @Column(length = 2000)
    private String message;

    public GeneralInquiry(Long propertyId, Long buyerId, String contactNumber, String message) {
        setPropertyId(propertyId);
        setBuyerId(buyerId);
        setContactNumber(contactNumber);
        this.message = message;
    }

    @Override
    public String getDetails() {
        return "General Inquiry | Property #" + getPropertyId()
                + " | Buyer #" + getBuyerId()
                + " | Message: \"" + message + "\"";
    }

    @Override
    public String getResponse() {
        return "Thank you for inquiring about property #" + getPropertyId()
                + ". A representative will contact you at "
                + getContactNumber() + " shortly.";
    }
}

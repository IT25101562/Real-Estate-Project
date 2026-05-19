package com.ResellLK.demo.controller;

import com.ResellLK.demo.model.interaction.GeneralInquiry;
import com.ResellLK.demo.model.interaction.Inquiry;
import com.ResellLK.demo.model.interaction.VisitRequest;
import com.ResellLK.demo.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Component 04: Property Booking & Inquiry Management.
 *
 * Exposes all CRUD operations for GeneralInquiry and VisitRequest.
 * All DB interactions go through InquiryService → Repository interfaces.
 */
@RestController
@RequestMapping("/api/interactions")
public class InteractionController {

    @Autowired
    private InquiryService inquiryService;

    // ── CREATE ───────────────────────────────────────────────────────────────

    /** Submit a General Inquiry (text message to seller). */
    @PostMapping("/inquiry")
    public ResponseEntity<?> submitInquiry(@RequestBody GeneralInquiry inquiry) {
        GeneralInquiry saved = inquiryService.submitInquiry(inquiry);
        return ResponseEntity.ok(saved);
    }

    /** Schedule a property viewing (Visit Request / Booking). */
    @PostMapping("/booking")
    public ResponseEntity<?> createBooking(@RequestBody VisitRequest visitRequest) {
        VisitRequest saved = inquiryService.createVisitRequest(visitRequest);
        return ResponseEntity.ok(saved);
    }

    // ── READ ─────────────────────────────────────────────────────────────────

    /** Get all interactions (both inquiry types combined). */
    @GetMapping
    public ResponseEntity<List<Inquiry>> getAllInteractions() {
        return ResponseEntity.ok(inquiryService.getAllInteractions());
    }

    /** Get a single interaction by ID. */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return inquiryService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Get all visit requests (bookings) for a specific buyer — used by My Bookings page. */
    @GetMapping("/booking/buyer/{buyerId}")
    public ResponseEntity<List<VisitRequest>> getBuyerBookings(@PathVariable Long buyerId) {
        return ResponseEntity.ok(inquiryService.getVisitsByBuyer(buyerId));
    }

    /** Get all general inquiries for a specific property — used by Seller Inquiry Panel. */
    @GetMapping("/inquiry/property/{propertyId}")
    public ResponseEntity<List<GeneralInquiry>> getPropertyInquiries(@PathVariable Long propertyId) {
        return ResponseEntity.ok(inquiryService.getInquiriesByProperty(propertyId));
    }

    /** Get all visit requests for a specific property — used by Seller Inquiry Panel. */
    @GetMapping("/booking/property/{propertyId}")
    public ResponseEntity<List<VisitRequest>> getPropertyBookings(@PathVariable Long propertyId) {
        return ResponseEntity.ok(inquiryService.getVisitsByProperty(propertyId));
    }

    /** Get all inquiries (text) submitted by a specific buyer. */
    @GetMapping("/inquiry/buyer/{buyerId}")
    public ResponseEntity<List<GeneralInquiry>> getBuyerInquiries(@PathVariable Long buyerId) {
        return ResponseEntity.ok(inquiryService.getInquiriesByBuyer(buyerId));
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────

    /** Edit the message text of an existing inquiry. */
    @PutMapping("/inquiry/{id}")
    public ResponseEntity<?> updateInquiry(@PathVariable Long id,
                                           @RequestBody Map<String, String> body) {
        return inquiryService.updateInquiry(id, body.get("message"))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Reschedule an existing visit request to a new date/time. */
    @PutMapping("/booking/{id}/reschedule")
    public ResponseEntity<?> rescheduleBooking(@PathVariable Long id,
                                               @RequestBody Map<String, String> body) {
        try {
            LocalDateTime newTime = LocalDateTime.parse(body.get("scheduledTime"));
            return inquiryService.rescheduleVisit(id, newTime)
                    .map(updated -> ResponseEntity.ok((Object) updated))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format. Use ISO-8601, e.g. 2026-06-15T10:00:00");
        }
    }

    /** Confirm a pending visit request (Seller action — state machine transition). */
    @PutMapping("/booking/{id}/confirm")
    public ResponseEntity<?> confirmBooking(@PathVariable Long id) {
        try {
            return inquiryService.confirmVisit(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Cancel a pending or confirmed visit request (Buyer or Seller action). */
    @PutMapping("/booking/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        try {
            return inquiryService.cancelVisit(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    /** Delete (remove) a general inquiry by ID. */
    @DeleteMapping("/inquiry/{id}")
    public ResponseEntity<?> deleteInquiry(@PathVariable Long id) {
        return inquiryService.deleteInquiry(id)
                ? ResponseEntity.ok("Inquiry deleted successfully")
                : ResponseEntity.notFound().build();
    }

    /** Delete (remove) a visit request / booking by ID. */
    @DeleteMapping("/booking/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        return inquiryService.deleteVisitRequest(id)
                ? ResponseEntity.ok("Booking deleted successfully")
                : ResponseEntity.notFound().build();
    }
}

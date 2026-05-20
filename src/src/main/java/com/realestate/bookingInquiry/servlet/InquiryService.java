package com.ResellLK.demo.service;

import com.ResellLK.demo.model.interaction.GeneralInquiry;
import com.ResellLK.demo.model.interaction.Inquiry;
import com.ResellLK.demo.model.interaction.VisitRequest;
import com.ResellLK.demo.repository.GeneralInquiryRepository;
import com.ResellLK.demo.repository.InquiryRepository;
import com.ResellLK.demo.repository.VisitRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Component 04: Property Booking & Inquiry Management.
 *
 * All DB interactions go through Repository interfaces — no JDBC templates.
 * Polymorphic calls to getDetails() and getResponse() happen here.
 */
@Service
@Transactional
public class InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private GeneralInquiryRepository generalInquiryRepository;

    @Autowired
    private VisitRequestRepository visitRequestRepository;

    // ── General Inquiry CRUD ─────────────────────────────────────────────────

    public GeneralInquiry submitInquiry(GeneralInquiry inquiry) {
        GeneralInquiry saved = generalInquiryRepository.save(inquiry);
        // Polymorphic calls — runtime dispatch to GeneralInquiry's implementation
        System.out.println("[INQUIRY SAVED] " + saved.getDetails());
        System.out.println("[AUTO-RESPONSE] " + saved.getResponse());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<GeneralInquiry> getInquiriesByProperty(Long propertyId) {
        return generalInquiryRepository.findByPropertyId(propertyId);
    }

    @Transactional(readOnly = true)
    public List<GeneralInquiry> getInquiriesByBuyer(Long buyerId) {
        return generalInquiryRepository.findByBuyerId(buyerId);
    }

    public Optional<GeneralInquiry> updateInquiry(Long id, String newMessage) {
        return generalInquiryRepository.findById(id).map(inq -> {
            inq.setMessage(newMessage);
            return generalInquiryRepository.save(inq);
        });
    }

    public Optional<GeneralInquiry> replyToInquiry(Long id, String reply) {
        return generalInquiryRepository.findById(id).map(inq -> {
            inq.setSellerReply(reply);
            return generalInquiryRepository.save(inq);
        });
    }

    public boolean deleteInquiry(Long id) {
        if (generalInquiryRepository.existsById(id)) {
            generalInquiryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ── Visit Request (Appointment Booking) CRUD ─────────────────────────────

    public VisitRequest createVisitRequest(VisitRequest visitRequest) {
        visitRequest.setStatus("PENDING");
        VisitRequest saved = visitRequestRepository.save(visitRequest);
        // Polymorphic calls — runtime dispatch to VisitRequest's implementation
        System.out.println("[VISIT SAVED] " + saved.getDetails());
        System.out.println("[AUTO-RESPONSE] " + saved.getResponse());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<VisitRequest> getVisitsByBuyer(Long buyerId) {
        return visitRequestRepository.findByBuyerId(buyerId);
    }

    @Transactional(readOnly = true)
    public List<VisitRequest> getVisitsByProperty(Long propertyId) {
        return visitRequestRepository.findByPropertyId(propertyId);
    }

    public Optional<VisitRequest> rescheduleVisit(Long id, LocalDateTime newTime) {
        return visitRequestRepository.findById(id).map(vr -> {
            vr.setScheduledTime(newTime);
            return visitRequestRepository.save(vr);
        });
    }

    public Optional<String> confirmVisit(Long id) {
        return visitRequestRepository.findById(id).map(vr -> {
            vr.confirmBooking();                    // State machine — Bookable interface
            visitRequestRepository.save(vr);
            return vr.getResponse();                // Polymorphic response
        });
    }

    public Optional<String> cancelVisit(Long id) {
        return visitRequestRepository.findById(id).map(vr -> {
            vr.cancelBooking();                     // State machine — Bookable interface
            visitRequestRepository.save(vr);
            return vr.getResponse();                // Polymorphic response
        });
    }

    public boolean deleteVisitRequest(Long id) {
        if (visitRequestRepository.existsById(id)) {
            visitRequestRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ── Combined reads ───────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Inquiry> getAllInteractions() {
        return inquiryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Inquiry> getById(Long id) {
        return inquiryRepository.findById(id);
    }
}

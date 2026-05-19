package com.ResellLK.demo.repository;

import com.ResellLK.demo.model.interaction.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for all Inquiry subtypes combined.
 * Spring Data JPA generates all SQL — no JDBC templates needed.
 */
@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByBuyerId(Long buyerId);
    List<Inquiry> findByPropertyId(Long propertyId);
}

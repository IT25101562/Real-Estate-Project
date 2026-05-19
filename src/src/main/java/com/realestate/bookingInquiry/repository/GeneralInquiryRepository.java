package com.ResellLK.demo.repository;

import com.ResellLK.demo.model.interaction.GeneralInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for GeneralInquiry entities only.
 * Spring Data JPA automatically scopes queries to the GENERAL discriminator value.
 */
@Repository
public interface GeneralInquiryRepository extends JpaRepository<GeneralInquiry, Long> {
    List<GeneralInquiry> findByPropertyId(Long propertyId);
    List<GeneralInquiry> findByBuyerId(Long buyerId);
}

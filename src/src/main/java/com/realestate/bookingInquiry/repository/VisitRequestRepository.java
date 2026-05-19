package com.ResellLK.demo.repository;

import com.ResellLK.demo.model.interaction.VisitRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for VisitRequest entities only.
 * Spring Data JPA automatically scopes queries to the VISIT discriminator value.
 */
@Repository
public interface VisitRequestRepository extends JpaRepository<VisitRequest, Long> {
    List<VisitRequest> findByBuyerId(Long buyerId);
    List<VisitRequest> findByPropertyId(Long propertyId);
    List<VisitRequest> findByBuyerIdAndStatus(Long buyerId, String status);
    List<VisitRequest> findByPropertyIdAndStatus(Long propertyId, String status);
}

package com.example.vendor_service.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.vendor_service.model.entity.Vendor;

@Repository
public interface VendorRepo extends JpaRepository<Vendor, UUID> {
    @Query("SELECT COUNT(v) FROM Vendor v WHERE v.nameStore = :name")
    int existStoreName(String name);

    @Query("SELECT v FROM Vendor v WHERE v.id = :vendorId")
    Optional<Vendor> findByVendorId(UUID vendorId);

    @Query("SELECT v FROM Vendor v WHERE v.userId = :userId")
    Vendor findByUserId(String userId);
}

package com.example.inventory_service.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.inventory_service.model.entity.Inventory;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, String> {

    @Query("SELECT iv FROM Inventory iv WHERE iv.variantId = :variantId")
    Optional<Inventory> findByVariantId(String variantId);

}

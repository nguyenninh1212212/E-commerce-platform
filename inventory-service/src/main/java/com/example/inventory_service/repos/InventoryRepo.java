package com.example.inventory_service.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.inventory_service.model.entity.Inventory;

import jakarta.transaction.Transactional;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, String> {

    @Query("SELECT iv FROM Inventory iv WHERE iv.variantId = :variantId")
    Optional<Inventory> findByVariantId(String variantId);

    @Query("SELECT iv FROM Inventory iv WHERE iv.variantId IN :variantIds")
    List<Inventory> findByVariantIds(List<String> variantIds);

    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.stockAvaiable = :stockAvaiable WHERE i.variantId = :variantId")
    int updateQuantity(String variantId, int quantity);

}

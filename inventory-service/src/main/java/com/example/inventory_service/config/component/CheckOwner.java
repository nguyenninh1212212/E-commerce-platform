package com.example.inventory_service.config.component;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.inventory_service.repos.InventoryRepo;

import lombok.RequiredArgsConstructor;

@Component("CheckOwner")
@RequiredArgsConstructor
public class CheckOwner {
    private final InventoryRepo inventoryRepo;

    public boolean IsOwner(List<String> variantIds) {
        int found = inventoryRepo.findByVariantIds(variantIds).size();
        return found == variantIds.size() ? true : false;

    }

}

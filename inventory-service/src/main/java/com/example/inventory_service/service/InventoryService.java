package com.example.inventory_service.service;

import java.util.List;

import com.example.inventory_service.model.dto.event.VariantCreatedEvent;
import com.example.inventory_service.model.dto.req.InventoryReq;
import com.example.inventory_service.model.dto.res.InventoryRes;
import com.example.inventory_service.model.dto.res.InventoryUserRes;

public interface InventoryService {
    List<InventoryRes> getAllInventory(List<String> variantIds);

    List<InventoryUserRes> getUserInventory(List<String> variantIds);

    void createInventory(List<VariantCreatedEvent> req);

    void updateInventory(InventoryReq req);

    void releaseStock(int quantity, String variantId);

    void confirmStock(int quantity, String variantId);

    void reserveStock(int quantity, String variantId);
}

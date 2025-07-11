package com.example.inventory_service.controller;

import com.example.inventory_service.model.dto.event.VariantCreatedEvent;
import com.example.inventory_service.service.InventoryService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/all")
    public ResponseEntity<?> getInventory(@RequestParam List<String> variantId) {
        return ResponseEntity.ok(inventoryService.getAllInventory(variantId));
    }

    @GetMapping("/view")
    public ResponseEntity<?> getUserInventory(@RequestParam List<String> variantId) {
        return ResponseEntity.ok(inventoryService.getUserInventory(variantId));
    }

    @PostMapping
    public ResponseEntity<?> createInventory(@RequestBody List<VariantCreatedEvent> req) {
        inventoryService.createInventory(req);
        return ResponseEntity.ok("Inventory created successfully");
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserveStock(
            @RequestParam int quantity,
            @RequestParam String variantId) {
        inventoryService.reserveStock(quantity, variantId);
        return ResponseEntity.ok("Stock reserved");
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmStock(
            @RequestParam int quantity,
            @RequestParam String variantId) {
        inventoryService.confirmStock(quantity, variantId);
        return ResponseEntity.ok("Stock confirmed");
    }

    @PostMapping("/release")
    public ResponseEntity<?> releaseStock(
            @RequestParam int quantity,
            @RequestParam String variantId) {
        inventoryService.releaseStock(quantity, variantId);
        return ResponseEntity.ok("Stock released");
    }
}

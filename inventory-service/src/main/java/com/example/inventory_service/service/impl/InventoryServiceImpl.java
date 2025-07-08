package com.example.inventory_service.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.inventory_service.Mapper.ToModel;
import com.example.inventory_service.excep.extd.AlreadyExistsException;
import com.example.inventory_service.excep.extd.NotFoundException;
import com.example.inventory_service.excep.extd.OutOfStockException;
import com.example.inventory_service.model.dto.event.VariantCreatedEvent;
import com.example.inventory_service.model.dto.res.InventoryRes;
import com.example.inventory_service.model.dto.res.InventoryUserRes;
import com.example.inventory_service.model.entity.Inventory;
import com.example.inventory_service.repos.InventoryRepo;
import com.example.inventory_service.service.InventoryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepo repo;
    @Value("${limit.lowStockThresold}")
    private int lowStockThresold;

    private <R> List<R> getInventory(List<String> variantIds, Function<Inventory, R> mapper) {
        return variantIds.stream()
                .map(repo::findByVariantId)
                .flatMap(Optional::stream)
                .map(mapper)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createInventory(List<VariantCreatedEvent> reqList) {
        reqList.forEach(req -> {
            repo.findByVariantId(req.getVariantId()).ifPresent(inv -> {
                throw new AlreadyExistsException(req.getVariantId());
            });
            Inventory inventory = Inventory.builder()
                    .lastUpdateAt(Instant.now())
                    .variantId(req.getVariantId())
                    .lowStockThresold(lowStockThresold)
                    .stockAvaiable(req.getQuantity())
                    .stockReversed(0)
                    .build();
            repo.save(inventory);
        });
    }

    @Override
    @Transactional
    public void reserveStock(int quantity, String variantId) {
        Inventory inv = repo.findByVariantId(variantId)
                .orElseThrow(() -> new NotFoundException(variantId));
        if (inv.getStockAvaiable() < quantity) {
            throw new OutOfStockException(variantId);
        }
        inv.setStockAvaiable(inv.getStockAvaiable() - quantity);
        inv.setStockReversed(inv.getStockReversed() + quantity);
        inv.setLastUpdateAt(Instant.now());
        repo.save(inv);
    }

    @Override
    @Transactional
    public void confirmStock(int quantity, String variantId) {
        Inventory inv = repo.findByVariantId(variantId)
                .orElseThrow(() -> new NotFoundException(variantId));
        inv.setStockReversed(inv.getStockReversed() - quantity);
        inv.setStockTotal(inv.getStockTotal() - quantity);
        inv.setLastUpdateAt(Instant.now());
        repo.save(inv);
    }

    @Override
    @Transactional
    public void releaseStock(int quantity, String variantId) {
        Inventory inv = repo.findByVariantId(variantId)
                .orElseThrow(() -> new NotFoundException(variantId));
        inv.setStockAvaiable(inv.getStockAvaiable() + quantity);
        inv.setStockReversed(inv.getStockReversed() - quantity);
        inv.setLastUpdateAt(Instant.now());
        repo.save(inv);
    }

    @Override
    public List<InventoryRes> getAllInventory(List<String> variantIds) {
        return getInventory(variantIds, ToModel::toRes);
    }

    @Override
    public List<InventoryUserRes> getUserInventory(List<String> variantIds) {
        return getInventory(variantIds, ToModel::toUserViewRes);

    }

    @Override
    @Transactional
    @PreAuthorize("@CheckOwner.isOwner(#variantIds)")
    public void deleteInventorys(List<String> variantIds) {
        List<Inventory> inventories = repo.findByVariantIds(variantIds);
        if (inventories.isEmpty()) {
            throw new NotFoundException("No inventories found for the provided variant IDs.");
        }
        repo.deleteAll(inventories);
    }

    @Override
    public void updateInventoryList(List<VariantCreatedEvent> events) {
        for (VariantCreatedEvent event : events) {
            repo.updateQuantity(event.getVariantId(), event.getQuantity());
        }
    }

}

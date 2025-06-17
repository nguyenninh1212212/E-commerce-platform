package com.example.inventory_service.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.inventory_service.Mapper.ToModel;
import com.example.inventory_service.excep.extd.AlreadyExistsException;
import com.example.inventory_service.excep.extd.NotFoundException;
import com.example.inventory_service.excep.extd.OutOfStockException;
import com.example.inventory_service.model.dto.req.InventoryReq;
import com.example.inventory_service.model.dto.res.InventoryRes;
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

    @Override
    @Transactional
    public void createInventory(List<InventoryReq> reqList) {
        for (InventoryReq req : reqList) {
            Optional<Inventory> exist = repo.findByVariantId(req.getVariantId());
            if (exist.isPresent()) {
                throw new AlreadyExistsException(req.getVariantId());
            }
            Inventory inventory = Inventory.builder()
                    .lastUpdateAt(Instant.now())
                    .variantId(req.getVariantId())
                    .lowStockThresold(lowStockThresold)
                    .stockAvaiable(req.getStockAvailable())
                    .stockReversed(0)
                    .build();
            repo.save(inventory);
        }
    }

    @Override
    public void updateInventory(InventoryReq req) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateInventory'");
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
    public List<InventoryRes> getInventory(List<String> variantIds) {
        List<InventoryRes> inventoryResList = new ArrayList<>();
        for (String variantId : variantIds) {
            repo.findByVariantId(variantId).ifPresent(iv -> {
                inventoryResList.add(ToModel.toRes(iv));
            });
            ;
        }
        return inventoryResList;
    }

}

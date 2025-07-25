package com.example.variant_service.grpc.client;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.variant_service.mapper.ToModel;
import com.example.variant_service.model.dto.res.InventoryUserRes;

import inventory.InventoryServiceGrpc.InventoryServiceBlockingStub;
import inventory.VariantIdRequest;
import inventory.VariantIdRequestList;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Slf4j
@NoArgsConstructor
@Component
public class InventoryGrpcClient {
    @GrpcClient("inventory-client")
    private InventoryServiceBlockingStub blockingStub;

    public List<InventoryUserRes> getUserInventorys(List<String> variantIds) {
        try {
            VariantIdRequestList idRequests = VariantIdRequestList.newBuilder()
                    .addAllVariantIds(
                            variantIds.stream()
                                    .map(variantId -> VariantIdRequest.newBuilder().setVariantId(variantId).build())
                                    .toList())
                    .build();
            return blockingStub.getUserInventory(idRequests).getViewsList().stream()
                    .map(ToModel::toUserInventoryRes).toList();
        } catch (Exception e) {
            log.error("Error in get inventory : {}", e.getMessage());
        }
        return java.util.Collections.emptyList();
    }

}

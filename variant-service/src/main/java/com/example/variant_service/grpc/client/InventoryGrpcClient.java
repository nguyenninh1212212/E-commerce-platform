package com.example.variant_service.grpc.client;

import inventory.InventoryServiceGrpc.InventoryServiceBlockingStub;
import lombok.NoArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;

@NoArgsConstructor
public class InventoryGrpcClient {
    @GrpcClient("inventory-client")
    private InventoryServiceBlockingStub blockingStub;

}

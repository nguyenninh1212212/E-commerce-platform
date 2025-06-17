package com.example.inventory_service.grpc.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import com.example.inventory_service.model.dto.req.InventoryReq;
import com.example.inventory_service.service.InventoryService;

import inventory.InventoryRequest;
import inventory.InventoryResponse;
import inventory.InventoryResponseList;
import inventory.Message;
import inventory.VariantIdRequest;
import inventory.InventoryServiceGrpc.InventoryServiceImplBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class InventoryGrpcService extends InventoryServiceImplBase {

    private final InventoryService inventoryService;

    @Value("${limit.lowStockThresold}")
    private int TheLowStockThresold;

    @Override
    public void createInventory(inventory.InventoryRequestList request,
            io.grpc.stub.StreamObserver<inventory.Message> responseObserver) {
        List<InventoryReq> reqList = new ArrayList<>();
        for (InventoryRequest reqProto : request.getRequestsList()) {
            try {
                InventoryReq req = InventoryReq
                        .builder()
                        .stockAvailable(reqProto.getStockAvailable())
                        .variantId(reqProto.getVariantId())
                        .build();
                reqList.add(req);
            } catch (Exception e) {
                log.info("Create inventory error : ", e.getMessage());
            }
        }
        inventoryService.createInventory(reqList);
        Message message = Message.newBuilder().setValue("Inventory build successfully").build();
        responseObserver.onNext(message);
        responseObserver.onCompleted();
    }

    @Override
    public void confirmStock(inventory.StockRequest request,
            io.grpc.stub.StreamObserver<inventory.Message> responseObserver) {
        try {
            inventoryService.confirmStock(request.getQuantity(), request.getVariantId());
            responseObserver.onNext(Message.newBuilder().setValue("Confirm stock success").build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.info("Confrim stock error  : ", e.getMessage());
        }
    }

    @Override
    public void releaseStock(inventory.StockRequest request,
            io.grpc.stub.StreamObserver<inventory.Message> responseObserver) {
        try {
            inventoryService.releaseStock(request.getQuantity(), request.getVariantId());
            responseObserver.onNext(Message.newBuilder().setValue("Release stock success").build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.info("Release stock error  : ", e.getMessage());
        }
    }

    @Override
    public void reserveStock(inventory.StockRequest request,
            io.grpc.stub.StreamObserver<inventory.Message> responseObserver) {
        try {
            inventoryService.reserveStock(request.getQuantity(), request.getVariantId());
            responseObserver.onNext(Message.newBuilder().setValue("Reserve stock success").build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.info("Reserve stock error  : ", e.getMessage());
        }

    }

    @Override
    public void getInventory(inventory.VariantIdRequestList request,
            io.grpc.stub.StreamObserver<inventory.InventoryResponseList> responseObserver) {
        try {
            List<String> VariantIdList = new ArrayList<>();
            for (VariantIdRequest variantId : request.getVariantIdList()) {
                VariantIdList.add(variantId.getVariantId());
            }
            List<InventoryResponse> inventoryResponses = inventoryService.getInventory(VariantIdList)
                    .stream()
                    .map(ivt -> InventoryResponse.newBuilder().build()).collect(Collectors.toList());
            InventoryResponseList inventoryResponseList = InventoryResponseList.newBuilder()
                    .addAllResponses(inventoryResponses)
                    .build();
            responseObserver.onNext(inventoryResponseList);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.info("Get inventorys error  : ", e.getMessage());
        }

    }

}

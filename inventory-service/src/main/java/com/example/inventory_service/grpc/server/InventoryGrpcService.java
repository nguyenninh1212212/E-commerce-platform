package com.example.inventory_service.grpc.server;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import com.example.inventory_service.Mapper.ToModel;
import com.example.inventory_service.service.InventoryService;

import inventory.InventoryResponse;
import inventory.InventoryResponseList;
import inventory.InventoryUserView;
import inventory.InventoryUserViewList;
import inventory.Message;
import inventory.VariantIdRequest;
import inventory.VariantIdRequestList;
import io.grpc.stub.StreamObserver;
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
    public void getInventory(VariantIdRequestList request,
            StreamObserver<InventoryResponseList> responseObserver) {
        try {
            List<String> variantIdList = request.getVariantIdList().stream()
                    .map(VariantIdRequest::getVariantId)
                    .collect(Collectors.toList());

            List<InventoryResponse> inventoryResponses = inventoryService.getAllInventory(variantIdList).stream()
                    .map(ToModel::toInventoryProtoResponse)
                    .collect(Collectors.toList());
            InventoryResponseList responseList = InventoryResponseList.newBuilder()
                    .addAllResponses(inventoryResponses)
                    .build();

            responseObserver.onNext(responseList);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Get inventorys error: ", e);
            responseObserver.onError(e);

        }
    }

    @Override
    public void getUserInventory(inventory.VariantIdRequestList request,
            io.grpc.stub.StreamObserver<inventory.InventoryUserViewList> responseObserver) {
        try {
            List<String> variantIdList = request.getVariantIdList().stream()
                    .map(VariantIdRequest::getVariantId)
                    .collect(Collectors.toList());
            List<InventoryUserView> inventoryResponses = inventoryService.getAllInventory(variantIdList).stream()
                    .map(ToModel::toInventoryUserProtoResponse)
                    .collect(Collectors.toList());
            InventoryUserViewList responseList = InventoryUserViewList.newBuilder()
                    .addAllViews(inventoryResponses)
                    .build();
            responseObserver.onNext(responseList);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Get inventorys user error: ", e);
            responseObserver.onError(e);
        }
    }

}

package com.example.variant_service.grpc.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.variant_service.model.dto.req.VariantReq;
import com.example.variant_service.model.dto.res.VariantRes;
import com.example.variant_service.model.dto.res.VariantUserRes;
import com.example.variant_service.model.enums.Status;
import com.example.variant_service.service.VariantServ;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import variant.VariantServiceGrpc;
import variant.GetVariantsRequest;
import variant.VariantResponse;
import variant.Attribute;
import variant.CreateVariantsRequest;
import variant.Message; // Renamed from 'res' in proto to match Java naming convention

@Slf4j
@GrpcService
public class VariantService extends VariantServiceGrpc.VariantServiceImplBase {

    @Autowired
    private VariantServ variantServ;

    private static variant.Status toProtoStatus(Status status) {
        if (status == null)
            return variant.Status.UNKNOWN;
        return switch (status) {
            case SOLD_OUT -> variant.Status.SOLD_OUT;
            case IN_STOCK -> variant.Status.IN_STOCK;
            case PRE_ORDER -> variant.Status.PRE_ORDER;
            case DISCONTINUED -> variant.Status.DISCONTINUED;
            default -> variant.Status.UNKNOWN;
        };
    }

    private static Status toStatus(variant.Status status) {
        return switch (status) {
            case SOLD_OUT -> Status.SOLD_OUT;
            case IN_STOCK -> Status.IN_STOCK;
            case PRE_ORDER -> Status.PRE_ORDER;
            case DISCONTINUED -> Status.DISCONTINUED;
            default -> Status.UNKNOWN;
        };
    }

    @Override
    public void getVariants(GetVariantsRequest request, io.grpc.stub.StreamObserver<VariantResponse> responseObserver) {
        try {
            List<VariantUserRes> variantList = variantServ.findByProductId(request.getProductId());
            for (VariantUserRes variant : variantList) {
                VariantResponse.Builder builder = VariantResponse.newBuilder()
                        .setId(variant.getId())
                        .setPrice(variant.getPrice())
                        .setSku(variant.getSku())
                        .setStatus(toProtoStatus(variant.getStatus()))
                        .addAllAttributes(
                                variant.getAttributes().stream()
                                        .map(attr -> Attribute.newBuilder()
                                                .setName(attr.getName())
                                                .addAllValues(attr.getValues())
                                                .build())
                                        .collect(Collectors.toList()));

                responseObserver.onNext(builder.build());
            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error in getVariants: {}", e.getMessage(), e);
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Lỗi server khi xử lý Variant: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void createVariants(CreateVariantsRequest request, io.grpc.stub.StreamObserver<Message> responseObserver) {
        try {
            List<VariantReq> variantList = new ArrayList<>();
            request.getVariantsList().forEach(variantReq -> {
                variantList.add(
                        VariantReq.builder()
                                .price(variantReq.getPrice())
                                .status(toStatus(variantReq.getStatus()))
                                .sku(variantReq.getSku())
                                .attributes(variantReq.getAttributesList().stream()
                                        .map(attr -> new com.example.variant_service.model.Attribute(attr.getName(),
                                                attr.getValuesList()))
                                        .collect(Collectors.toList()))
                                .build());
            });
            variantServ.createVariantList(variantList, request.getProductId());
            Message response = Message.newBuilder().setValue("Variants created successfully").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error in createVariants: {}", e.getMessage(), e);
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Lỗi server khi tạo Variant: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void deleteVariantsByProductId(GetVariantsRequest request,
            io.grpc.stub.StreamObserver<Message> responseObserver) {
        try {
            variantServ.deleteVariantsByProductId(request.getProductId());
            Message response = Message.newBuilder().setValue("Variants deleted successfully").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error in deleteVariantsByProductId: {}", e.getMessage(), e);
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Lỗi server khi xóa Variant: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }
}

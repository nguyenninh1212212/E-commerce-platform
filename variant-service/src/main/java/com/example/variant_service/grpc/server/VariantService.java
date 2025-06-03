package com.example.variant_service.grpc.server;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.variant_service.model.dto.req.VariantReq;
import com.example.variant_service.model.dto.res.VariantRes;
import com.example.variant_service.model.enums.Status;
import com.example.variant_service.service.VariantServ;

import net.devh.boot.grpc.server.service.GrpcService;
import variant.VariantServiceGrpc;
import variant.res;
import variant.GetVariantsRequest;
import variant.VariantResponse;
import variant.Attribute;
import variant.CreateVariantsRequest;

@GrpcService
public class VariantService extends VariantServiceGrpc.VariantServiceImplBase {
    @Autowired
    private VariantServ variantServ;

    private static variant.Status toProtoStatus(com.example.variant_service.model.enums.Status status) {
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

    public void getVariants(GetVariantsRequest request, io.grpc.stub.StreamObserver<VariantResponse> responseObserver) {
        try {
            java.util.List<VariantRes> variantList = variantServ.findByProductId(request.getProductId());

            for (VariantRes variant : variantList) {
                VariantResponse.Builder builder = VariantResponse.newBuilder()
                        .setId(variant.getId())
                        .setSku(variant.getSku())
                        .setBarcode(variant.getBarcode())
                        .setPrice(variant.getPrice())
                        .setImgurl(variant.getImgurl())
                        .setQuantity(variant.getQuantity())
                        .setStatus(toProtoStatus(variant.getStatus()));

                builder.addAllAttributes(
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
            e.printStackTrace();
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Lỗi server khi xử lý Variant: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void createVariants(CreateVariantsRequest request, io.grpc.stub.StreamObserver<res> responseObserver) {
        try {
            request.getVariantsList().forEach(variantReq -> {
                VariantReq variant = VariantReq.builder()
                        .sku(variantReq.getSku())
                        .barcode(variantReq.getBarcode())
                        .price(variantReq.getPrice())
                        .imgurl(variantReq.getImgurl())
                        .quantity(variantReq.getQuantity())
                        .status(toStatus(variantReq.getStatus()))
                        .attributes(variantReq.getAttributesList().stream()
                                .map(attr -> new com.example.variant_service.model.Attribute(attr.getName(),
                                        attr.getValuesList()))
                                .collect(Collectors.toList()))
                        .build();

                variantServ.createVariant(variant, variantReq.getProductId());
            });

            res response = res.newBuilder()
                    .setValue("Variants created successfully")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Lỗi server khi tạo Variant: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }
}

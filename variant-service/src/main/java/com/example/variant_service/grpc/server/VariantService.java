package com.example.variant_service.grpc.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.variant_service.mapper.ToModel;
import com.example.variant_service.mapper.ToProto;
import com.example.variant_service.model.dto.req.VariantReq;
import com.example.variant_service.model.dto.res.VariantUserRes;
import com.example.variant_service.service.VariantServ;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import variant.VariantServiceGrpc;
import variant.GetVariantsRequest;
import variant.VariantResponse;
import shared.Attribute;
import variant.CreateVariantsRequest;
import shared.Empty;

@Slf4j
@GrpcService
public class VariantService extends VariantServiceGrpc.VariantServiceImplBase {

    @Autowired
    private VariantServ variantServ;

    @Override
    public void getVariants(GetVariantsRequest request, io.grpc.stub.StreamObserver<VariantResponse> responseObserver) {
        try {
            List<VariantUserRes> variantList = variantServ.findByProductId(request.getProductId());
            for (VariantUserRes variant : variantList) {
                VariantResponse.Builder builder = VariantResponse.newBuilder()
                        .setId(variant.getId())
                        .setPrice(variant.getPrice())
                        .setSku(variant.getSku())
                        .setStatus(ToProto.toProtoStatus(variant.getStatus()))
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
}

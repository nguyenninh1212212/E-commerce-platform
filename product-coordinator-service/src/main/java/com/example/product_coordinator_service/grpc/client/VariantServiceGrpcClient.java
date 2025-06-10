package com.example.product_coordinator_service.grpc.client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.product_coordinator_service.model.dto.req.Attribute;
import com.example.product_coordinator_service.model.dto.req.Variant;
import com.example.product_coordinator_service.model.enums.Status;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import variant.VariantServiceGrpc.VariantServiceBlockingStub;
import variant.res;
import variant.CreateVariantsRequest;
import variant.VariantsRequest;

@Slf4j
@Component
@NoArgsConstructor
public class VariantServiceGrpcClient {
    @GrpcClient("variant-client")
    private VariantServiceBlockingStub blockingStub;

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

    public String CreateVariant(String productId, List<Variant> variantList) {
        try {
            List<VariantsRequest> reqList = new ArrayList<>();
            for (Variant variant : variantList) {
                List<shared.Attribute> attributes = variant.getAttributes()
                        .stream()
                        .map(attb -> shared.Attribute.newBuilder()
                                .setName(attb.getName())
                                .addAllValues(attb.getValue())
                                .build())
                        .collect(Collectors.toList());
                VariantsRequest req = VariantsRequest.newBuilder()
                        .setPrice(variant.getPrice())
                        .setProductId(productId)
                        .setQuantity(variant.getQuantity())
                        .setStatus(toProtoStatus(variant.getStatus()))
                        .addAllAttributes(attributes)
                        .build();
                reqList.add(req);
            }
            CreateVariantsRequest createVariantsRequest = CreateVariantsRequest.newBuilder().addAllVariants(reqList)
                    .build();
            res response = blockingStub.createVariants(createVariantsRequest);
            return response.getValue().toString();
        } catch (Exception e) {
            log.info("CreateVariant", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

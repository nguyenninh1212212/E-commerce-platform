package com.example.variant_service.service.kafka;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.shaded.com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.variant_service.mapper.ToModel;
import com.example.variant_service.model.dto.req.VariantReq;
import com.example.variant_service.model.dto.req.VariantUpdateReq;
import com.example.variant_service.service.VariantServ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import variant.CreateVariantsRequest;
import variant.GetVariantsRequest;
import variant.UpdateVariantsRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaCosumerVariant {
        private final VariantServ variantServ;

        private final String TOPIC_CREATE = "variant-create";
        private final String TOPIC_DELETE = "product-delete";
        private final String TOPIC_UPDATE = "variant-update";

        @KafkaListener(topics = TOPIC_CREATE)
        public void consumeCreateEvent(
                        ConsumerRecord<String, byte[]> record)
                        throws com.google.protobuf.InvalidProtocolBufferException {
                byte[] event = record.value();
                CreateVariantsRequest createVariantsRequest = CreateVariantsRequest.parseFrom(event);
                List<VariantReq> variantsList = createVariantsRequest.getVariantsList()
                                .stream()
                                .map(ToModel::toVariantsReq)
                                .toList();
                variantServ.createVariantList(variantsList, createVariantsRequest.getProductId());
                log.info("Created variants for product ID: {}", createVariantsRequest.getProductId());
        }

        @KafkaListener(topics = TOPIC_DELETE)
        public void consumeDeleteEvent(
                        ConsumerRecord<String, byte[]> record)
                        throws com.google.protobuf.InvalidProtocolBufferException {
                byte[] event = record.value();
                GetVariantsRequest request = GetVariantsRequest.parseFrom(event);
                variantServ.deleteVariantsByProductId(request.getProductId());
                log.info("Deleted variants for product ID: {}", request.getProductId());
        }

        @KafkaListener(topics = TOPIC_UPDATE)
        public void consumeUpdateEvent(
                        ConsumerRecord<String, byte[]> record)
                        throws com.google.protobuf.InvalidProtocolBufferException {
                byte[] event = record.value();
                UpdateVariantsRequest request = UpdateVariantsRequest.parseFrom(event);
                List<VariantUpdateReq> vList = request.getVariantsList().stream()
                                .map(v -> ToModel.toVariantUpdateReq(v)).toList();
                variantServ.updateVariantList(vList, request.getProductId());
                ;
                log.info("Deleted variants for product ID: {}", request.getProductId());
        }
}

package com.example.attribute_service.grpc.server;

import net.devh.boot.grpc.server.service.GrpcService;

import attribute.AttributeServiceGrpc;
import attribute.CategoryAttribute;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.attribute_service.service.AttributeServ;

import attribute.Attribute;
import attribute.Empty;
import io.grpc.stub.StreamObserver;

@GrpcService
public class AttributeService extends AttributeServiceGrpc.AttributeServiceImplBase {

   @Autowired
   private AttributeServ attributeServ;

   @Override
   public void getCategoryAttribute(Empty request,
         StreamObserver<CategoryAttribute> responseObserver) {
            
      CategoryAttribute response = CategoryAttribute.newBuilder()
            .addAttributes(Attribute.newBuilder().setName("Default Attribute").build())
            .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
   }
}

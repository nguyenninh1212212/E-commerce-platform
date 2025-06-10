package com.example.product_coordinator_service.model.dto.req;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attribute {
    private String name;
    private List<String> value;
}

package com.example.vendor_service.model.entity;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Vendor {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private String nameStore;
    private String email;
    private String userId;
    @Builder.Default
    private boolean isBan = false;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private PickUpAddress address;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tax_info_id")
    private TaxInfo taxInfo;
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendorShippingMethod> shippingMethods;
}

package com.example.address_service.model.dto.req;

import com.example.address_service.validation.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressReq {

    @NotBlank(groups = create.class, message = "Full name cannot be blank")
    private String fullName;

    @NotBlank(groups = create.class, message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{9,15}$", message = "Phone number must be digits only and 9-15 characters long")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(groups = create.class, message = "Country is required")
    private String country;

    @NotBlank(groups = create.class, message = "Province is required")
    private String province;

    @NotBlank(groups = create.class, message = "District is required")
    private String district;

    @NotBlank(groups = create.class, message = "Ward is required")
    private String ward;

    @NotBlank(groups = create.class, message = "Street is required")
    private String street;

    private String addressLine2;

    private String postalCode;

    private boolean isDefault;

    @NotBlank(groups = create.class, message = "Address type is required (e.g. HOME, WORK)")
    private String type;
}
    
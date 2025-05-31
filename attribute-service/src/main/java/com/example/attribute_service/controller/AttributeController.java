package com.example.attribute_service.controller;

import com.example.attribute_service.model.dto.res.Attribute;
import com.example.attribute_service.model.dto.res.CategoriesDTO;
import com.example.attribute_service.model.entity.CategoryAttribute;
import com.example.attribute_service.service.AttributeServ;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attributes")
@RequiredArgsConstructor
@Tag(name = "Attributes", description = "Quản lý danh mục và thuộc tính sản phẩm")
public class AttributeController {

    private final AttributeServ attributeServ;

    // --- CATEGORY ---
    @Operation(summary = "Thêm category mới")
    @PostMapping("/category")
    public ResponseEntity<String> addCategory(@RequestParam String category) {
        attributeServ.addCategory(category);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoriesDTO>> getCategories() {
        return ResponseEntity.ok(attributeServ.getCategories());
    }

    @GetMapping("/category/all")
    public ResponseEntity<List<CategoryAttribute>> getAllCategories() {
        return ResponseEntity.ok(attributeServ.getAllCategories());
    }

    @DeleteMapping("/category")
    public ResponseEntity<String> deleteCategory(@RequestParam String id) {
        attributeServ.deleteCategory(id);
        return ResponseEntity.ok("success");
    }

    // --- ATTRIBUTE ---

    @PostMapping("/{categoryId}/add")
    public ResponseEntity<String> addAttributes(
            @PathVariable String categoryId,
            @RequestBody List<Attribute> attributes) {
        attributeServ.addAttribute(categoryId, attributes);
        return ResponseEntity.ok("success");
    }

    @PutMapping("/{categoryId}/update")
    public ResponseEntity<String> updateAttribute(
            @PathVariable String categoryId,
            @RequestBody Attribute attribute) {
        attributeServ.updateAttribute(categoryId, attribute);
        return ResponseEntity.ok("success");
    }

    @PutMapping("/{categoryId}/update-value")
    public ResponseEntity<String> updateAttributeValue(
            @PathVariable String categoryId,
            @RequestBody Attribute attribute) {
        attributeServ.updateAttributeValue(categoryId, attribute);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/{categoryId}/delete")
    public ResponseEntity<String> deleteAttribute(
            @PathVariable String categoryId,
            @RequestParam String name) {
        attributeServ.deleteAttribute(categoryId, name);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/{categoryId}/delete-value")
    public ResponseEntity<String> deleteValueByAttribute(
            @PathVariable String categoryId,
            @RequestParam String name) {
        attributeServ.deleteValueByAttribute(categoryId, name);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/{categoryId}/delete-all")
    public ResponseEntity<String> deleteAllAttributes(
            @PathVariable String categoryId) {
        attributeServ.deleteAllAttributesByCategory(categoryId);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/{categoryId}/list")
    public ResponseEntity<List<Attribute>> getAttributesByCategoryId(
            @PathVariable String categoryId) {
        return ResponseEntity.ok(attributeServ.getAttributeByCategoryId(categoryId));
    }
}
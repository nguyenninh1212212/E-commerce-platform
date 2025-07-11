package com.example.attribute_service.controller;

import com.example.attribute_service.model.dto.Attribute;
import com.example.attribute_service.model.dto.res.CategoriesDTO;
import com.example.attribute_service.service.AttributeServ;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/attributes")
@RequiredArgsConstructor
public class AttributeController {

    private final AttributeServ attributeServ;

    @GetMapping("/category/all")
    public ResponseEntity<?> getCategoryAttributes() {

        return ResponseEntity.ok(attributeServ.getAllCategoryAttributes());
    }

    @PostMapping("/category")
    public ResponseEntity<String> addCategory(@RequestParam String category) {
        attributeServ.addCategory(category);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/category")
    
    public ResponseEntity<List<CategoriesDTO>> getCategories() {
        return ResponseEntity.ok(attributeServ.getCategories());
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
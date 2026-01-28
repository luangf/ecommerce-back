package com.talkovia.controllers;

import com.talkovia.dto.ProductRequestDTO;
import com.talkovia.dto.ProductResponseDTO;
import com.talkovia.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Save product")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> save(@Valid @RequestPart("data") ProductRequestDTO dto,
                                     @RequestParam("images") List<MultipartFile> images) {
        productService.saveProduct(dto, images);
        return ResponseEntity.status(CREATED).build();
    }

    @Operation(summary = "Get all products of a category")
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProductsOfCategory(@RequestParam String category) {
        return ResponseEntity.ok(productService.getCategoryProducts(category));
    }

    @Operation(summary = "Get products of the authenticated user")
    @GetMapping("/my")
    public ResponseEntity<List<ProductResponseDTO>> getMyProducts() {
        return ResponseEntity.ok(productService.getMyProducts());
    }

    @Operation(summary = "Get a product by an id")
    @GetMapping("/p/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
}

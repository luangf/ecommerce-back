package com.talkovia.dto;

import com.talkovia.model.ProductImage;
import com.talkovia.model.enums.Category;
import com.talkovia.model.enums.Condition;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponseDTO(
        Long id,
        String name,
        BigDecimal price,
        String brand,
        String description,
        String size,
        Condition condition,
        String conditionNotes,
        Category category,
        List<ProductImage> images
) {
}

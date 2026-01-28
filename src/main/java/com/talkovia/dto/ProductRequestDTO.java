package com.talkovia.dto;

import com.talkovia.model.enums.Category;
import com.talkovia.model.enums.Condition;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "name is mandatory")
        @Size(max = 100, message = "name must be at most {max} characters long")
        String name,

        @NotNull(message = "price is mandatory")
        @PositiveOrZero(message = "price must be positive or zero")
        @Digits(integer = 4, fraction = 2,
                message = "price must have up to {integer} integer digits and {fraction} decimal places")
        BigDecimal price,

        @Size(max = 50, message = "brand must be at most {max} characters long")
        String brand,

        @Size(max = 1000, message = "description must be at most {max} characters long")
        String description,

        @NotBlank(message = "size is mandatory")
        @Size(max = 30, message = "size must be at most {max} characters long")
        String size,

        @NotNull(message = "condition is mandatory")
        Condition condition,

        @Size(max = 400, message = "conditionNotes must be at most {max} characters long")
        String conditionNotes,

        @NotNull(message = "category is mandatory")
        Category category
        ) {
    }

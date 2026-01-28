package com.talkovia.mapper;

import com.talkovia.dto.ProductRequestDTO;
import com.talkovia.dto.ProductResponseDTO;
import com.talkovia.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequestDTO dto);
    List<ProductResponseDTO> toDTOList(List<Product> categoryProducts);
    ProductResponseDTO toDTO(Product product);
}

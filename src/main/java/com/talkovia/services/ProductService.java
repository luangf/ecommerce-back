package com.talkovia.services;

import com.talkovia.customexceptions.ObjectNotFoundException;
import com.talkovia.dto.ProductRequestDTO;
import com.talkovia.dto.ProductResponseDTO;
import com.talkovia.mapper.ProductMapper;
import com.talkovia.model.Product;
import com.talkovia.model.ProductImage;
import com.talkovia.model.User;
import com.talkovia.model.enums.Category;
import com.talkovia.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final LocalImageStorageService imageStorage;
    private final AuthenticatedUserService authenticatedUserService;

    private void validateImages(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("At least one image is required");
        }

        if (images.size() > 5) {
            throw new IllegalArgumentException("Maximum of 5 images allowed");
        }
    }

    public void saveProduct(ProductRequestDTO dto, List<MultipartFile> images) {
        validateImages(images);

        Product product = productMapper.toEntity(dto);

        User user = authenticatedUserService.getAuthenticatedUser();
        product.setUser(user);

        for (MultipartFile file : images) {
            String url = imageStorage.save(file);

            ProductImage image = new ProductImage();
            image.setUrl(url);
            image.setProduct(product);

            product.getImages().add(image);
        }
        productRepository.save(product);
    }

    public List<ProductResponseDTO> getCategoryProducts(String category) {
        Category enumCategory = Category.valueOf(category.toUpperCase());
        List<Product> products = productRepository.findByCategory(enumCategory);
        return productMapper.toDTOList(products);
    }

    public List<ProductResponseDTO> getMyProducts() {
        User user = authenticatedUserService.getAuthenticatedUser();
        List<Product> products = productRepository.findByUser(user);
        return productMapper.toDTOList(products);
    }

    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Item not found! ID: " + id + ", Type: " + Product.class.getName()));
        return productMapper.toDTO(product);
    }
}

package com.talkovia.repositories;

import com.talkovia.model.Product;
import com.talkovia.model.User;
import com.talkovia.model.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByUser(User user);
}

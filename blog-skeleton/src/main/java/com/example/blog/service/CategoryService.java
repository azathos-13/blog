package com.example.blog.service;

import com.example.blog.entity.Category;
import com.example.blog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // 获取所有分区（按排序值升序）
    public List<Category> getAllCategories() {
        return categoryRepository.findAllByOrderBySortOrderAsc();
    }

    // 根据ID获取单个分区
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // 保存或更新分区
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    // 删除分区
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
package com.example.blog.repository;

import com.example.blog.entity.Article;
import com.example.blog.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    
    // 根据分区查找文章（按时间倒序）
    List<Article> findByCategoryOrderByCreatedAtDesc(Category category);
    
    // 根据分区查找文章（分页）
    Page<Article> findByCategoryOrderByCreatedAtDesc(Category category, Pageable pageable);
    
    // 查找所有文章（按时间倒序）
    List<Article> findAllByOrderByCreatedAtDesc();
    
    // 查找所有文章（分页，按时间倒序）
    Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    // 搜索文章
    @Query("SELECT a FROM Article a WHERE a.title LIKE %:keyword% OR a.content LIKE %:keyword% ORDER BY a.createdAt DESC")
    List<Article> searchArticles(@Param("keyword") String keyword);
    
    // 统计分区下的文章数量
    long countByCategory(Category category); // 注意：应该是 long 不是 Long
}
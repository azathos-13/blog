package com.example.blog.service;

import com.example.blog.entity.Article;
import com.example.blog.entity.Category;
import com.example.blog.entity.User;
import com.example.blog.repository.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArticleService {
    
    private final ArticleRepository articleRepository;
    
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
    
    // 保存文章
    public Article saveArticle(Article article) {
        article.setUpdatedAt(LocalDateTime.now());
        return articleRepository.save(article);
    }
    
    // 创建新文章
    public Article createArticle(String title, String content, Category category, User creator) {
        Article article = new Article(title, content, category, creator);
        return articleRepository.save(article);
    }
    
    // 根据ID获取文章
    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }
    
    // 获取分区下的文章列表
    public List<Article> getArticlesByCategory(Category category) {
        return articleRepository.findByCategoryOrderByCreatedAtDesc(category);
    }
    
    // 获取分区下的文章列表（分页）
    public Page<Article> getArticlesByCategory(Category category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return articleRepository.findByCategoryOrderByCreatedAtDesc(category, pageable);
    }
    
    // 获取所有文章（分页）
    public Page<Article> getAllArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return articleRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
    
    // 搜索文章
    public List<Article> searchArticles(String keyword) {
        return articleRepository.searchArticles(keyword);
    }
    
    // 删除文章
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
    
    // 统计分区下的文章数量
    public long countArticlesByCategory(Category category) {
        return articleRepository.countByCategory(category);
    }
    
    // 更新文章
    public Article updateArticle(Long id, String title, String content, Category category) {
        return articleRepository.findById(id).map(article -> {
            article.setTitle(title);
            article.setContent(content);
            article.setCategory(category);
            article.setUpdatedAt(LocalDateTime.now());
            return articleRepository.save(article);
        }).orElse(null);
    }
}
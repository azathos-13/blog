package com.example.blog.controller;

import com.example.blog.entity.Article;
import com.example.blog.entity.Category;
import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    
    private final ArticleService articleService;
    private final CategoryService categoryService;
    
    public ArticleController(ArticleService articleService, CategoryService categoryService) {
        this.articleService = articleService;
        this.categoryService = categoryService;
    }
    
    /**
     * 显示文章列表（按分区）
     * GET /articles?categoryId=xxx
     */
    @GetMapping
    public String listArticles(@RequestParam(required = false) Long categoryId,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        boolean isAdmin = currentUser != null && "ADMIN".equals(currentUser.getRole());
        boolean isGuest = currentUser != null && "GUEST".equals(currentUser.getRole());
        
        model.addAttribute("user", currentUser);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isGuest", isGuest);
        model.addAttribute("categories", categoryService.getAllCategories());
        
        if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId).orElse(null);
            if (category != null) {
                Page<Article> articlePage = articleService.getArticlesByCategory(category, page, size);
                model.addAttribute("articles", articlePage.getContent());
                model.addAttribute("currentCategory", category);
                model.addAttribute("totalPages", articlePage.getTotalPages());
                model.addAttribute("currentPage", page);
            }
        } else {
            // 显示所有文章
            Page<Article> articlePage = articleService.getAllArticles(page, size);
            model.addAttribute("articles", articlePage.getContent());
            model.addAttribute("totalPages", articlePage.getTotalPages());
            model.addAttribute("currentPage", page);
        }
        
        
        
        
        return "articles/list";
    }
    
    /**
     * 显示写文章页面（嘉宾和管理员可用）
     * GET /articles/write
     */
    @GetMapping("/write")
    public String showWriteForm(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        
        // 检查权限：只有登录的嘉宾或管理员可以写文章
        if (currentUser == null || 
            (!"GUEST".equals(currentUser.getRole()) && !"ADMIN".equals(currentUser.getRole()))) {
            redirectAttributes.addFlashAttribute("message", "只有登录的嘉宾或管理员可以发表文章");
            return "redirect:/login";
        }
        
        model.addAttribute("user", currentUser);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("article", new Article());
        
        return "articles/write";
    }
    
    /**
     * 处理文章提交
     * POST /articles/write
     */
    @PostMapping("/write")
    public String submitArticle(@RequestParam String title,
                                @RequestParam String content,
                                @RequestParam Long categoryId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        
        // 再次检查权限
        if (currentUser == null || 
            (!"GUEST".equals(currentUser.getRole()) && !"ADMIN".equals(currentUser.getRole()))) {
            redirectAttributes.addFlashAttribute("message", "权限不足");
            return "redirect:/login";
        }
        
        Category category = categoryService.getCategoryById(categoryId).orElse(null);
        if (category == null) {
            redirectAttributes.addFlashAttribute("error", "所选分区不存在");
            return "redirect:/articles/write";
        }
        
        // 创建文章
        Article article = articleService.createArticle(title, content, category, currentUser);
        redirectAttributes.addFlashAttribute("success", "文章发表成功！");
        
        // 重定向到文章详情页
        return "redirect:/articles/" + article.getId();
    }
    
    /**
     * 显示文章详情
     * GET /articles/{id}
     */
    @GetMapping("/{id}")
    public String viewArticle(@PathVariable Long id, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        Article article = articleService.getArticleById(id).orElse(null);
        
        if (article == null) {
            return "redirect:/articles";
        }
        
        model.addAttribute("article", article);
        model.addAttribute("user", currentUser);
        model.addAttribute("isAdmin", currentUser != null && "ADMIN".equals(currentUser.getRole()));
        
        return "articles/view";
    }
    
    /**
     * 删除文章（仅管理员）
     * GET /articles/delete/{id}
     */
    @GetMapping("/delete/{id}")
    public String deleteArticle(@PathVariable Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        
        // 只有管理员可以删除文章
        if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
            redirectAttributes.addFlashAttribute("message", "只有管理员可以删除文章");
            return "redirect:/articles/" + id;
        }
        
        articleService.deleteArticle(id);
        redirectAttributes.addFlashAttribute("success", "文章删除成功");
        
        return "redirect:/articles";
    }
    
    /**
     * 搜索文章
     * GET /articles/search
     */
    @GetMapping("/search")
    public String searchArticles(@RequestParam String keyword, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        List<Article> articles = articleService.searchArticles(keyword);
        
        model.addAttribute("articles", articles);
        model.addAttribute("user", currentUser);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categories", categoryService.getAllCategories());
        
        return "articles/search";
    }
}
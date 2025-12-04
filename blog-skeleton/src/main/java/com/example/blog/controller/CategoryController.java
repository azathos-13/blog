package com.example.blog.controller;

import com.example.blog.entity.Category;
import com.example.blog.entity.User;
import com.example.blog.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categories") // 所有方法的URL都以 /categories 开头
public class CategoryController {

    private final CategoryService categoryService;

    // 构造器注入
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 1. 公开访问：显示所有分区列表（用于导航栏或发表文章时选择）
     * GET /categories
     */
    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories/list"; // 对应 templates/category/list.html
    }

    /**
     * 2. 管理员专用：显示分区管理页面（包含操作表单）
     * GET /categories/manage
     */
    @GetMapping("/manage")
    public String manageCategories(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // --- 手动权限检查：仅限管理员 ---
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
            redirectAttributes.addFlashAttribute("message", "权限不足，仅管理员可访问。");
            return "redirect:/"; // 非管理员重定向到首页
        }
        // --- 权限检查通过 ---

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("newCategory", new Category()); // 用于新增表单
        return "categories/manage"; // 对应 templates/category/manage.html
    }

    /**
     * 3. 管理员专用：处理新增分区的表单提交
     * POST /categories
     */
    @PostMapping
    public String createCategory(@ModelAttribute Category newCategory,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        // --- 手动权限检查：仅限管理员 ---
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
            redirectAttributes.addFlashAttribute("message", "权限不足，操作被拒绝。");
            return "redirect:/categories/manage";
        }
        // --- 权限检查通过 ---

        categoryService.saveCategory(newCategory);
        redirectAttributes.addFlashAttribute("successMessage", "分区创建成功！");
        return "redirect:/categories/manage";
    }

    /**
     * 4. 管理员专用：处理删除分区的请求
     * GET /categories/delete/{id}
     * 注意：通常删除用 DELETE 方法，这里为了简化使用 GET
     */
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        // --- 手动权限检查：仅限管理员 ---
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
            redirectAttributes.addFlashAttribute("message", "权限不足，操作被拒绝。");
            return "redirect:/categories/manage";
        }
        // --- 权限检查通过 ---

        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("successMessage", "分区删除成功！");
        return "redirect:/categories/manage";
    }
}
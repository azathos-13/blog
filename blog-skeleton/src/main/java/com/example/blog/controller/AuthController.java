package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Authentication controller: login, logout, and home page.
 *
 * Authorization approach (explicit, manual):
 * - After successful login, we put the whole User object into HttpSession:
 *      session.setAttribute("user", user);
 * - In controller methods we check:
 *      User user = (User) session.getAttribute("user");
 *      if (user == null) -> visitor (not logged in)
 *      else -> use user.getRole() to decide permissions
 *
 * This is intentionally simple to be readable for beginners.
 */
@Controller
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Home page: shows current user info (if any)
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        // retrieve the User object from session (may be null)
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("role", user.getRole());
        } else {
            model.addAttribute("username", "Visitor");
            model.addAttribute("role", "NONE");
        }
        return "index";
    }

    // show login form
    @GetMapping("/login")
    public String loginForm(HttpSession session, Model model) {
        // If already logged in, redirect to home
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/";
        }
        model.addAttribute("error", "");
        return "login";
    }

    // perform login
    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username,
                              @RequestParam String password,
                              HttpServletRequest request,
                              Model model) {
        Optional<User> uOpt = userRepository.findByUsername(username);
        if (uOpt.isEmpty()) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        User user = uOpt.get();
        // plain-text password comparison (for learning only)
        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        // Successful login: store full User object in session
        HttpSession session = request.getSession(true); // create if absent
        session.setAttribute("user", user);

        // Redirect to home page
        return "redirect:/";
    }

    // logout: destroy session
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Invalidate the whole session to remove the stored user object
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

    // Example of a protected admin-only page (optional)
    // Demonstrates manual role check inside controller method.
    @GetMapping("/admin-only")
    public String adminOnly(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            // not allowed -> simple text response or redirect
            model.addAttribute("message", "Access denied. Admins only.");
            return "access-denied";
        }
        model.addAttribute("username", user.getUsername());
        return "admin";
    }
}

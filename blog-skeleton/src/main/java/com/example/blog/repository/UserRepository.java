package com.example.blog.repository;

import com.example.blog.entity.User; // 注意：import 的是 entity.User，不是 model.User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; // 确保这行存在

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 关键：这个方法必须返回 Optional<User>，以匹配Controller中的调用
    Optional<User> findByUsername(String username);
}
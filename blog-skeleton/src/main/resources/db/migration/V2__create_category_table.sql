-- V2__create_category_table.sql
-- 创建文章分区表
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分区唯一ID',
    name VARCHAR(100) NOT NULL COMMENT '分区名称',
    description TEXT COMMENT '分区描述',
    sort_order INT DEFAULT 0 COMMENT '排序值（数字越小越靠前）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='文章分区表';

-- 插入一些初始示例数据
INSERT INTO category (name, description, sort_order) VALUES
('技术笔记', '关于编程、框架和开发工具的学习记录', 1),
('生活随笔', '记录日常的思考与见闻', 2),
('读书心得', '阅读之后的总结与感想', 3);
-- 创建文章表
CREATE TABLE IF NOT EXISTS article (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '文章标题',
    content TEXT NOT NULL COMMENT '文章内容',
    category_id BIGINT NOT NULL COMMENT '分区ID',
    creator_id BIGINT NOT NULL COMMENT '创建者ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
    FOREIGN KEY (creator_id) REFERENCES blog_user(id) ON DELETE CASCADE
);

-- 为文章表添加索引
CREATE INDEX idx_article_category ON article(category_id);
CREATE INDEX idx_article_creator ON article(creator_id);
CREATE INDEX idx_article_created ON article(created_at);
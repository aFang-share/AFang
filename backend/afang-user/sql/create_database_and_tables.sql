-- ========================================
-- AFang用户后端系统数据库初始化脚本
-- ========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS afang
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE afang;

-- ========================================
-- 用户表 (users)
-- ========================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID，主键自增',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    email VARCHAR(100) UNIQUE COMMENT '邮箱',
    phone VARCHAR(20) UNIQUE COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    user_role VARCHAR(20) DEFAULT 'user' COMMENT '用户角色（user/admin等）',
    status VARCHAR(20) DEFAULT 'active' COMMENT '用户状态（active/inactive/banned等）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    -- 索引
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_user_role (user_role),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ========================================
-- 可选：插入初始数据
-- ========================================

-- 插入管理员用户（可选，密码需要使用BCrypt加密）
-- 注意：这里的密码需要使用应用程序中相同的加密算法进行加密
-- 默认密码：admin123（需要先使用BCrypt加密）
-- INSERT INTO users (username, password, email, user_role, status)
-- VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'admin@example.com', 'admin', 'active');

-- 插入测试用户（可选）
-- INSERT INTO users (username, password, email, user_role, status)
-- VALUES ('test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'test@example.com', 'user', 'active');

-- ========================================
-- 数据库版本信息
-- ========================================
-- 创建版本记录表（可选，用于数据库版本管理）
CREATE TABLE IF NOT EXISTS schema_version (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version VARCHAR(20) NOT NULL COMMENT '版本号',
    description VARCHAR(255) COMMENT '版本描述',
    script_name VARCHAR(100) COMMENT '脚本名称',
    executed_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
    INDEX idx_version (version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据库版本记录表';

-- 记录当前版本
INSERT IGNORE INTO schema_version (version, description, script_name)
VALUES ('1.0.0', '初始化用户表结构', 'create_database_and_tables.sql');

-- ========================================
-- 使用说明
-- ========================================
/*
1. 执行此脚本前请确保：
   - MySQL服务已启动
   - 具有创建数据库的权限
   - 配置文件中的数据库连接信息正确

2. 密码加密说明：
   - 应用程序使用BCrypt算法加密密码
   - 如需手动插入用户，请先使用相同的加密算法处理密码
   - 示例密码"admin123"的BCrypt哈希值：$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa

3. 字段说明：
   - user_role: 用户角色，可选值：user（普通用户）、admin（管理员）
   - status: 用户状态，可选值：active（活跃）、inactive（非活跃）、banned（封禁）

4. 索引优化：
   - 已为常用查询字段添加索引
   - 可根据实际查询需求调整索引策略

5. 字符集：
   - 使用utf8mb4字符集，支持完整的Unicode字符集
   - 包括emoji等特殊字符
*/
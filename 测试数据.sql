-- 测试用户数据
-- 用户名: admin, 密码: 123456 (MD5加密后: e10adc3949ba59abbe56e057f20f883e)

INSERT INTO users (username, phone, password, nickname, avatar, gender, birthday, points, vip_level, status, create_time, update_time) 
VALUES ('admin', '13800138000', 'e10adc3949ba59abbe56e057f20f883e', '管理员', null, 1, '1990-01-01', 1000, 3, 1, NOW(), NOW());

-- 普通测试用户
INSERT INTO users (username, phone, password, nickname, avatar, gender, birthday, points, vip_level, status, create_time, update_time) 
VALUES ('testuser', '13800138001', 'e10adc3949ba59abbe56e057f20f883e', '测试用户', null, 0, '1995-05-15', 100, 1, 1, NOW(), NOW());

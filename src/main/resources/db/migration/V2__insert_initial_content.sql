-- 网站刚建好时的初始内容。之后都可以在管理后台里修改，不需要改这个文件。
-- 邮箱等联系方式不写在这里（代码会公开），请在管理后台的「个人信息」里填写。

INSERT INTO profile (id, name, intro, about_md)
VALUES (
    1,
    '曾进',
    '正在学 Java 和 Spring Boot。学到的东西、做过的东西、喜欢的东西，都会慢慢记在这里。',
    '这里还没有写。'
);

INSERT INTO now_entries (body_md)
VALUES (
'- 在学：Java 和 Spring Boot
- 在做：这个网站'
);

INSERT INTO posts (slug, title, summary, body_md, status, published_at)
VALUES (
    'hello',
    '网站开张了',
    '这个网站的第一篇文章。',
'这个网站从今天开始运行。

它用 Spring Boot 搭建，文章存在数据库里，用 Markdown 来写。以后学到的东西、做过的东西，都会记在这里。',
    'PUBLISHED',
    now()
);

INSERT INTO works (slug, title, summary, body_md, year)
VALUES (
    'this-site',
    '这个网站',
    '用 Spring Boot、MyBatis 和 PostgreSQL 搭建的个人网站。',
'## 用到的技术

- Java 25、Spring Boot 4
- Thymeleaf：在服务器端生成页面
- MyBatis、PostgreSQL、Flyway：保存和管理数据
- Spring Security：管理后台的登录
- Docker、GitHub Actions：打包和自动测试

页面没有使用前端框架，只有 HTML 和 CSS。',
    2026
);

INSERT INTO journey_entries (year, month, title)
VALUES (2026, 11, '开始做这个网站');

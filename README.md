# personal-site

曾进的个人网站。用来记录学到的东西、做过的东西和喜欢的东西，打算一直维护下去。

目前只有中文版，以后会加上日文版和英文版。

## 有什么

**公开页面**

| 网址 | 内容 |
|---|---|
| `/` | 首页：自我介绍、近况、最近的文章、作品、历程、喜欢的东西 |
| `/posts` | 文章，按年份排列。正文用 Markdown 写 |
| `/works` | 作品 |
| `/now` | 近况，以前的近况会留作存档 |
| `/journey` | 历程：人生时间线 |
| `/likes` | 喜欢的东西 |
| `/about` | 关于 |
| `/feed.xml` | 文章的订阅源（Atom 格式） |

**管理后台**（`/admin`，需要登录）

在浏览器里写文章、更新近况，管理作品、历程、喜欢的东西，以及修改名字、自我介绍和联系方式。改内容不需要改代码。

## 技术栈

| 分类 | 技术 |
|---|---|
| 语言 | Java 25 |
| 框架 | Spring Boot 4.0（Spring MVC、Spring Security、Bean Validation） |
| 页面 | Thymeleaf、HTML、CSS（没有使用前端框架） |
| 数据库 | PostgreSQL 17、MyBatis、Flyway |
| Markdown | commonmark-java |
| 构建 | Maven（Maven Wrapper） |
| 测试 | JUnit 5、Mockito、MockMvc、Testcontainers |
| 运行环境 | Docker、Docker Compose、GitHub Actions |

## 在自己的电脑上运行

需要先装好 **Java 25** 和 **Docker Desktop**，并且让 Docker Desktop 保持运行。Maven 不用装。

```bash
./mvnw spring-boot:run
```

启动时会自动用 Docker 启动数据库，并执行建表脚本。启动后打开 <http://localhost:8080>。

- 管理后台：<http://localhost:8080/admin>
- 本地的登录账号写在 `src/main/resources/application-local.yml` 里，只在自己电脑上有效
- 数据库在 `localhost:15432`，账号见 `compose.yaml`，可以用 IntelliJ 的数据库工具连接查看

网站停止后，数据库会继续在 Docker 里运行，数据也会保留。想停掉数据库时：

```bash
docker compose stop
```

## 运行测试

```bash
./mvnw test
```

测试时会用 Docker 另外启动一个临时的 PostgreSQL，结束后自动删除，不会影响上面的开发用数据库。

## 放头像

把照片存成 `src/main/resources/static/images/avatar.jpg`（建议正方形，边长 300 像素左右），重新启动网站后，首页就会显示照片。没有照片时显示名字的第一个字。

## 代码结构

```
src/main/java/com/jince/site
├── PersonalSiteApplication.java   启动类
├── config        设置：登录和权限、配置项
├── controller    接收请求，决定显示哪个页面
│   └── admin     管理后台的页面
├── service       业务逻辑
├── mapper        数据库读写的接口（MyBatis）
├── entity        和数据库表一一对应的类
├── form          管理后台表单的输入内容和校验规则
├── dto           页面显示用的数据
├── exception     自定义的异常
└── support       辅助类（日期显示）

src/main/resources
├── application.yml          所有环境共用的设置
├── application-local.yml    本地开发专用
├── application-prod.yml     线上专用
├── messages.properties      公开页面上的固定文字
├── db/migration             建表脚本（Flyway）
├── mapper                   SQL（MyBatis 的 XML）
├── templates                页面模板（Thymeleaf）
└── static                   CSS、图标、图片
```

一次请求的流程：`Controller → Service → Mapper → 数据库`，再由 Controller 把数据交给模板生成页面。

## 修改表结构

不要改 `db/migration` 里已经执行过的脚本。需要改表时，新建下一个编号的脚本，例如 `V3__add_xxx.sql`，网站启动时会自动执行。

## 上线

用 `Dockerfile` 打包成镜像：

```bash
docker build -t personal-site .
```

运行时通过环境变量传入设置：

| 环境变量 | 内容 |
|---|---|
| `SPRING_PROFILES_ACTIVE` | 镜像里已经设为 `prod` |
| `DATABASE_URL` | 例：`jdbc:postgresql://主机名:5432/数据库名?sslmode=require` |
| `DATABASE_USERNAME` | 数据库用户名 |
| `DATABASE_PASSWORD` | 数据库密码 |
| `ADMIN_USERNAME` | 管理后台的用户名 |
| `ADMIN_PASSWORD` | 管理后台的密码，请用足够长的随机密码 |
| `SITE_BASE_URL` | 网站的正式地址，例：`https://example.dev` |
| `PORT` | 监听的端口，不设时为 8080 |

健康检查地址是 `/actuator/health`。

## 自动测试

代码推送到 GitHub 的 `main` 分支或发起 Pull Request 时，GitHub Actions 会自动运行全部测试，并确认 Docker 镜像能打包成功。设置在 `.github/workflows/ci.yml`。

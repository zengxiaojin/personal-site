# 把网站打包成 Docker 镜像。分两个阶段，最终的镜像里只有运行所需的东西。

# ---------- 第一阶段：编译并打包 ----------
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# 先只复制依赖配置并下载依赖。
# 以后只改代码时，这一层可以直接用缓存，构建会快很多。
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN sh ./mvnw -B -q dependency:go-offline

COPY src/ src/
# 测试在 GitHub Actions 里运行，这里只打包
RUN sh ./mvnw -B -q package -DskipTests

# ---------- 第二阶段：运行 ----------
FROM eclipse-temurin:25-jre
WORKDIR /app

# 不使用 root 用户运行，更安全
RUN useradd --system --no-create-home app
USER app

COPY --from=build /app/target/*.jar app.jar

# 线上环境读取 application-prod.yml，数据库和管理员密码来自环境变量
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

# 小型服务器内存有限，让 Java 最多使用其中的 75%
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]

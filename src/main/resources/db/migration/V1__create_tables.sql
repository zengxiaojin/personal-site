-- 建表。表结构以后有变化时，新建 V3、V4…… 脚本，不要修改已经执行过的脚本。

-- 个人信息：整张表只有一行（id 固定为 1）
CREATE TABLE profile (
    id         INTEGER      PRIMARY KEY CHECK (id = 1),
    name       VARCHAR(50)  NOT NULL,
    intro      TEXT         NOT NULL,
    about_md   TEXT         NOT NULL DEFAULT '',
    email      VARCHAR(255),
    github_url VARCHAR(255),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-- 文章
CREATE TABLE posts (
    id           BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    -- 网址里使用的名字，例如 /posts/hello。发布后不要再改
    slug         VARCHAR(100) NOT NULL UNIQUE,
    title        VARCHAR(200) NOT NULL,
    summary      VARCHAR(500),
    body_md      TEXT         NOT NULL,
    status       VARCHAR(20)  NOT NULL DEFAULT 'DRAFT' CHECK (status IN ('DRAFT', 'PUBLISHED')),
    published_at TIMESTAMPTZ,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_posts_status_published_at ON posts (status, published_at DESC);

-- 近况：每次更新新增一行，旧的留作存档
CREATE TABLE now_entries (
    id         BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    body_md    TEXT        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 作品
CREATE TABLE works (
    id         BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    slug       VARCHAR(100) NOT NULL UNIQUE,
    title      VARCHAR(200) NOT NULL,
    summary    VARCHAR(300) NOT NULL,
    body_md    TEXT         NOT NULL DEFAULT '',
    site_url   VARCHAR(500),
    repo_url   VARCHAR(500),
    year       INTEGER      NOT NULL,
    sort_order INTEGER      NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-- 历程：人生时间线
CREATE TABLE journey_entries (
    id          BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    year        INTEGER      NOT NULL,
    month       INTEGER      CHECK (month BETWEEN 1 AND 12),
    title       VARCHAR(200) NOT NULL,
    description TEXT,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-- 喜欢的东西
CREATE TABLE like_items (
    id         BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category   VARCHAR(50)  NOT NULL,
    title      VARCHAR(200) NOT NULL,
    note       VARCHAR(500),
    url        VARCHAR(500),
    sort_order INTEGER      NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT now()
);

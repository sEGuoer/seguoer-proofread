# --- schema ---
CREATE TABLE `collection`
(
    `id`                bigint unsigned NOT NULL AUTO_INCREMENT,
    `title`             varchar(255) NOT NULL,
    `title_translation` varchar(255)   DEFAULT NULL COMMENT '翻译后的标题 [proofread-specific]',
    `slug`              varchar(255)   DEFAULT NULL,
    `type`              char(20) NOT NULL DEFAULT 'doc' COMMENT 'doc|video',
    `content`           text,
    `video`             varchar(255)   DEFAULT NULL COMMENT 'video link',
    `duration`          int unsigned NOT NULL DEFAULT '0',
    `cover`             varchar(255)   DEFAULT NULL,
    `description`       varchar(512)   DEFAULT NULL,
    `published`      tinyint(1) NOT NULL DEFAULT '1',
    `free`           tinyint(1) NOT NULL DEFAULT '0',
    `price`             decimal(10, 2) DEFAULT NULL,
    `view_count`        int unsigned NOT NULL DEFAULT '0',
    `seo_title`         varchar(255)   DEFAULT NULL,
    `seo_description`   varchar(255)   DEFAULT NULL,
    `completed`      tinyint(1) NOT NULL DEFAULT '1',
    `proofread`        tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已经校对过 [proofread-specific]',
    `user_id`           bigint unsigned NOT NULL,
    `created_at`        timestamp NULL DEFAULT NULL,
    `updated_at`        timestamp NULL DEFAULT NULL,
    `deleted_at`        timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `collection_slug_unique` (`slug`)
);


# --- data ---
INSERT INTO `collection` (`title`, `title_translation`, `slug`, `type`, `content`, `video`, `duration`, `cover`, `description`, `published`, `free`, `price`, `view_count`, `seo_title`, `seo_description`, `completed`, `proofread`, `user_id`, `deleted_at`, `created_at`, `updated_at`)
VALUES
    ('Docker 快速入门文档', NULL, 'docker-get-started__doc', 'doc', NULL, NULL, 0, 'https://bianxuebianzuo.oss-cn-shenzhen.aliyuncs.com/uploads/cover/1/xQkug4WdwX1628302147.png', 'Docker 官方入门文档不完全翻译。介绍了什么是容器、什么是镜像、以及如何创建镜像、运行容器、构建镜像最佳实践等 Docker 基础知识。', 1, 1, 0.00, 0, NULL, NULL, 1, 0, 1, NULL, now(), now()),
    ('Doloribus quam iusto ipsa omnis.', NULL, 'dolorem-officia-dolor-qui', 'doc', NULL, NULL, 0, '/img/cover/doc-default-cover.png', 'Eos dignissimos aut autem deserunt. Tempore nostrum quasi nostrum provident et eum. Sapiente ut aperiam quo rerum modi consequatur. Id velit quos in sed.', 1, 0, 128.00, 0, 'Libero.', 'Vitae est amet autem fuga. Qui quasi eos cupiditate repellat. Sunt fugit quis provident et aperiam.', 1, 0, 1, NULL, now(), now()),
    ('Qui possimus repudiandae dolores ea cum eos.', NULL, 'corporis-quis-quae-consequatur-rerum-quae-omnis-deleniti', 'doc', NULL, NULL, 0, '/img/cover/doc-default-cover.png', 'Facilis sapiente qui dolor occaecati aut reiciendis esse vitae. Nobis sunt nulla sint aut non et.', 1, 0, 20.00, 0, 'Similique.', 'Ea aut optio aliquid non. Et optio doloribus rerum. Facere cumque minima ut dolor id cumque. Ut alias quisquam expedita quos.', 1, 0, 1, NULL, now(), now()),
    ('Et enim autem commodi quia qui.', NULL, 'eos-reiciendis-reiciendis-qui-accusantium-doloremque-magni', 'doc', NULL, NULL, 0, '/img/cover/doc-default-cover.png', 'Suscipit omnis praesentium beatae ratione. Vel dolore earum sint veritatis. Quia nam quae harum unde est et.', 1, 0, 128.00, 0, 'Veritatis.', 'Et quidem ratione in et dolores. Vitae rem quis architecto. Quis corrupti tempora repellendus fugit numquam qui incidunt quae.', 1, 0, 1, NULL, now(), now()),
    ('Minus sed corporis distinctio.', NULL, 'occaecati-consequatur-sunt-iste-nam-suscipit', 'doc', NULL, NULL, 0, '/img/cover/doc-default-cover.png', 'Facere ipsam corrupti et ut laborum. Ut autem amet consequuntur voluptas. Animi asperiores modi velit qui aut ratione molestias quibusdam. Dignissimos ut quas veniam blanditiis maiores esse.', 1, 0, 155.00, 0, 'Et minus.', 'Libero sapiente cum quia magnam deserunt. Ipsa odio eaque nobis quaerat.', 1, 0, 1, NULL, now(), now()),
    ('Voluptatum autem animi amet et non nobis.', NULL, 'qui-distinctio-ut-dolor-eos', 'doc', NULL, NULL, 0, '/img/cover/doc-default-cover.png', 'Perferendis reprehenderit reiciendis occaecati nemo quia fuga facere debitis. Et sit ipsam nostrum quam provident. Pariatur officia expedita fugiat non debitis.', 1, 1, 0.00, 0, 'Voluptate.', 'Voluptatem odio quo et sint et qui rerum vitae. Laborum animi aut ab atque unde. Eos maxime voluptas ratione eum.', 0, 0, 1, NULL, now(), now()),
    ('Facere necessitatibus deleniti est similique.', NULL, 'repellendus-adipisci-nihil-ullam-quibusdam-quisquam-ullam', 'doc', NULL, NULL, 0, '/img/cover/doc-default-cover.png', 'Molestiae ducimus aut possimus expedita. Dignissimos quos optio error explicabo. Est laudantium asperiores aperiam error. Omnis alias aspernatur ad molestiae nihil neque eum veniam.', 1, 0, 167.00, 0, 'Quod.', 'Ipsa doloremque et repudiandae corporis. Quasi libero eveniet facilis labore et nesciunt. Inventore dolor ut harum.', 0, 0, 1, NULL, now(), now());

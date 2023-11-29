CREATE TABLE `lecture`
(
    `id`                      bigint unsigned NOT NULL AUTO_INCREMENT,
    `title`                   varchar(255) NOT NULL,
    `title_translation`       varchar(255)          DEFAULT NULL,
    `slug`                    varchar(255)          DEFAULT NULL,
    `content`                 text,
    `video`                   varchar(255)          DEFAULT NULL,
    `video_id`                varchar(255)          DEFAULT NULL,
    `duration`                int unsigned NOT NULL DEFAULT '0',
    `description`             text,
    `description_translation` text,
    `sort_order`              int          NOT NULL DEFAULT '0',
    `published`            tinyint(1) NOT NULL DEFAULT '1',
    `free`                 tinyint(1) NOT NULL DEFAULT '0',
    `requires_login`          tinyint(1) NOT NULL DEFAULT '0',
    `cover`                   varchar(255)          DEFAULT NULL,
    `section_id`              bigint unsigned NOT NULL,
    `collection_id`           bigint unsigned NOT NULL,
    `created_at`              timestamp NULL DEFAULT NULL,
    `updated_at`              timestamp NULL DEFAULT NULL,
    `deleted_at`              timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `lectures_slug_unique` (`slug`)
);
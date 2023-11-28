CREATE TABLE `section`
(
    `id`                bigint unsigned NOT NULL AUTO_INCREMENT,
    `slug`              varchar(255)          DEFAULT NULL,
    `title`             varchar(255) NOT NULL,
    `title_translation` varchar(255)          DEFAULT NULL,
    `description`       varchar(512)          DEFAULT NULL,
    `sort_order`        int          NOT NULL DEFAULT '0',
    `collection_id`     bigint unsigned NOT NULL,
    `created_at`        timestamp NULL DEFAULT NULL,
    `updated_at`        timestamp NULL DEFAULT NULL,
    `deleted_at`        timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `sections_slug_unique` (`slug`)
);
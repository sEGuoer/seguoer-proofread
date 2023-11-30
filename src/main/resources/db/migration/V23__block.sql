CREATE TABLE `block`
(
    `id`                  bigint unsigned NOT NULL AUTO_INCREMENT,
    `content`             text NOT NULL,
    `content_translation` text,
    `sort_order`              int          NOT NULL DEFAULT '0',
    `published`      tinyint(1) NOT NULL DEFAULT '1',
    `lecture_id`          bigint unsigned NOT NULL,
    `collection_id`          bigint unsigned NOT NULL,
    `created_at`          timestamp NULL DEFAULT NULL,
    `updated_at`          timestamp NULL DEFAULT NULL,
    `deleted_at`          timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE `forum`.`question` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(50) NOT NULL,
  `description` TEXT NOT NULL,
  `gmt_create` BIGINT NOT NULL,
  `gmt_modified` BIGINT NOT NULL,
  `creator` INT NOT NULL,
  `comment_count` INT NOT NULL DEFAULT 0,
  `view_count` INT NOT NULL DEFAULT 0,
  `like_count` INT NOT NULL DEFAULT 0,
  `tag` VARCHAR(256) NULL DEFAULT 0,
  PRIMARY KEY (`id`));

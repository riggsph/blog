DROP DATABASE IF EXISTS cmsblog_test;

CREATE DATABASE cmsblog_test;

USE cmsblog_test;

CREATE TABLE `user` (
	`id` INT NOT NULL auto_increment,
    `username` VARCHAR(30) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `enabled` boolean not null,
    PRIMARY KEY(`id`)
) ENGINE = InnoDB;

CREATE TABLE `role` (
	`id` INT NOT NULL auto_increment,
	`role` varchar(30) not null,
    PRIMARY KEY(`id`)
) ENGINE = InnoDB;

create table `user_role`(
	`user_id` int not null,
	`role_id` int not null,
	primary key(`user_id`,`role_id`),
	foreign key (`user_id`) references `user`(`id`),
	foreign key (`role_id`) references `role`(`id`));

CREATE TABLE `approval` (
	`id` TINYINT NOT NULL auto_increment,
    `description` VARCHAR(50) NOT NULL,
    PRIMARY KEY(`id`)
) ENGINE = InnoDB;

CREATE TABLE `article` (
	`articleId` INT NOT NULL auto_increment,
    `title` VARCHAR(100) NOT NULL,
    `content` VARCHAR(2000) NOT NULL,
    postDate DATE NOT NULL,
    expirationDate DATE NULL,
    approvalId TINYINT NOT NULL,
    imageLink VARCHAR(200) NULL,
    userId INT NOT NULL,
    PRIMARY KEY(`articleId`),
    FOREIGN KEY(approvalId) REFERENCES `approval`(`id`),
    FOREIGN KEY(userId) REFERENCES `user`(`id`)
) ENGINE = InnoDB;

CREATE TABLE `page` (
	`pageId` INT NOT NULL auto_increment,
    `title` VARCHAR(100) NOT NULL,
    `content` VARCHAR(2000) NOT NULL,
    postDate DATE NOT NULL,
    expirationDate DATE NULL,
    userId INT NOT NULL,
    PRIMARY KEY(`pageId`),
    FOREIGN KEY(userId) REFERENCES `user`(`id`)
) ENGINE = InnoDB;

CREATE TABLE `category` (
	`categoryId` INT NOT NULL auto_increment,
    `description` VARCHAR(25) NOT NULL,
    PRIMARY KEY(`categoryId`)
) ENGINE = InnoDB;

CREATE TABLE article_category (
    articleId INT NOT NULL,
    categoryId INT NOT NULL,
    FOREIGN KEY(articleId) REFERENCES `article`(`articleId`),
    FOREIGN KEY(categoryId) REFERENCES `category`(`categoryId`)
) ENGINE = InnoDB;
        
insert into `role`(`id`,`role`)
    values(1, "ROLE_ADMIN"), (2, "ROLE_EDITOR"), (3, "ROLE_USER");
    
INSERT INTO `approval` (`description`)
	values ("Pending Approval"), ("Approved");

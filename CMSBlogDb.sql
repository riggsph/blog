DROP DATABASE IF EXISTS cmsblog;

CREATE DATABASE cmsblog;

USE cmsblog;

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

insert into `user`(`id`,`username`,`password`,`enabled`)
    values(1,"admin", "$2a$10$9eQMsFqMWF/zWgLebGd8BeHxVinLmmRqCrO/L625z.gASSTJFldCS", true),
        (2,"editor","$2a$10$9eQMsFqMWF/zWgLebGd8BeHxVinLmmRqCrO/L625z.gASSTJFldCS", true),
        (3,"user","$2a$10$9eQMsFqMWF/zWgLebGd8BeHxVinLmmRqCrO/L625z.gASSTJFldCS", true);
        
insert into `role`(`id`,`role`)
    values(1, "ROLE_ADMIN"), (2, "ROLE_EDITOR"), (3, "ROLE_USER");
    
insert into `user_role`(`user_id`,`role_id`)
    values(1,1),(1,2),(1,3),(2,2),(2,3),(3,3);
    
INSERT INTO `approval` (`description`)
	values ("Pending Approval"), ("Approved");

INSERT INTO `article` (`articleId`, `title`, `content`, postDate, expirationDate, approvalId, imageLink, userId)
	values (1, "Test Article #1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi pharetra hendrerit turpis sit amet pulvinar. Praesent eget dapibus odio, quis consectetur tellus. Phasellus lacinia, ligula vitae aliquet auctor, neque lectus auctor sapien, at volutpat arcu massa sed ipsum. Phasellus lacinia rutrum vulputate. Nam ac nibh et magna varius ullamcorper. Aenean a bibendum nulla. Sed fermentum lorem sit amet dolor feugiat, quis lacinia nisl rutrum. Mauris elementum sapien venenatis vestibulum dapibus.", "2021/11/01", "2022/10/01", 2, "https://cdn.pixabay.com/photo/2018/03/08/01/04/cuba-3207576_1280.jpg", 1),
	(2, "Test Article #2", "Phasellus at erat leo. Suspendisse ex dolor, pharetra sit amet scelerisque nec, dignissim at enim. Mauris rutrum eu justo sed hendrerit. In odio mi, ultrices id ex sed, auctor laoreet velit. Etiam vel imperdiet sapien. Aenean hendrerit est in eros ultricies consequat. Praesent luctus vitae justo sed vehicula. Sed nibh nibh, interdum eget aliquam luctus, tincidunt in turpis. Etiam nulla diam, gravida non ante vitae, hendrerit luctus dolor. In viverra consequat semper. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Suspendisse suscipit, risus sed molestie commodo, diam diam porta augue, vitae aliquam turpis metus vel risus.", "2021/11/04", null, 2, "https://cdn.pixabay.com/photo/2018/03/01/05/19/car-3189771_1280.jpg", 1);

INSERT INTO `category` (`categoryId`,`description`)
	values (1, "#test"), (2, "#lorem"), (3, "#ipsum");

INSERT INTO article_category (articleId, categoryId)
	values (1, 1), (1, 2), (1, 3), (2, 1), (2, 3);

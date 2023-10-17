--
-- build my database
--
-- created by YXH_XianYu on 2023.10
--

CREATE DATABASE peer_learning_system;

USE peer_learning_system;

Create TABLE user
(
    `uuid` VARCHAR(64),
    `username` VARCHAR(64),
    `password` VARCHAR(64),
    `email` VARCHAR(64),
    `authority` INT,

    PRIMARY KEY (`uuid`)
);

Create TABLE problem
(
    `uuid` VARCHAR(64),
    `name` VARCHAR(64),
    `content` TEXT,
    `standardAnswer` TEXT,

    PRIMARY KEY (`uuid`)
);

CREATE TABLE grouphomework
(
    `uuid` VARCHAR(64),
    `name` VARCHAR(64),
    `problemUUID` VARCHAR(64),
    `state` INT,
    `submitDeadline` DATE,
    `ratingDeadline` DATE,

    PRIMARY KEY (`uuid`),
    FOREIGN KEY (`problemUUID`) REFERENCES problem(`uuid`)
);

CREATE TABLE homework
(
    `uuid` VARCHAR(64),
    `groupHomeworkUUID` VARCHAR(64),
    `userUUID` VARCHAR(64),
    `answer` TEXT,
    `isExcellentHomework` TINYINT(1),
    `haveChecked` TINYINT(1),
    `checkedScore` FLOAT,

    PRIMARY KEY (`uuid`),
    FOREIGN KEY (`groupHomeworkUUID`) REFERENCES grouphomework(`uuid`),
    FOREIGN KEY (`userUUID`) REFERENCES user(`uuid`)
);

CREATE TABLE rating
(
    `uuid` VARCHAR(64),
    `homeworkUUID` VARCHAR(64),
    `userUUID` VARCHAR(64),
    `score` FLOAT,

    PRIMARY KEY (`uuid`),
    FOREIGN KEY (`homeworkUUID`) REFERENCES homework(`uuid`),
    FOREIGN KEY (`userUUID`) REFERENCES user(`uuid`)
);

--
-- 此外，我在navicat中，手动设置了以下属性为 unique
--     user(username), problem(name), grouphomework(name)
--
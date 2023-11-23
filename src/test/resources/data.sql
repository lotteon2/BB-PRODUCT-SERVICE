create table if not exists category
(
    category_id   bigint auto_increment
        primary key,
    created_at    datetime(6)  null,
    is_deleted    bit          null,
    updated_at    datetime(6)  null,
    category_name varchar(255) null
);

create table if not exists tag
(
    tag_id     bigint auto_increment
        primary key,
    created_at datetime(6)  null,
    is_deleted bit          null,
    updated_at datetime(6)  null,
    tag_name   varchar(255) null
);

create table if not exists flower
(
    flower_id          bigint auto_increment
        primary key,
    created_at         datetime(6)  null,
    is_deleted         bit          null,
    updated_at         datetime(6)  null,
    flower_name        varchar(255) null,
    language_of_flower varchar(255) null
);

INSERT INTO category (category_id, category_name)
VALUES (1, 'Electronics'),
       (2, 'Clothing'),
       (3, 'Home and Garden'),
       (4, 'Books'),
       (5, 'Sports and Outdoors'),
       (6, 'Toys and Games'),
       (7, 'Beauty and Personal Care'),
       (8, 'Automotive'),
       (9, 'Health and Household'),
       (10, 'Office Products');

INSERT INTO tag ( tag_name)
VALUES ('친구'),
       ('연인'),
       ('가족');

insert into flower(flower_id, flower_name, language_of_flower)
values (1, '장미', '장미 꽃말'),
       (2, '빨간 장미', '빨간 장미 꽃말'),
       (3, '파란 장미', '파란 장미 꽃말'),
       (4, '노란 장미', '노란 장미 꽃말');

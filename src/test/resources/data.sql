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

INSERT INTO tag (tag_id, tag_name)
VALUES (1, 'Technology'),
       (2, 'Fashion'),
       (3, 'Home Decor'),
       (4, 'Mystery'),
       (5, 'Outdoor Recreation'),
       (6, 'Educational'),
       (7, 'Beauty'),
       (8, 'Automotive'),
       (9, 'Wellness'),
       (10, 'Office Supplies');

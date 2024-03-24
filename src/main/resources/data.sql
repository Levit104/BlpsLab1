insert into role(name)
values ('ROLE_USER'),
       ('ROLE_GUIDE'),
       ('ROLE_ADMIN');

insert into country(name)
values ('Италия'),
       ('Франция'),
       ('Испания');

insert into city(name, country_id)
values ('Венеция', 1),
       ('Флоренция', 1),
       ('Рим', 1),
       ('Париж', 2),
       ('Марсель', 2),
       ('Брест', 2),
       ('Барселона', 3),
       ('Мадрид', 3),
       ('Севилья', 3);

insert into order_status(name)
values ('На рассмотрении'),
       ('Принят'),
       ('Отклонён');
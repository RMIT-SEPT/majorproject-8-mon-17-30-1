-- For local development only
drop table if exists user;

create table user (
    user_id int auto_increment primary key,
    first_name varchar(255) not null,
    last_name varchar(255) not null
);

insert into user (first_name, last_name) values
    ('John', 'Smith'),
    ('Tim', 'Apple');
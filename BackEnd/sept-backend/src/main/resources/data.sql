-- For local development only
drop table if exists user;
create table user (
    user_id int not null,
    username varchar(255) not null,
    password varchar(255) not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    street_address varchar(255) not null,
    city varchar(255) not null,
    state varchar(3) not null,
    postcode varchar(4) not null,
    role_id int not null,
    constraint user_pk primary key  (user_id),
    constraint user_unique_username unique key (username)
);

drop table if exists role;
create table role (
    role_id int not null,
    role_name varchar(255) not null,
    constraint role_pk primary key (role_id),
    constraint role_unique_role_name unique key (role_name)
);

alter table user
    add constraint user_foreign_role_id
    foreign key (role_id) references role(role_id)
;

insert into role values
    (0, 'ADMIN'),
    (1, 'WORKER'),
    (2, 'CUSTOMER')
;

insert into user values
    (0, 'John_Smith', '$2a$10$04qYVTsskUNhDATOFF3Hk.jVmQVaHG/MxzNMU2AMaSWx/t344pIhS', 'John', 'Smith', '123 ABC Street', 'Melbourne', 'VIC', '3000', 2),
    (1, 'Tim_Apple', '$2a$10$Pv/aDCOvwmdKIr.m0jWUPOZpw5Y5jGauOue2uN4EAEAtjRypkVjeW', 'Tim', 'Apple', '500 Bourke Street', 'Melbourne', 'VIC', '3000', 1)
;
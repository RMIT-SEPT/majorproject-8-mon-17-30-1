-- For local development only
drop table if exists Administrator, Customer, Worker, Services, Bookings, Timetable;

create table Administrator (
    a_user_id int primary key,
    password varchar(8) not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null
);

create table Customer (
    c_user_id int primary key,
    password varchar(8) not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    address varchar(255) not null,
    phone_number varchar (255) not null
);

create table Worker (
    w_user_id int primary key,
    password varchar(8) not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null
);

create table Services (
    service_id int primary key,
    service_date date,
    w_user_id int REFERENCES Worker(w_user_id)
);

create table Bookings (
    booking_id int primary key,
    service_id int REFERENCES Services(service_id),
    service_date date,
    w_user_id int REFERENCES Worker(w_user_id)
);

create table Timetable (
    time_id int primary key,
    month varchar(255),
    day varchar(255),
    hours int,
    w_user_id int REFERENCES Worker(w_user_id)
);
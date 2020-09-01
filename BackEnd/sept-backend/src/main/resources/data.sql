-- For local development only
-- drop table if exists user;
-- create table user (
--     user_id int not null,
--     username varchar(255) not null,
--     password varchar(255) not null,
--     first_name varchar(255) not null,
--     last_name varchar(255) not null,
--     role varchar(10) not null,
--     constraint user_pk primary key (user_id),
--     constraint user_unique_username unique (username)
-- );
--
-- drop table if exists admin;
-- create table admin (
--     admin_id int not null,
--     user_id int not null,
--     business_id int not null,
--     constraint admin_pk primary key (admin_id),
--     constraint admin_unique_user_id unique (user_id)
-- );
--
-- drop table if exists customer;
-- create table customer (
--     customer_id int not null,
--     user_id int not null,
--     street_address varchar(255) not null,
--     city varchar(255) not null,
--     state varchar(3) not null,
--     postcode varchar(4) not null,
--     constraint customer_pk primary key (customer_id),
--     constraint customer_unique_user_id unique (user_id)
-- );
--
-- drop table if exists worker;
-- create table worker (
--     worker_id int not null,
--     user_id int not null,
--     constraint worker_pk primary key (worker_id),
--     constraint worker_unique_user_id unique (user_id)
-- );
--
-- drop table if exists business;
-- create table  business (
--     business_id int not null,
--     business_name varchar(255),
--     constraint business_pk primary key (business_id)
-- );
--
-- drop table if exists service;
-- create table service (
--     service_id int not null,
--     business_id int not null,
--     service_name varchar(255) not null,
--     duration_minutes int not null,
--     constraint service_pk primary key (service_id)
-- );
--
-- drop table if exists service_worker;
-- create table service_worker (
--     service_id int not null,
--     worker_id int not null,
--     constraint service_worker_pk primary key (service_id, worker_id)
-- );
--
-- drop table if exists booking;
-- create table booking (
--     booking_id int not null,
--     service_id int not null,
--     customer_id int not null,
--     booking_time timestamp not null,
--     created_time timestamp not null,
--     last_modified_time timestamp not null,
--     status varchar(10) not null,
--     constraint booking_pk primary key (booking_id)
-- );
--
--
-- -- Constraints
-- alter table admin add constraint admin_foreign_user_id foreign key (user_id) references user(user_id);
-- alter table admin add constraint admin_foreign_business_id foreign key (business_id) references business(business_id);
--
-- alter table customer add constraint customer_foreign_user_id foreign key (user_id) references user(user_id);
--
-- alter table worker add constraint worker_foreign_user_id foreign key (user_id) references user(user_id);
--
-- alter table service add constraint service_foreign_business_id foreign key (business_id) references business(business_id);
--
-- alter table service_worker add constraint service_worker_foreign_service_id foreign key (service_id) references service(service_id);
-- alter table service_worker add constraint service_worker_foreign_worker_id foreign key (worker_id) references worker(worker_id);
--
-- alter table booking add constraint booking_foreign_service_id foreign key (service_id) references service(service_id);
-- alter table booking add constraint booking_foreign_customer_id foreign key (customer_id) references customer(customer_id);


-- Test values
insert into business (business_id, business_name) values
    (0, 'Virtucon')
;

insert into user (user_id, username, password, first_name, last_name, role) values
    (0, 'John_Smith', '$2a$10$04qYVTsskUNhDATOFF3Hk.jVmQVaHG/MxzNMU2AMaSWx/t344pIhS'/*test_password*/, 'John', 'Smith', 2),
    (1, 'no2', '$2a$10$Pv/aDCOvwmdKIr.m0jWUPOZpw5Y5jGauOue2uN4EAEAtjRypkVjeW'/*another_test_password*/, 'Number', 'Two', 1),
    (2, 'dr_evil', '$2a$10$04qYVTsskUNhDATOFF3Hk.jVmQVaHG/MxzNMU2AMaSWx/t344pIhS'/*test_password*/, 'Dr', 'Evil', 0),
    (3, 'ap', '$2a$10$Pv/aDCOvwmdKIr.m0jWUPOZpw5Y5jGauOue2uN4EAEAtjRypkVjeW'/*another_test_password*/, 'Austin', 'Powers', 1)
;

insert into admin (admin_id, user_id, business_id) values
    (0, 2, 0)
;

insert into customer (customer_id, user_id, street_address, city, state, postcode) values
    (0, 0, '123 ABC Street', 'Melbourne', 0, '3000'),
    (1, 3, 'London Pad', 'Melbourne', 0, '3000')
;

insert into worker (worker_id, user_id) values
    (0, 1)
;

insert into service (service_id, business_id, service_name, duration_minutes) values
    (0, 0, 'Preparation H', 30)
;

-- insert into service_worker values
--     (0, 0)
-- ;
--
-- insert into booking values
--     (0, 0, 0, {ts '2020-09-25 08:00:00.00'}, {ts '2020-08-17 18:47:52.69'}, {ts '2020-08-17 18:47:52.69'}, 0),
--     (1, 0, 1, {ts '2020-10-03 15:30:00.00'}, {ts '2020-08-18 18:47:52.69'}, {ts '2020-08-20 00:00:00.00'}, 1)
-- ;
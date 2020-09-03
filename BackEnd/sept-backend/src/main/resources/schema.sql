-- These tables will have to be created in the DB instance

drop table if exists user;
create table user (
    user_id int not null auto_increment,
    username varchar(255) not null,
    password varchar(255) not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    role int not null, -- 0=ADMIN,1=WORKER,2=CUSTOMER
    constraint user_pk primary key (user_id),
    constraint user_unique_username unique (username)
);

drop table if exists admin;
create table admin (
    admin_id int not null auto_increment,
    user_id int not null,
    business_id int not null,
    constraint admin_pk primary key (admin_id),
    constraint admin_unique_user_id unique (user_id)
);

drop table if exists customer;
create table customer (
    customer_id int not null auto_increment,
    user_id int not null,
    street_address varchar(255) not null,
    city varchar(255) not null,
    state varchar(3) not null,
    postcode varchar(4) not null,
    constraint customer_pk primary key (customer_id),
    constraint customer_unique_user_id unique (user_id)
);

drop table if exists worker;
create table worker (
    worker_id int not null auto_increment,
    user_id int not null,
    constraint worker_pk primary key (worker_id),
    constraint worker_unique_user_id unique (user_id)
);

drop table if exists business;
create table  business (
    business_id int not null auto_increment,
    business_name varchar(255),
    constraint business_pk primary key (business_id),
    constraint business_unique_business_name unique (business_name)
);

drop table if exists service;
create table service (
    service_id int not null auto_increment,
    business_id int not null,
    service_name varchar(255) not null,
    duration_minutes int not null,
    constraint service_pk primary key (service_id)
);

drop table if exists service_worker;
create table service_worker (
    service_worker_id int not null auto_increment,
    service_id int not null,
    worker_id int not null,
    constraint service_worker_pk primary key (service_worker_id)
);

drop table if exists booking;
create table booking (
    booking_id int not null auto_increment,
    service_worker_id int not null,
    customer_id int not null,
    booking_time timestamp not null,
    created_time timestamp not null,
    last_modified_time timestamp not null,
    status int not null, -- 0=ACTIVE,1=CANCELLED
    constraint booking_pk primary key (booking_id)
);


-- Constraints
alter table admin add constraint admin_foreign_user_id foreign key (user_id) references user(user_id);
alter table admin add constraint admin_foreign_business_id foreign key (business_id) references business(business_id);

alter table customer add constraint customer_foreign_user_id foreign key (user_id) references user(user_id);

alter table worker add constraint worker_foreign_user_id foreign key (user_id) references user(user_id);

alter table service add constraint service_foreign_business_id foreign key (business_id) references business(business_id);

alter table service_worker add constraint service_worker_foreign_service_id foreign key (service_id) references service(service_id);
alter table service_worker add constraint service_worker_foreign_worker_id foreign key (worker_id) references worker(worker_id);

alter table booking add constraint booking_foreign_service_id foreign key (service_worker_id) references service_worker(service_worker_id);
alter table booking add constraint booking_foreign_customer_id foreign key (customer_id) references customer(customer_id);

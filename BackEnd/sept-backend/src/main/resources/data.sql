-- Test values
insert into business (business_id, business_name) values
    (1, 'Virtucon')
;

insert into user (user_id, username, password, first_name, last_name, `role`) values
    (1, 'John_Smith', '$2a$10$04qYVTsskUNhDATOFF3Hk.jVmQVaHG/MxzNMU2AMaSWx/t344pIhS'/*test_password*/, 'John', 'Smith', 'CUSTOMER'),
    (2, 'no2', '$2a$10$Pv/aDCOvwmdKIr.m0jWUPOZpw5Y5jGauOue2uN4EAEAtjRypkVjeW'/*another_test_password*/, 'Number', 'Two', 'WORKER'),
    (3, 'dr_evil', '$2a$10$04qYVTsskUNhDATOFF3Hk.jVmQVaHG/MxzNMU2AMaSWx/t344pIhS'/*test_password*/, 'Dr', 'Evil', 'ADMIN'),
    (4, 'ap', '$2a$10$Pv/aDCOvwmdKIr.m0jWUPOZpw5Y5jGauOue2uN4EAEAtjRypkVjeW'/*another_test_password*/, 'Austin', 'Powers', 'WORKER'),
    (5, 'JJohnson', '$2a$10$04qYVTsskUNhDATOFF3Hk.jVmQVaHG/MxzNMU2AMaSWx/t344pIhS'/*test_password*/, 'James', 'Johnson', 'CUSTOMER'),
    (6, 'MaryW', '$2a$10$04qYVTsskUNhDATOFF3Hk.jVmQVaHG/MxzNMU2AMaSWx/t344pIhS'/*test_password*/, 'Mary', 'Williams', 'CUSTOMER'),
    (7, 'LinMil', '$2a$10$04qYVTsskUNhDATOFF3Hk.jVmQVaHG/MxzNMU2AMaSWx/t344pIhS'/*test_password*/, 'Linda', 'Miller', 'CUSTOMER'),
    (8, 'RichyRich', '$2a$10$04qYVTsskUNhDATOFF3Hk.jVmQVaHG/MxzNMU2AMaSWx/t344pIhS'/*test_password*/, 'Richard', 'Harris', 'CUSTOMER')
;

insert into admin (admin_id, user_id, business_id) values
    (1, 3, 1)
;

insert into customer (customer_id, user_id, street_address, city, state, postcode) values
    (1, 1, '123 ABC Street', 'Melbourne', 'VIC', '3000'),
    (2, 4, 'London Pad', 'Melbourne', 'VIC', '3000')
;

insert into worker (worker_id, user_id, status) values
    (1, 2, 'ACTIVE')
;

insert into service (service_id, business_id, service_name, duration_minutes, status) values
    (1, 1, 'Preparation H', 30, 'ACTIVE')
;

insert into service_worker values
    (1, 1, 1)
;

insert into availability values
    (1, 'FRIDAY', {t '08:00:00'}, {t '16:00:00'})
;

insert into service_worker_availability values
    (1, 1, 1, {d '2020-09-24'}, {d '2020-10-02'})
;

insert into booking values
    (1, 1, 1, {ts '2020-09-25 08:00:00.00'}, {ts '2020-08-17 18:47:52.69'}, {ts '2020-08-17 18:47:52.69'}, 'ACTIVE'),
    (3, 1, 1, {ts '2020-09-25 09:00:00.00'}, {ts '2020-08-17 19:47:52.69'}, {ts '2020-08-17 19:47:52.69'}, 'ACTIVE'),
    (2, 1, 2, {ts '2020-10-02 15:30:00.00'}, {ts '2020-08-18 18:47:52.69'}, {ts '2020-08-20 00:00:00.00'}, 'CANCELLED')
;
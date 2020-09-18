-- Test values
insert into business (business_id, business_name) values
    (0, 'Virtucon')
;

insert into user (user_id, username, password, first_name, last_name, role) values
    (0, 'John_Smith', '$2a$10$04qYVTsskUNhDATOFF3Hk.jVmQVaHG/MxzNMU2AMaSWx/t344pIhS'/*test_password*/, 'John', 'Smith', 'CUSTOMER'),
    (1, 'no2', '$2a$10$Pv/aDCOvwmdKIr.m0jWUPOZpw5Y5jGauOue2uN4EAEAtjRypkVjeW'/*another_test_password*/, 'Number', 'Two', 'WORKER'),
    (2, 'dr_evil', '$2a$10$04qYVTsskUNhDATOFF3Hk.jVmQVaHG/MxzNMU2AMaSWx/t344pIhS'/*test_password*/, 'Dr', 'Evil', 'ADMIN'),
    (3, 'ap', '$2a$10$Pv/aDCOvwmdKIr.m0jWUPOZpw5Y5jGauOue2uN4EAEAtjRypkVjeW'/*another_test_password*/, 'Austin', 'Powers', 'WORKER')
;

insert into admin (admin_id, user_id, business_id) values
    (0, 2, 0)
;

insert into customer (customer_id, user_id, street_address, city, state, postcode) values
    (0, 0, '123 ABC Street', 'Melbourne', 'VIC', '3000'),
    (1, 3, 'London Pad', 'Melbourne', 'VIC', '3000')
;

insert into worker (worker_id, user_id) values
    (0, 1)
;

insert into service (service_id, business_id, service_name, duration_minutes) values
    (0, 0, 'Preparation H', 30)
;

insert into service_worker values
    (0, 0, 0)
;

insert into booking values
    (0, 0, 0, {ts '2020-09-25 08:00:00.00'}, {ts '2020-08-17 18:47:52.69'}, {ts '2020-08-17 18:47:52.69'}, 'ACTIVE'),
    (2, 0, 0, {ts '2020-09-25 09:00:00.00'}, {ts '2020-08-17 19:47:52.69'}, {ts '2020-08-17 19:47:52.69'}, 'ACTIVE'),
    (1, 0, 1, {ts '2020-10-03 15:30:00.00'}, {ts '2020-08-18 18:47:52.69'}, {ts '2020-08-20 00:00:00.00'}, 'CANCELLED')
;
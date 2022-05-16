drop schema if exists communal_payments cascade;

create schema communal_payments;

create table communal_payments.users
(
    id bigint primary key GENERATED ALWAYS AS IDENTITY(minvalue 1 INCREMENT by 1),
    last_name varchar(50) not null,
    first_name varchar(50) not null,
    patronymic varchar(50) not null,
    email varchar(100) not null unique,
    phone_number varchar(20) not null,
    registration_date timestamp (0) default now()
);

create table communal_payments.billing_addresses
(
    id bigint primary key GENERATED ALWAYS AS identity(minvalue 1 INCREMENT by 1),
    billing_address varchar(300) not null,
    user_id bigint references communal_payments.users(id)
);

create table communal_payments.templates
(
    id bigint GENERATED ALWAYS AS IDENTITY(minvalue 1 INCREMENT by 1),
    template_name varchar(30) not null,
    iban varchar(29) not null,
    purpose_of_payment varchar(300) not null,
    billing_address_id bigint,
    primary key (id),
    foreign key (billing_address_id) references communal_payments.billing_addresses(id)
);

create table communal_payments.payments
(
    id bigint GENERATED ALWAYS AS identity(minvalue 1 INCREMENT by 1),
    template_id bigint,
    card_number varchar(20) not null,
    payment_amount numeric(12,2) not null,
    status varchar(10) not null,
    time_of_creation timestamp (0) not null,
    status_change_time timestamp (0),
    primary key (id),
    foreign key (template_id) references communal_payments.templates(id)
);
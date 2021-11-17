drop table if exists payments cascade;
drop table if exists point_cards cascade;
drop table if exists products cascade;
drop table if exists points cascade;
drop table if exists projects cascade;
drop table if exists developers cascade;
drop table if exists commands cascade;
drop table if exists account_tokens cascade;
drop table if exists account_token_values cascade;
drop table if exists project_tokens cascade;
drop table if exists expired_project_tokens cascade;
drop table if exists expired_authentication_tokens cascade;
drop table if exists project_token_seq cascade;

create table developers
(
    seq         bigint      not null auto_increment,
    name        varchar(12) not null,
    email       varchar(50) not null unique,
    login_token varchar(80),
    create_at   datetime    not null default current_timestamp(),
    last_login_at datetime not null default current_timestamp(),
    activate    bool        not null default true,

    primary key (seq)
);

create table account_tokens
(
    seq bigint not null auto_increment,
    name varchar(12) not null,
    email varchar(50) not null unique,
    token_value varchar(132) not null unique,
    account_mode enum('REGISTER', 'DEACTIVATE') not null,
    expired_at datetime not null default current_timestamp(),

    primary key (seq)
);

create table projects
(
    seq                         bigint          not null auto_increment,
    owner_id                    bigint          not null,
    title                       varchar(64)     not null,
    name                        varchar(128)    not null,
    password                    varchar(64),
    unpaid_call_count           bigint          not null,
    client_address              varchar(128),
    client_port                 bigint,
    client_token                varchar(256),
    connection_server_name      varchar(64),
    connection_server_connected boolean         default false,
    primary key (seq),
    constraint fk_projects_to_developers foreign key (owner_id) references developers (seq) on delete restrict on update restrict
);

create table project_tokens
(
    seq                 bigint          not null    auto_increment,
    token               varchar(256)    not null,
    created_date        datetime        not null,
    issued_date         datetime,
    expired_date        datetime,
    primary key (seq)
);

create table points
(
    seq       bigint         not null auto_increment,
    owner_id  bigint         not null,
    name      varchar(32)    not null,
    amount    decimal(19, 2) not null,
    balance   decimal(19, 2) not null,
    create_at datetime       not null default current_timestamp(),
    primary key (seq),
    constraint fk_points_to_developers foreign key (owner_id) references developers (seq) on delete restrict on update restrict
);

create table products
(
    seq       bigint         not null auto_increment,
    name      varchar(32)    not null,
    price     decimal(19, 2) not null,
    available bool           not null default true,
    primary key (seq)
);

create table point_cards
(
    seq       bigint         not null auto_increment,
    name      varchar(32)    not null,
    point     decimal(19, 2) not null,
    price     decimal(19, 2) not null,
    available bool           not null default true,
    primary key (seq)
);

create table payments
(
    seq              bigint                                                                 not null auto_increment,
    buyer_id         bigint                                                                 not null,
    receipt_id       varchar(128),
    order_id         varchar(128)                                                           not null,
    method           enum ('CARD','TRANS','VBANK','PHONE'),
    name             varchar(32),
    point            decimal(19, 2)                                                         not null,
    amount           decimal(19, 2)                                                         not null,
    status           enum ('READY','PAID','FAILED','CANCELLED') default 'READY'             not null,
    create_at        datetime                                   default current_timestamp() not null,
    paid_at          datetime,
    failed_at        datetime,
    cancelled_amount decimal(19, 2)                             default 0,
    cancelled_at     datetime,

    primary key (seq),
    constraint fk_payments_to_developers foreign key (buyer_id) references developers (seq) on delete restrict on update restrict
);

create table account_token_values
(
    seq bigint not null auto_increment,
    value varchar(132) not null,
    used boolean not null default false,

    primary key (seq)
);

create table expired_authentication_tokens
(
    seq bigint not null auto_increment,
    value varchar(256) not null unique,

    primary key (seq)
);

create table project_token_seq
(
    SEQUENCE_NAME varchar(255) not null ,
    NEXT_VAL numeric(18,0),
    PRIMARY KEY (SEQUENCE_NAME)
);
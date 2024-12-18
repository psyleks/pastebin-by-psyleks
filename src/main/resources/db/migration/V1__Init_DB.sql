create sequence message_seq start with 2 increment by 1;
create sequence user_seq start with 2 increment by 1;
create table message
(
    id       bigint        not null,
    user_id  bigint,
    text     varchar(2048) not null,
    filename varchar(255),
    tag      varchar(255),
    created_at timestamp not null default current_timestamp,
    primary key (id)
);
create table user_role
(
    user_id bigint not null,
    roles   varchar(255) check (roles in ('USER', 'ADMIN'))
);
create table usr
(
    active          boolean      not null,
    id              bigint       not null,
    username        varchar(31),
    activation_code varchar(255),
    email           varchar(255) not null,
    password        varchar(255) not null,
    primary key (id)
);
alter table if exists message
    add constraint message_user_fk foreign key (user_id) references usr;
alter table if exists user_role
    add constraint user_role_user_fk foreign key (user_id) references usr;

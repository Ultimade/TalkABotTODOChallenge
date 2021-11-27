create sequence hibernate_sequence;

create table IF NOT EXISTS table_todo
(
    id          bigserial    NOT NULL
        constraint table_todo_pkey
            primary key,
    created_by   varchar(255),
    created_date timestamp not null,
    modified_by  varchar(255),
    updated_date timestamp,
    name         varchar(200) not null,
    deadline     timestamp,
    priority     integer
);
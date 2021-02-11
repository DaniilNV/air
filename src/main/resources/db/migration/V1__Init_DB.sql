create table hibernate_sequence
(
    next_val bigint
)
    engine = InnoDB;
insert into hibernate_sequence
values (1);
insert into hibernate_sequence
values (1);

create table message
(
    id       bigint        not null,
    filename varchar(255),
    tag      varchar(255),
    text     varchar(2048) not null,
    user_id  bigint,
    primary key (id)
)
    engine = InnoDB;

create table user_role
(
    user_id bigint not null,
    roles   varchar(255)
)
    engine = InnoDB;

create table users
(
    id              bigint       not null,
    activation_code varchar(255),
    active          bit,
    email           varchar(255) not null,
    password        varchar(255) not null,
    username        varchar(255) not null,
    primary key (id)

)
    engine = InnoDB;
alter table message
    add constraint message_users_fk foreign key (user_id) references users (id);
alter table user_role
    add constraint users_role_users_fk foreign key (user_id) references users (id);

create table user_subscriptions
(
    channel_id    bigint not null references users,
    subscriber_id bigint not null references users,
    primary key (channel_id, subscriber_id)
)
    engine = InnoDB;
create table message_like
(
    user_id    bigint not null references users,
    message_id bigint not null references message,
    primary key (user_id, message_id)
)
    engine = InnoDB;
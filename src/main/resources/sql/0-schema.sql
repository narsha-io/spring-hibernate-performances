create table continent
(
    id   serial primary key,
    name varchar(200) not null unique
);

create table region
(
    id        serial primary key,
    name      varchar(200) not null unique,
    continent_id bigint       not null references continent
);

create table country
(
    id        serial primary key,
    name      varchar(200) not null unique,
    region_id bigint       not null references region
);
create table book
(
    id    varchar(200) primary key,
    title varchar(500) not null
);

create table users
(
    id         varchar(200) primary key,
    username   varchar(200) not null,
    country_id bigint       not null references country
);

create table review
(
    book_id     varchar(200) references book (id),
    user_id     varchar(200) references users (id),
    score       double precision not null,
    summary     varchar(500)     not null,
    description varchar(500),
    primary key (book_id, user_id)
);

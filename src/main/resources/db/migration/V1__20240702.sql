create table users(
    id int generated by default as identity primary key,
    username varchar(255) not null unique,
    password varchar(255) not null,
    role varchar(32) not null,
    first_name VARCHAR(64)   NOT NULL,
    last_name  VARCHAR(64)   NOT NULL,
    status varchar(32) not null,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

create table files(
    id int generated by default as identity primary key,
    location varchar(255) not null,
    status varchar(32) not null
);

create table events(
    id int generated by default as identity primary key,
    user_id int not null references users(id),
    file_id int not null references files(id),
    status varchar(32) not null
);
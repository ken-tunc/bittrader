create table if not exists product_code
(
    product_code varchar(30) primary key
);

create table if not exists duration
(
    duration varchar(10) primary key
);

create table if not exists candle
(
    product_code varchar(30),
    duration     varchar(10),
    date_time    datetime,
    open         float,
    close        float,
    high         float,
    low          float,
    volume       float,
    primary key (product_code, duration, date_time),
    foreign key (product_code) references product_code (product_code),
    foreign key (duration) references duration (duration)
);

create table if not exists ema_params
(
    product_code     varchar(30),
    duration         varchar(10),
    short_time_frame int      not null,
    long_time_frame  int      not null,
    created_at       datetime not null default current_timestamp,
    primary key (product_code, duration, created_at),
    foreign key (product_code) references product_code (product_code),
    foreign key (duration) references duration (duration)
);

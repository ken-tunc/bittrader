create table ema_params
(
    product_code     varchar(30),
    duration         varchar(10),
    short_time_frame int      not null,
    long_time_frame  int      not null,
    created_at       datetime not null default current_timestamp,
    primary key (product_code, duration, created_at),
    foreign key (product_code) references product_code (product_code),
    foreign key (duration) references duration (duration)
) engine = InnoDB;

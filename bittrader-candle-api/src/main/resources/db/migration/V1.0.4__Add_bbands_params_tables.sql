create table bbands_params
(
    product_code varchar(30),
    duration     varchar(10),
    time_frame   int      not null,
    buy_k        float    not null,
    sell_k       float    not null,
    created_at   datetime not null default current_timestamp,
    primary key (product_code, duration, created_at),
    foreign key (product_code) references product_code (product_code),
    foreign key (duration) references duration (duration)
) engine = InnoDB;

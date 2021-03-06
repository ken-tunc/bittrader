create table product_code
(
    product_code varchar(30) primary key
) engine = InnoDB;

create table duration
(
    duration varchar(10) primary key
) engine = InnoDB;

create table candle
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
) engine = InnoDB;

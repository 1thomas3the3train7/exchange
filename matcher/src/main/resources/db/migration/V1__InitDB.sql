drop table if exists users cascade ;
drop table if exists bank_account cascade ;
drop table if exists order_buy cascade ;
drop table if exists order_sell cascade ;

CREATE TABLE users(
    id serial,
    token varchar(255),
    primary key (id)
);

CREATE TABLE bank_account (
    id serial,
    currency varchar(20),
    balance float8,
    owner_id bigint,
    primary key (id),
    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE order_buy (
    id serial,
    currency_to_buy varchar(20),
    currency_to_sell varchar(20),
    count_to_buy float8,
    count_to_sell float8,
    max_price float8,
    owner_id bigint,
    from_bank_account_id bigint,
    to_bank_account_id bigint,
    date_publication timestamp,
    date_update timestamp,
    active bool,
    order_status varchar(20),
    primary key (id),
    FOREIGN KEY (from_bank_account_id) REFERENCES bank_account (id) ON DELETE CASCADE,
    FOREIGN KEY (to_bank_account_id) REFERENCES bank_account (id) ON DELETE CASCADE,
    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE order_sell (
    id serial,
    currency_to_buy varchar(20),
    currency_to_sell varchar(20),
    count_to_buy float8,
    count_to_sell float8,
    min_price float8,
    owner_id bigint,
    from_bank_account_id bigint,
    to_bank_account_id bigint,
    date_publication timestamp,
    date_update timestamp,
    active bool,
    order_status varchar(20),
    primary key (id),
    FOREIGN KEY (from_bank_account_id) REFERENCES bank_account (id) ON DELETE CASCADE,
    FOREIGN KEY (to_bank_account_id) REFERENCES bank_account (id) ON DELETE CASCADE,
    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);
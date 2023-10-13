insert into users (token)
values ('token1'),
       ('token2'),
       ('token3'),
       ('token4');

insert into bank_account (currency, balance, owner_id)
values ('RUB', 1000, 1),
       ('GRADE', 500, 1),
       ('RUB', 5000, 2),
       ('GRADE', 1000, 2),
       ('RUB', 2500, 3),
       ('GRADE', 1300, 3),
       ('RUB', 2700, 4),
       ('GRADE', 1700, 4);

insert into order_buy (currency_to_buy, currency_to_sell, count_to_buy, count_to_sell, max_price, owner_id, from_bank_account_id,
                       to_bank_account_id, date_publication, date_update, active)
values ('GRADE', 'RUB', 800, 8000, 10, 1, 1, 2, '2023-10-06 00:00:00', '2023-10-06 00:00:00', true),
       ('GRADE', 'RUB', 100, 12000, 10, 2, 3, 4, '2023-10-07 00:00:00', '2023-10-07 00:00:00', true),
       ('GRADE', 'RUB', 2000, 40000, 20, 3, 5, 6, '2023-10-08 00:00:00', '2023-10-08 00:00:00', true);

insert into order_sell (currency_to_buy, currency_to_sell, count_to_buy, count_to_sell, min_price, owner_id, from_bank_account_id,
                        to_bank_account_id, date_publication, date_update, active)
values ('RUB', 'GRADE', 25000, 2500, 10, 2, 4, 3, '2023-10-01 00:00:00', '2023-10-01 00:00:00', true),
       ('RUB', 'GRADE', 20000, 1000, 20, 3, 6, 5, '2023-10-02 00:00:00', '2023-10-02 00:00:00', true),
       ('RUB', 'GRADE', 22500, 1500, 15, 4, 8, 7, '2023-10-03 00:00:00', '2023-10-03 00:00:00', true);


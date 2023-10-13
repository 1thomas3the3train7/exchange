CREATE TEMPORARY TABLE order_sell_temp
(
    id                   serial,
    currency_to_buy      varchar(20),
    currency_to_sell     varchar(20),
    count_to_buy         float8,
    count_to_sell        float8,
    min_price            float8,
    owner_id             bigint,
    from_bank_account_id bigint,
    to_bank_account_id   bigint,
    date_publication     timestamp,
    date_update          timestamp,
    active               bool,
    isenough             bool
);

CREATE OR REPLACE FUNCTION buy_trade_operation(sell_orders order_sell_temp, function_buy_account_id integer,
                                               function_count_to_buy float8, function_max_price integer)
    RETURNS TABLE
            (
                id              integer,
                id_order_sell   integer,
                id_bank_account interval,
                operation_sum   float8,
                buy_account_id  integer,
                is_final_result bool
            )
    LANGUAGE plpgsql
AS
$$
DECLARE
    id_bank_account   integer;
    id_order_sell     integer;
    result_sum        float8;
    buy_sum           float8;
    final_delta_count float8;
    sell_sum          float8 := 0;
    sell_count        float8;
    row_record        RECORD;
    is_enough         bool;
BEGIN

    CREATE TEMPORARY TABLE result_table
    (
        id              serial,
        id_order_sell   integer,
        id_bank_account interval,
        operation_sum   float8,
        buy_account_id  integer,
        is_final_result bool
    );

    SELECT isenough FROM sell_orders LIMIT 1 INTO is_enough;

    buy_sum = function_count_to_buy * function_max_price;

    FOR row_record IN SELECT * FROM sell_orders
        LOOP
            SELECT row_record.count_to_sell INTO sell_count;
            sell_sum = sell_sum + sell_count;
            IF sell_sum >= function_count_to_buy THEN
                final_delta_count = function_count_to_buy - (sell_sum - sell_count);

                UPDATE bank_account
                SET balance = balance + final_delta_count * row_record.min_price
                WHERE id = row_record.to_bank_account_id
                RETURNING id INTO id_bank_account;
                UPDATE bank_account SET balance = balance + row_record.count_to_sell WHERE id = function_buy_account_id;
                UPDATE order_sell
                SET count_to_sell = count_to_sell - final_delta_count
                WHERE id = row_record.id
                RETURNING id INTO id_order_sell;

                result_sum = (sell_sum - sell_count) + final_delta_count;

                INSERT INTO result_table (operation_sum, buy_account_id, id_order_sell, id_bank_account,
                                          is_final_result)
                VALUES (result_sum, buy_account_id, id_order_sell, id_bank_account, true);
            ELSE
                UPDATE bank_account
                SET balance = balance + row_record.count_to_sell * row_record.min_price
                WHERE id = row_record.to_bank_account_id
                RETURNING id INTO id_bank_account;

                UPDATE bank_account SET balance = balance + row_record.count_to_sell WHERE id = function_buy_account_id;

                UPDATE order_sell
                SET count_to_sell = 0
                WHERE id = row_record.id
                RETURNING id INTO id_order_sell;

                INSERT INTO result_table (operation_sum, id_bank_account, id_order_sell, is_final_result)
                VALUES (sell_sum, id_bank_account, id_order_sell, false);
            END IF;
        END LOOP;

    RETURN QUERY SELECT * FROM result_table;

    DROP TABLE order_sell_temp;
END;
$$


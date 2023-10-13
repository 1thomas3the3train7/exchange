DROP FUNCTION IF EXISTS buy_operation(character varying, character varying,
                                      numeric, numeric, bigint, bigint, bigint);
CREATE OR REPLACE FUNCTION buy_operation(function_currency_to_buy varchar,
                                         function_currency_to_sell varchar,
                                         function_count_to_buy decimal,
                                         function_max_price decimal,
                                         function_skip_owner_id bigint,
                                         function_buy_account_id bigint,
                                         function_order_buy_id bigint)
    RETURNS TABLE
            (
                id              integer,
                id_order_sell   integer,
                id_bank_account integer,
                operation_sum   float8,
                buy_account_id  integer,
                date_creation   timestamp,
                price           float8,
                is_final_result bool,
                is_enough bool
            )
    LANGUAGE plpgsql
AS
$$
DECLARE
    sum                      DECIMAL := 0;
    row_record               RECORD;
    balance_value            DECIMAL;
    id_bank_account          integer;
    id_order_sell            integer;
    result_sum               float8;
    final_delta_count        float8;
    sell_sum                 float8  := 0;
    sell_count               float8;
    is_enough_res                bool;
BEGIN
    DROP TABLE IF EXISTS result_table_buy;
    DROP TABLE IF EXISTS result_table;
    DROP TABLE IF EXISTS temp_table;

    CREATE TEMPORARY TABLE result_table_buy
    (
        id              serial,
        id_order_sell   integer,
        id_bank_account integer,
        operation_sum   float8,
        buy_account_id  integer,
        price           float8,
        date_creation   timestamp,
        is_final_result bool,
        is_enough bool
    );

    CREATE TEMPORARY TABLE temp_table AS
    SELECT *
    FROM order_sell os
    WHERE os.currency_to_sell = function_currency_to_buy
      AND os.currency_to_buy = function_currency_to_sell
      AND os.min_price <= function_max_price
      AND os.owner_id != function_skip_owner_id
    ORDER BY min_price ASC, date_publication ASC;

    CREATE TEMPORARY TABLE result_table AS
    SELECT *, FALSE AS isEnough FROM order_sell WHERE FALSE;

    FOR row_record IN SELECT * FROM temp_table
        LOOP
            SELECT row_record.count_to_sell INTO balance_value;

            sum = sum + balance_value;

            INSERT INTO result_table
            VALUES (row_record.id,
                    row_record.currency_to_buy,
                    row_record.currency_to_sell,
                    row_record.count_to_buy,
                    row_record.count_to_sell,
                    row_record.min_price,
                    row_record.owner_id,
                    row_record.from_bank_account_id,
                    row_record.to_bank_account_id,
                    row_record.date_publication,
                    row_record.date_update,
                    row_record.active,
                    row_record.order_status);

            IF sum > function_count_to_buy OR sum = function_count_to_buy THEN
                EXIT;
            end if;
        END LOOP;

    IF sum < function_count_to_buy THEN
        is_enough_res = FALSE;
        UPDATE result_table SET is_enough = FALSE;
    ELSE
        is_enough_res = TRUE;
        UPDATE result_table SET is_enough = TRUE;
    end if;

    FOR row_record IN SELECT * FROM result_table
        LOOP
            SELECT row_record.count_to_sell INTO sell_count;
            sell_sum = sell_sum + sell_count;
            IF sell_sum >= function_count_to_buy THEN
                final_delta_count = function_count_to_buy - (sell_sum - sell_count);

                SELECT *
                FROM update_bank_account(final_delta_count * row_record.min_price, row_record.to_bank_account_id)
                INTO id_bank_account;
                --                     UPDATE bank_account
--                     SET balance = balance + final_delta_count * row_record.min_price
--                     WHERE bank_account.id = row_record.to_bank_account_id
--                     RETURNING bank_account.id INTO id_bank_account;
                PERFORM *
                FROM update_bank_account(final_delta_count, function_buy_account_id);
                --                     UPDATE bank_account
--                     SET balance = balance + row_record.count_to_sell
--                     WHERE bank_account.id = function_buy_account_id;
                SELECT *
                FROM update_count_order_sell(final_delta_count, row_record.id)
                INTO id_order_sell;

                PERFORM *
                FROM update_count_order_buy(final_delta_count, function_order_buy_id);
                --                     UPDATE order_sell
--                     SET count_to_sell = count_to_sell - final_delta_count
--                     WHERE order_sell.id = row_record.id
--                     RETURNING order_sell.id INTO id_order_sell;

                result_sum = (sell_sum - sell_count) + final_delta_count;

                INSERT INTO result_table_buy (operation_sum, buy_account_id, id_order_sell, id_bank_account,
                                              is_final_result, is_enough, price)
                VALUES (result_sum, buy_account_id, id_order_sell, id_bank_account, true, is_enough_res, row_record.min_price);
            ELSE
                SELECT *
                FROM update_bank_account(row_record.count_to_sell * row_record.min_price,
                                         row_record.to_bank_account_id)
                INTO id_bank_account;
                --                     UPDATE bank_account
--                     SET balance = balance + row_record.count_to_sell * row_record.min_price
--                     WHERE bank_account.id = row_record.to_bank_account_id
--                     RETURNING bank_account.id INTO id_bank_account;
                PERFORM *
                FROM update_bank_account(row_record.count_to_sell, function_buy_account_id);
                --                     UPDATE bank_account
--                     SET balance = balance + row_record.count_to_sell
--                     WHERE bank_account.id = function_buy_account_id;

                SELECT *
                FROM update_count_to_0_order_sell(row_record.id)
                INTO id_order_sell;

                PERFORM *
                FROM update_count_order_buy(row_record.count_to_sell, function_order_buy_id);
                --                     UPDATE order_sell
--                     SET count_to_sell = 0
--                     WHERE order_sell.id = row_record.id
--                     RETURNING order_sell.id INTO id_order_sell;

                INSERT INTO result_table_buy (operation_sum, id_bank_account, id_order_sell, is_final_result, is_enough, price)
                VALUES (sell_sum, id_bank_account, id_order_sell, false, is_enough, row_record.min_price);
            END IF;
        END LOOP;
    RETURN QUERY SELECT * FROM result_table_buy;
END ;
$$;

CREATE OR REPLACE FUNCTION update_bank_account(function_balance float8, function_bank_account_id bigint)
    RETURNS bigint
    LANGUAGE plpgsql
AS
$$
DECLARE
    res bigint;
BEGIN
    UPDATE bank_account
    SET balance = balance + function_balance
    WHERE id = function_bank_account_id
    RETURNING id INTO res;
    RETURN res;
END;
$$;

CREATE OR REPLACE FUNCTION update_count_to_0_order_sell(function_id bigint)
    RETURNS bigint
    LANGUAGE plpgsql
AS
$$
DECLARE
    res bigint;
BEGIN
    UPDATE order_sell
    SET count_to_sell = 0
    WHERE id = function_id
    RETURNING id INTO res;
    RETURN res;
END;

$$;

CREATE OR REPLACE FUNCTION update_count_order_sell(function_count_to_minus float8, function_id bigint)
    RETURNS bigint
    LANGUAGE plpgsql
AS
$$
DECLARE
    res bigint;
BEGIN
    UPDATE order_sell
    SET count_to_sell = count_to_sell - function_count_to_minus
    WHERE id = function_id
    RETURNING id INTO res;
    RETURN res;
END;
$$;

CREATE OR REPLACE FUNCTION update_count_order_buy(function_count_to_minus float8, function_id bigint)
    RETURNS bigint
    LANGUAGE plpgsql
AS
$$
DECLARE
    res bigint;
BEGIN
    UPDATE order_buy
    SET count_to_buy = count_to_buy - function_count_to_minus
    WHERE id = function_id
    RETURNING id INTO res;
    RETURN res;
END;
$$;

CREATE OR REPLACE FUNCTION update_count_to_0_order_buy(function_id bigint)
    RETURNS bigint
    LANGUAGE plpgsql
AS
$$
DECLARE
    res bigint;
BEGIN
    UPDATE order_buy
    SET count_to_buy = 0
    WHERE id = function_id
    RETURNING id INTO res;
    RETURN res;
END;
$$;
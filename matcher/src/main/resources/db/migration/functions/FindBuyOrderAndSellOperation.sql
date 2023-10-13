DROP FUNCTION IF EXISTS sell_operation(function_currency_to_buy varchar, function_currency_to_sell varchar,
                                       function_count_to_sell decimal, function_min_price decimal,
                                       function_skip_owner_id bigint);

CREATE OR REPLACE FUNCTION sell_operation(function_currency_to_buy varchar,
                                          function_currency_to_sell varchar,
                                          function_count_to_sell decimal,
                                          function_min_price decimal,
                                          function_skip_owner_id bigint,
                                          function_sell_account_id bigint,
                                          function_order_sell_id bigint)
    RETURNS TABLE
            (
                id              integer,
                id_order_buy   integer,
                id_bank_account integer,
                operation_sum   float8,
                buy_account_id  integer,
                date_creation   timestamp,
                is_final_result bool,
                is_enough bool
            )
    LANGUAGE plpgsql
AS
$$
DECLARE
    sum               DECIMAL := 0;
    row_record        RECORD;
    balance_value     DECIMAL;
    buy_sum           float8;
    buy_count         float8;
    result_sum        float8;
    final_delta_count float8;
    id_bank_account_res   bigint;
    id_order_buy_res      bigint;
    is_enough_res bool;
BEGIN
    DROP TABLE IF EXISTS result_table_buy;
    DROP TABLE IF EXISTS result_table;
    DROP TABLE IF EXISTS temp_table;

    CREATE TEMPORARY TABLE result_table_sell
    (
        id              serial,
        id_order_buy   integer,
        id_bank_account integer,
        operation_sum   float8,
        buy_account_id  integer,
        date_creation timestamp,
        is_final_result bool,
        is_enough bool
    );

    CREATE TEMPORARY TABLE temp_table AS
    SELECT *
    FROM order_buy ob
    WHERE ob.currency_to_sell = function_currency_to_buy
      AND ob.currency_to_buy = function_currency_to_sell
      AND ob.max_price >= function_min_price
      AND ob.owner_id != function_skip_owner_id
    ORDER BY ob.max_price DESC, ob.date_publication ASC;

    CREATE TEMPORARY TABLE result_table AS
    SELECT *, FALSE AS isEnough FROM order_buy WHERE FALSE;

    FOR row_record IN SELECT * FROM temp_table
        LOOP
            SELECT row_record.count_to_buy INTO balance_value;

            sum = sum + balance_value;
            IF sum > function_count_to_sell OR sum = function_count_to_sell THEN
                INSERT INTO result_table
                VALUES (row_record.id,
                        row_record.currency_to_buy,
                        row_record.currency_to_sell,
                        row_record.count_to_buy,
                        row_record.count_to_sell,
                        row_record.max_price,
                        row_record.owner_id,
                        row_record.from_bank_account_id,
                        row_record.to_bank_account_id,
                        row_record.date_publication,
                        row_record.date_update,
                        row_record.active,
                        row_record.order_status);
                EXIT;
            ELSE
                INSERT INTO result_table
                VALUES (row_record.id,
                        row_record.currency_to_buy,
                        row_record.currency_to_sell,
                        row_record.count_to_buy,
                        row_record.count_to_sell,
                        row_record.max_price,
                        row_record.owner_id,
                        row_record.from_bank_account_id,
                        row_record.to_bank_account_id,
                        row_record.date_publication,
                        row_record.date_update,
                        row_record.active,
                        row_record.order_status);
            end if;
        END LOOP;

    IF sum < function_count_to_sell THEN
        is_enough_res = FALSE;
        UPDATE result_table SET isEnough = FALSE;
--         RETURN QUERY SELECT * FROM result_table;
    ELSE
        is_enough_res = TRUE;
        UPDATE result_table SET isEnough = TRUE;
--         RETURN QUERY SELECT * FROM result_table;
    end if;
    FOR row_record IN SELECT * FROM result_table
        LOOP
            SELECT row_record.count_to_buy INTO buy_count;
            buy_sum = buy_sum + buy_count;
            IF buy_sum >= function_count_to_sell THEN
                final_delta_count = function_count_to_sell - (buy_sum - buy_count);

                PERFORM * FROM update_bank_account(final_delta_count * function_min_price, function_sell_account_id);

                PERFORM * FROM update_count_order_sell(final_delta_count, function_order_sell_id);

                SELECT *
                FROM update_bank_account(final_delta_count, row_record.to_bank_account_id)
                INTO id_bank_account_res;

                SELECT *
                FROM update_count_order_buy(final_delta_count, row_record.id)
                INTO id_order_buy_res;

                result_sum = (buy_sum - buy_count) + final_delta_count;

                INSERT INTO result_table_sell (id_order_buy, id_bank_account, operation_sum, is_final_result, is_enough, date_creation)
                VALUES (id_order_buy_res, id_bank_account_res, result_sum, true, is_enough_res, (SELECT current_timestamp));
            ELSE
                PERFORM *
                FROM update_bank_account(row_record.count_to_buy * function_min_price, function_sell_account_id);

                PERFORM * FROM update_count_order_sell(row_record.count_to_buy, function_order_sell_id);

                SELECT * FROM update_bank_account(row_record.count_to_buy, row_record.to_bank_account_id)
                INTO id_bank_account_res;

                SELECT * FROM update_count_to_0_order_buy(row_record.id)
                INTO id_order_buy_res;

                INSERT INTO result_table_sell (id_order_buy, id_bank_account, operation_sum, is_final_result, is_enough, date_creation)
                VALUES (id_order_buy, id_bank_account, buy_sum, false, is_enough_res, (SELECT current_timestamp));
            end if;
        END LOOP;

    RETURN QUERY SELECT * FROM result_table_sell;
END ;
$$;
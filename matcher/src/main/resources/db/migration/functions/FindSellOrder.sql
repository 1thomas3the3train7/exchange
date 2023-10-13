DROP FUNCTION IF EXISTS find_sell_order_for_buy_request(character varying, character varying,
                                                        numeric, numeric, bigint);
CREATE OR REPLACE FUNCTION find_sell_order_for_buy_request(function_currency_to_buy varchar,
                                                           function_currency_to_sell varchar,
                                                           function_count_to_buy decimal,
                                                           function_max_price decimal,
                                                           function_skip_owner_id bigint)
    RETURNS TABLE
            (
                id                   integer,
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
                order_status varchar(20),
                isEnough             bool
            )
    LANGUAGE plpgsql
AS
$$
DECLARE
    sum           DECIMAL := 0;
    row_record    RECORD;
    balance_value DECIMAL;
BEGIN

    DROP TABLE IF EXISTS result_table;
    DROP TABLE IF EXISTS temp_table;

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
            IF sum > function_count_to_buy OR sum = function_count_to_buy THEN
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
                EXIT;
            ELSE
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
            end if;
        END LOOP;

    IF sum < function_count_to_buy THEN
        UPDATE result_table SET isEnough = FALSE;



        RETURN QUERY SELECT * FROM result_table;
    ELSE
        UPDATE result_table SET isEnough = TRUE;


        RETURN QUERY SELECT * FROM result_table;
    end if;
END ;
$$;
CREATE OR REPLACE FUNCTION find_operation_by_date_and_currency(function_from_date timestamp,
                                                               function_to_date timestamp,
                                                               function_currency_to_buy varchar,
                                                               function_currency_to_sell varchar,
                                                               function_type_order varchar)
    RETURNS TABLE
            (
                id integer,
                count_in_operation float8,
                operation_status varchar(20),
                to_bank_account_id bigint,
                order_id bigint,
                price float8,
                date_creation timestamp
            )
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF function_type_order = 'BUY' THEN
        RETURN QUERY SELECT oh.*
                     FROM operation_history oh
                              LEFT JOIN order_buy ob ON oh.order_id = ob.id
                     WHERE oh.date_creation >= function_from_date
                       AND oh.date_creation <= function_to_date
                       AND ob.currency_to_buy = function_currency_to_buy
                       AND ob.currency_to_sell = function_currency_to_sell;
    ELSE
        RETURN QUERY SELECT oh.*
                     FROM operation_history oh
                              LEFT JOIN order_sell os ON oh.order_id = os.id
                     WHERE oh.date_creation >= function_from_date
                       AND oh.date_creation <= function_to_date
                       AND os.currency_to_buy = function_currency_to_buy
                       AND os.currency_to_sell = function_currency_to_sell;
    END IF;
END;
$$
DROP FUNCTION IF EXISTS find_sell_and_buy_order();
CREATE OR REPLACE FUNCTION find_sell_and_buy_order()
    RETURNS TABLE
            (
                id                   integer,
                currency_to_buy      varchar(20),
                currency_to_sell     varchar(20),
                count_to_buy         float8,
                count_to_sell        float8,
                price                float8,
                owner_id             bigint,
                from_bank_account_id bigint,
                to_bank_account_id   bigint,
                date_publication     timestamp,
                date_update          timestamp,
                active               bool,
                order_status         varchar(20)
            )
    LANGUAGE plpgsql
AS
$$
BEGIN
    DROP TABLE IF EXISTS result_table;
    CREATE TEMPORARY TABLE result_table
    (
        id                   integer,
        currency_to_buy      varchar(20),
        currency_to_sell     varchar(20),
        count_to_buy         float8,
        count_to_sell        float8,
        price                float8,
        owner_id             bigint,
        from_bank_account_id bigint,
        to_bank_account_id   bigint,
        date_publication     timestamp,
        date_update          timestamp,
        active               bool,
        order_status         varchar(20)
    );
    INSERT INTO result_table (id, currency_to_buy, currency_to_sell, count_to_buy, count_to_sell, price, owner_id, from_bank_account_id, to_bank_account_id, date_publication, date_update, active, order_status)
    SELECT os.id, os.currency_to_buy, os.currency_to_sell, os.count_to_buy, os.count_to_sell, os.min_price AS price, os.owner_id, os.from_bank_account_id, os.to_bank_account_id, os.date_publication, os.date_update, os.active, 'SELL' AS order_status FROM order_sell os;
    RETURN QUERY SELECT * FROM result_table;
END;
$$;

select * from find_sell_and_buy_order();
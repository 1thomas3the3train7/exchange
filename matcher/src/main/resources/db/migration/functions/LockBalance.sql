DROP FUNCTION IF EXISTS lock_balance(bank_account_id bigint, count float8);
CREATE OR REPLACE FUNCTION lock_balance(bank_account_id bigint, count float8)
    RETURNS TABLE
            (
                id       integer,
                currency varchar(20),
                balance  float8,
                owner_id bigint
            )
    LANGUAGE plpgsql
AS
$$
DECLARE
    founded_balance float8;
BEGIN
    SELECT ba.balance FROM bank_account AS ba WHERE id = bank_account_id INTO founded_balance;
    IF founded_balance >= count THEN
        RETURN QUERY UPDATE bank_account SET balance = founded_balance - count WHERE id = bank_account_id
            RETURNING id, currency, balance, owner_id;
    ELSE
        RETURN QUERY SELECT NULL::bigint  AS id,
                            NULL::varchar AS currency,
                            NULL::float8  AS balance,
                            NULL::bigint     owner_id;
    end if;
END;
$$;
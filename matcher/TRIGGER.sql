CREATE OR REPLACE FUNCTION check_currency() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.currencyToBuy <> (SELECT currency FROM bank_account WHERE id = NEW.from_bank_account_id) THEN
        RAISE EXCEPTION 'Currency in order does not match currency in bank account';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_currency_trigger
    BEFORE INSERT OR UPDATE ON order_buy
    FOR EACH ROW EXECUTE FUNCTION check_currency();
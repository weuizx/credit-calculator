CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS credits
(
    credit_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    ip_address VARCHAR(45) NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    payment_type VARCHAR(15) NOT NULL,
    amount FLOAT8 NOT NULL,
    rate FLOAT8 NOT NULL,
    term INTEGER NOT NULL,
    start_date TIMESTAMP NOT NULL

);

CREATE TABLE IF NOT EXISTS payments
(
    payment_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    credit_id UUID NOT NULL REFERENCES credits (credit_id) ON DELETE CASCADE,
    number INTEGER NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    monthly_payment FLOAT8 NOT NULL,
    debt_payment FLOAT8 NOT NULL,
    interest_payment FLOAT8 NOT NULL,
    debt_balance FLOAT8 NOT NULL
);


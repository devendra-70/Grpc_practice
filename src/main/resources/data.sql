-- Create table if it does not exist
CREATE TABLE IF NOT EXISTS stocks (
    id SERIAL PRIMARY KEY,
    stock_symbol VARCHAR(10) UNIQUE NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert example rows - skip if stock_symbol already exists
INSERT INTO stocks (stock_symbol, price)
VALUES ('AAPL', 175.50)
ON CONFLICT (stock_symbol) DO NOTHING;

INSERT INTO stocks (stock_symbol, price)
VALUES ('GOOGL', 2800.75)
ON CONFLICT (stock_symbol) DO NOTHING;

INSERT INTO stocks (stock_symbol, price)
VALUES ('AMZN', 3400.00)
ON CONFLICT (stock_symbol) DO NOTHING;

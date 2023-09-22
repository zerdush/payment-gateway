CREATE TABLE payment_request (
    id VARCHAR(36) NOT NULL,
    merchant_reference_id VARCHAR(36) NOT NULL,
    merchant_id VARCHAR(36) NOT NULL,
    credit_card_number VARCHAR(16) NOT NULL,
    expiry_month INT NOT NULL,
    expiry_year INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    cvv VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL,
    message VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT unique_merchant_request UNIQUE (merchant_reference_id, merchant_id),
    CHECK (status IN ('REQUESTED', 'SUCCESSFUL', 'FAILED'))
);

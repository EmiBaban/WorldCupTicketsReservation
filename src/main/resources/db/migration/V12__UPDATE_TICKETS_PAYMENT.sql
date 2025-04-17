SET search_path = project, pg_catalog;

CREATE TABLE project.payments (
                          id UUID PRIMARY KEY,
                          timestamp TIMESTAMP NOT NULL,
                          amount DOUBLE PRECISION NOT NULL,
                          payment_method TEXT,
                          is_paid BOOLEAN NOT NULL,
                          user_id UUID,
                          CONSTRAINT fk_payment_user FOREIGN KEY (user_id) REFERENCES users(id)
);

ALTER TABLE project.tickets
    DROP COLUMN is_paid,
    DROP COLUMN payment_method,
    ADD COLUMN payment_id UUID,
    ADD CONSTRAINT fk_ticket_payment FOREIGN KEY (payment_id) REFERENCES payments(id);

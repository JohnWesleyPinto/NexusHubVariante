-- V18: Create Password Reset Tokens table for secure single-use token password recovery
CREATE TABLE usr_password_reset_token (
    idtoken UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    iduser UUID NOT NULL,
    dstoken VARCHAR(100) NOT NULL UNIQUE,
    tsexpiration TIMESTAMP NOT NULL,
    flused BOOLEAN NOT NULL DEFAULT FALSE,
    tscreated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_password_reset_user FOREIGN KEY (iduser) REFERENCES sec_user (iduser) ON DELETE CASCADE
);

CREATE INDEX ix_password_reset_token ON usr_password_reset_token (dstoken);
CREATE INDEX ix_password_reset_user ON usr_password_reset_token (iduser);

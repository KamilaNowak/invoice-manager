CREATE DATABASE `invoicer`;

CREATE TABLE users(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    username VARCHAR(128) DEFAULT NULL,
    passwd VARCHAR(128) DEFAULT NULL,
    email VARCHAR(128) DEFAULT NULL,
    date_of_birth timestamp DEFAULT CURRENT_TIMESTAMP,
    createdAt timestamp DEFAULT CURRENT_TIMESTAMP
)

CREATE TABLE customers(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(256) DEFAULT NULL,
    surname VARCHAR(256) DEFAULT NULL,
    email VARCHAR(128) DEFAULT NULL,
    phone_number BIGINT DEFAULT NULL,
    address_id INT DEFAULT NULL,

    CONSTRAINT address_id_cfkey
        FOREIGN KEY(address_id)
            REFERENCES address_details(id)
                ON DELETE CASCADE
                    ON UPDATE CASCADE
)

CREATE TABLE companies(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    company_name VARCHAR(256) DEFAULT NULL,
    nip BIGINT DEFAULT NULL,
    address_id  INT DEFAULT NULL,
    owner_id INT DEFAULT NULL,

    CONSTRAINT address_id_fkey
        FOREIGN KEY (address_id)
            REFERENCES address_details(id)
                ON DELETE CASCADE
                    ON UPDATE CASCADE,

    CONSTRAINT owner_id_fkey
        FOREIGN KEY (owner_id)
            REFERENCES owners(id)
                 ON DELETE CASCADE
                    ON UPDATE CASCADE
)
CREATE TABLE owners(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(256) DEFAULT NULL,
    surname VARCHAR(256) DEFAULT NULL,
    email VARCHAR(128) DEFAULT NULL,
    pid INT DEFAULT NULL
)
CREATE TABLE address_details(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    country VARCHAR(128) DEFAULT NULL,
    city VARCHAR(128) DEFAULT NULL,
    street VARCHAR(128) DEFAULT NULL,
    building INTEGER DEFAULT NULL
)
CREATE TABLE item(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    description VARCHAR(512) DEFAULT NULL,
    invoice_id INTEGER DEFAULT NULL,

    CONSTRAINT invoice_id_fkey
        FOREIGN KEY(invoice_id)
            REFERENCES invoices(invoice_no)
                ON DELETE NO ACTION
                    ON UPDATE NO ACTION
)
CREATE TABLE company_invoices(
    invoice_no VARCHAR(128) NOT NULL PRIMARY KEY,
    date_of_issue timestamp DEFAULT CURRENT_TIMESTAMP,
    quantity INTEGER DEFAULT NULL,
    amount BIGINT DEFAULT NULL,
    payment_option VARCHAR(64) DEFAULT NULL,
    prepared_by INTEGER DEFAULT NULL,
    company_id INTEGER DEFAULT NULL,

    CONSTRAINT company_id_fkey
        FOREIGN KEY (company_id)
            REFERENCES companies(id)
                ON DELETE NO ACTION
                    ON UPDATE NO ACTION,

    CONSTRAINT preparedBy_id_fkey
        FOREIGN KEY(prepared_by)
            REFERENCES users(id)
                 ON DELETE NO ACTION
                    ON UPDATE NO ACTION,
)
CREATE TABLE personal_invoices(
    invoice_no VARCHAR(128) NOT NULL PRIMARY KEY,
    date_of_issue timestamp DEFAULT CURRENT_TIMESTAMP,
    quantity INTEGER DEFAULT NULL,
    amount BIGINT DEFAULT NULL,
    payment_method VARCHAR(64) DEFAULT NULL,
    customer_id INTEGER DEFAULT NULL,
    prepared_by INTEGER DEFAULT NULL,

    CONSTRAINT customer_id_fkey
        FOREIGN KEY(customer_id)
            REFERENCES customers(id)
                ON DELETE NO ACTION
                    ON UPDATE NO ACTION,

    CONSTRAINT preparedBy_id_fkey
        FOREIGN KEY(prepared_by)
            REFERENCES users(id)
                 ON DELETE NO ACTION
                    ON UPDATE NO ACTION,
)

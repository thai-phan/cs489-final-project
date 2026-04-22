DROP SCHEMA IF EXISTS schema1 CASCADE;
CREATE SCHEMA schema1;

-- 1. Dedicated Roles Table
CREATE TABLE roles
(
    role_id   SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL -- 'manager', 'dentist', 'patient'
);

-- 2. Updated Users Table
CREATE TABLE users
(
    user_id      SERIAL PRIMARY KEY,
    first_name   VARCHAR(50)         NOT NULL,
    last_name    VARCHAR(50)         NOT NULL,
    email        VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    -- Foreign key to the roles table
    role_id      INT                 NOT NULL REFERENCES roles (role_id),
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. Specialized Tables (Same as before, but linked via user_id)
CREATE TABLE managers
(
    user_id         INT PRIMARY KEY REFERENCES users (user_id) ON DELETE CASCADE,
    office_location VARCHAR(100)
);

CREATE TABLE dentists
(
    user_id           INT PRIMARY KEY REFERENCES users (user_id) ON DELETE CASCADE,
    dentist_id_number VARCHAR(20) UNIQUE NOT NULL,
    specialization    VARCHAR(100)       NOT NULL
);

CREATE TABLE patients
(
    user_id         INT PRIMARY KEY REFERENCES users (user_id) ON DELETE CASCADE,
    date_of_birth   DATE NOT NULL,
    mailing_address TEXT NOT NULL
);

CREATE TABLE schema1.surgeries
(
    surgery_id        SERIAL PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    location_address  VARCHAR(255) NOT NULL,
    telephone_number  VARCHAR(50) NOT NULL
);

CREATE TABLE schema1.appointment_statuses
(
    status_id   SERIAL PRIMARY KEY,
    status_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE schema1.appointments
(
    appointment_id       SERIAL PRIMARY KEY,
    appointment_date_time TIMESTAMP NOT NULL,
    patient_user_id      BIGINT NOT NULL REFERENCES schema1.users (user_id),
    dentist_user_id      BIGINT NOT NULL REFERENCES schema1.users (user_id),
    surgery_id           BIGINT NOT NULL REFERENCES schema1.surgeries (surgery_id),
    status_id            BIGINT NOT NULL REFERENCES schema1.appointment_statuses (status_id)
);

CREATE TABLE schema1.bills
(
    bill_id         SERIAL PRIMARY KEY,
    amount          NUMERIC(10,2) NOT NULL,
    paid            BOOLEAN NOT NULL,
    patient_user_id BIGINT NOT NULL REFERENCES schema1.users (user_id)
);

-- Seed data for schema1
-- Parent tables first, then dependent tables.

-- emty the tables before inserting data
-- DELETE FROM schema1.user_roles WHERE user_id IN (1, 2, 3, 4, 5);
-- DELETE FROM schema1.users WHERE user_id IN (1, 2, 3, 4, 5);
-- DELETE FROM schema1.roles WHERE role_id IN (1, 2, 3, 4, 5);
-- DELETE FROM schema1.bills   WHERE bill_id IN (1, 2, 3, 4, 5);
-- DELETE FROM schema1.appointments    WHERE appointment_id IN (1, 2, 3, 4, 5);
-- DELETE FROM schema1.addresses WHERE address_id IN (1, 2, 3, 4, 5);
-- DELETE FROM schema1.surgeries   WHERE surgery_id IN (1, 2, 3, 4, 5);
-- DELETE FROM schema1.dentists WHERE dentist_id IN (1, 2, 3, 4, 5);
-- DELETE FROM schema1.patients WHERE patient_id IN (1, 2, 3, 4, 5);



INSERT INTO schema1.patients (patient_id, first_name, last_name, contact_phone, email, date_of_birth) VALUES
    (1, 'John', 'Smith', '555-0101', 'john.smith@example.com', '1988-02-14'),
    (2, 'Emma', 'Johnson', '555-0102', 'emma.johnson@example.com', '1992-09-30'),
    (3, 'Liam', 'Davis', '555-0103', 'liam.davis@example.com', '1985-06-11'),
    (4, 'Olivia', 'Martinez', '555-0104', 'olivia.martinez@example.com', '1995-01-22'),
    (5, 'Noah', 'Wilson', '555-0105', 'noah.wilson@example.com', '1979-12-03');

INSERT INTO schema1.dentists (dentist_id, first_name, last_name, phone, email, specialization) VALUES
    (1, 'Alice', 'Brown', '555-0201', 'alice.brown@example.com', 'Orthodontics'),
    (2, 'Michael', 'Nguyen', '555-0202', 'michael.nguyen@example.com', 'Endodontics'),
    (3, 'Sophia', 'Taylor', '555-0203', 'sophia.taylor@example.com', 'Prosthodontics'),
    (4, 'Ethan', 'Harris', '555-0204', 'ethan.harris@example.com', 'Pediatric Dentistry'),
    (5, 'Ava', 'Lopez', '555-0205', 'ava.lopez@example.com', 'Periodontics');

INSERT INTO schema1.surgeries (surgery_id, name, location_address, telephone_number) VALUES
    (1, 'Main Street Dental Surgery', '100 Main Street, Boston', '555-0301'),
    (2, 'North Clinic Surgery', '25 North Avenue, Cambridge', '555-0302'),
    (3, 'Harbor Dental Care', '14 Harbor Road, Quincy', '555-0303'),
    (4, 'Lakeside Family Surgery', '78 Lake Street, Somerville', '555-0304'),
    (5, 'Downtown Smile Studio', '220 Center Plaza, Salem', '555-0305');

INSERT INTO schema1.addresses (address_id, patient_id, mailing_address, city) VALUES
    (1, 1, '12 Oak Lane, Boston, MA 02110', 'Boston'),
    (2, 2, '88 River Road, Cambridge, MA 02139', 'Cambridge'),
    (3, 3, '45 Pine Street, Quincy, MA 02169', 'Quincy'),
    (4, 4, '9 Maple Avenue, Somerville, MA 02144', 'Somerville'),
    (5, 5, '300 Elm Road, Salem, MA 01970', 'Salem');

INSERT INTO schema1.appointments (appointment_id, appointment_date_time, dentist_id, patient_id, surgery_id, status) VALUES
    (1, '2026-04-20 09:00:00', 1, 1, 1, 'SCHEDULED'),
    (2, '2026-04-20 10:30:00', 2, 2, 2, 'COMPLETED'),
    (3, '2026-04-21 11:15:00', 3, 3, 3, 'SCHEDULED'),
    (4, '2026-04-21 13:00:00', 4, 4, 4, 'CANCELLED'),
    (5, '2026-04-22 14:45:00', 5, 5, 5, 'COMPLETED');

INSERT INTO schema1.bills (bill_id, amount, paid, patient_id) VALUES
    (1, 180.00, FALSE, 1),
    (2, 95.50, TRUE, 2),
    (3, 220.75, FALSE, 3),
    (4, 140.00, TRUE, 4),
    (5, 310.25, FALSE, 5);

INSERT INTO schema1.roles (role_id, name) VALUES
    (1, 'ADMIN'),
    (2, 'STAFF'),
    (3, 'DENTIST'),
    (4, 'HYGIENIST'),
    (5, 'PATIENT');

INSERT INTO schema1.users (user_id, username, password, enabled) VALUES
    (1, 'admin', '{noop}admin123', TRUE),
    (2, 'staff', '{noop}staff123', TRUE),
    (3, 'dentist', '{noop}dentist123', TRUE),
    (4, 'hygienist', '{noop}hygienist123', TRUE),
    (5, 'patient', '{noop}patient123', TRUE);

INSERT INTO schema1.user_roles (user_id, role_id) VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5);



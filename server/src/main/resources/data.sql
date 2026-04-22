-- Seed data for schema1


-- clear all tables first to avoid conflicts with existing data
DELETE FROM schema1.bills;
DELETE FROM schema1.appointments;
DELETE FROM schema1.appointment_statuses;
DELETE FROM schema1.surgeries;
DELETE FROM schema1.patients;
DELETE FROM schema1.dentists;
DELETE FROM schema1.managers;
DELETE FROM schema1.users;
DELETE FROM schema1.roles;

INSERT INTO schema1.roles (role_id, role_name)
VALUES (1, 'MANAGER'),
       (2, 'DENTIST'),
       (3, 'PATIENT');

INSERT INTO schema1.users (user_id, first_name, last_name, email, phone_number, password, enabled, role_id)
VALUES (1, 'Admin', 'User', 'admin@example.com', '555-0001', '{noop}admin123', TRUE, 1),
       (2, 'Alice', 'Brown', 'alice.brown@example.com', '555-0201', '{noop}dentist123', TRUE, 2),
       (3, 'Michael', 'Nguyen', 'dentist1@example.com', '555-0202', '{noop}dentist123', TRUE, 2),
       (4, 'Sophia', 'Taylor', 'dentist2@example.com', '555-0203', '{noop}dentist123', TRUE, 2),
       (5, 'Ethan', 'Harris', 'ethan.harris@example.com', '555-0204', '{noop}dentist123', TRUE, 2),
       (6, 'Ava', 'Lopez', 'ava.lopez@example.com', '555-0205', '{noop}dentist123', TRUE, 2),
       (7, 'John', 'Smith', 'john.smith@example.com', '555-0101', '{noop}patient123', TRUE, 3),
       (8, 'Emma', 'Johnson', 'patient1@example.com', '555-0102', '{noop}patient123', TRUE, 3),
       (9, 'Liam', 'Davis', 'patient2@example.com', '555-0103', '{noop}patient123', TRUE, 3),
       (10, 'Olivia', 'Martinez', 'olivia.martinez@example.com', '555-0104', '{noop}patient123', TRUE, 3),
       (11, 'Noah', 'Wilson', 'noah.wilson@example.com', '555-0105', '{noop}patient123', TRUE, 3);

INSERT INTO schema1.dentists (user_id, dentist_id_number, specialization)
VALUES (2, 'D001', 'Orthodontics'),
       (3, 'D002', 'Endodontics'),
       (4, 'D003', 'Prosthodontics'),
       (5, 'D004', 'Pediatric Dentistry'),
       (6, 'D005', 'Periodontics');

INSERT INTO schema1.patients (user_id, date_of_birth, mailing_address)
VALUES (7, '1988-02-14', '12 Oak Lane, Boston, MA 02110'),
       (8, '1992-09-30', '88 River Road, Cambridge, MA 02139'),
       (9, '1985-06-11', '45 Pine Street, Quincy, MA 02169'),
       (10, '1995-01-22', '9 Maple Avenue, Somerville, MA 02144'),
       (11, '1979-12-03', '300 Elm Road, Salem, MA 01970');

-- seed 3 managers
INSERT INTO schema1.managers (user_id, office_location)
VALUES (1, 'Head Office, Boston');

INSERT INTO schema1.appointment_statuses (status_id, status_name)
VALUES (1, 'PENDING'),
       (2, 'SCHEDULED'),
       (3, 'COMPLETED'),
       (4, 'CANCELLED');

-- from table.sql: seed data for appointments


INSERT INTO schema1.surgeries (surgery_id, name, location_address, telephone_number)
VALUES (1, 'Main Street Dental Surgery', '100 Main Street, Boston', '555-0301'),
       (2, 'North Clinic Surgery', '25 North Avenue, Cambridge', '555-0302'),
       (3, 'Harbor Dental Care', '14 Harbor Road, Quincy', '555-0303'),
       (4, 'Lakeside Family Surgery', '78 Lake Street, Somerville', '555-0304'),
       (5, 'Downtown Smile Studio', '220 Center Plaza, Salem', '555-0305');

INSERT INTO schema1.appointments (appointment_id, patient_user_id, dentist_user_id, appointment_date_time, surgery_id,
                                  status_id)
VALUES (1, 7, 2, '2024-07-01 10:00:00', 1, 2),
       (2, 8, 3, '2024-07-02 11:30:00', 2, 2),
       (3, 9, 4, '2024-07-03 14:00:00', 3, 2),
       (4, 10, 5, '2024-07-04 09:00:00', 4, 2),
       (5, 11, 6, '2024-07-05 15:30:00', 5, 2);

INSERT INTO schema1.bills (bill_id, amount, paid, patient_user_id)
VALUES (1, 180.00, FALSE, 7),
       (2, 95.50, TRUE, 8),
       (3, 220.75, FALSE, 9),
       (4, 140.00, TRUE, 10),
       (5, 310.25, FALSE, 11);

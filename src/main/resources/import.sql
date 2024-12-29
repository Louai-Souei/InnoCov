-- Insérer 10 utilisateurs dans la table user avec des données uniques
INSERT INTO user (id, phone, firstname, lastname, email, password, occupation, role) VALUES (1, '12312345', 'Jean', 'Dupont', 'user1@email.com', '$2a$10$wae0VqErlj5hCl8nE8XUBOElXu91rbYEqMZWovQ5JJeClPq3exuni', 'TEACHER', 'ADMIN');
INSERT INTO user (id, phone, firstname, lastname, email, password, occupation, role) VALUES (2, '12312346', 'Marie', 'Curie', 'user2@email.com', '$2a$10$wae0VqErlj5hCl8nE8XUBOElXu91rbYEqMZWovQ5JJeClPq3exuni', 'STUDENT', 'PASSENGER');
INSERT INTO user (id, phone, firstname, lastname, email, password, occupation, role) VALUES (3, '12312347', 'Paul', 'Durand', 'user3@email.com', '$2a$10$wae0VqErlj5hCl8nE8XUBOElXu91rbYEqMZWovQ5JJeClPq3exuni', 'STAFF', 'DRIVER');
INSERT INTO user (id, phone, firstname, lastname, email, password, occupation, role) VALUES (4, '12312348', 'Alice', 'Martin', 'user4@email.com', '$2a$10$wae0VqErlj5hCl8nE8XUBOElXu91rbYEqMZWovQ5JJeClPq3exuni', 'TEACHER', 'PASSENGER');
INSERT INTO user (id, phone, firstname, lastname, email, password, occupation, role) VALUES (5, '12312349', 'Lucas', 'Bernard', 'user5@email.com', '$2a$10$wae0VqErlj5hCl8nE8XUBOElXu91rbYEqMZWovQ5JJeClPq3exuni', 'STUDENT', 'ADMIN');
INSERT INTO user (id, phone, firstname, lastname, email, password, occupation, role) VALUES (6, '12312350', 'Emma', 'Blanc', 'user6@email.com', '$2a$10$wae0VqErlj5hCl8nE8XUBOElXu91rbYEqMZWovQ5JJeClPq3exuni', 'STAFF', 'DRIVER');
INSERT INTO user (id, phone, firstname, lastname, email, password, occupation, role) VALUES (7, '12312351', 'David', 'Moreau', 'user7@email.com', '$2a$10$wae0VqErlj5hCl8nE8XUBOElXu91rbYEqMZWovQ5JJeClPq3exuni', 'TEACHER', 'ADMIN');
INSERT INTO user (id, phone, firstname, lastname, email, password, occupation, role) VALUES (8, '12312352', 'Sophie', 'Roux', 'user8@email.com', '$2a$10$wae0VqErlj5hCl8nE8XUBOElXu91rbYEqMZWovQ5JJeClPq3exuni', 'STUDENT', 'PASSENGER');
INSERT INTO user (id, phone, firstname, lastname, email, password, occupation, role) VALUES (9, '12312353', 'Antoine', 'Petit', 'user9@email.com', '$2a$10$wae0VqErlj5hCl8nE8XUBOElXu91rbYEqMZWovQ5JJeClPq3exuni', 'STAFF', 'DRIVER');
INSERT INTO user (id, phone, firstname, lastname, email, password, occupation, role) VALUES (10, '12312354', 'Julie', 'Fabre', 'user10@email.com', '$2a$10$wae0VqErlj5hCl8nE8XUBOElXu91rbYEqMZWovQ5JJeClPq3exuni', 'TEACHER', 'PASSENGER');


-- Insérer 5 routes dans la table route
INSERT INTO route (id, departure, arrival, departure_date, number_of_passengers, driver_id, created_at, updated_at) VALUES (1, 'Paris', 'Lyon', '2024-01-05 08:00:00', 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO route (id, departure, arrival, departure_date, number_of_passengers, driver_id, created_at, updated_at) VALUES (2, 'Marseille', 'Nice', '2024-01-10 09:30:00', 4, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO route (id, departure, arrival, departure_date, number_of_passengers, driver_id, created_at, updated_at) VALUES (3, 'Bordeaux', 'Toulouse', '2024-01-15 14:00:00', 2, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO route (id, departure, arrival, departure_date, number_of_passengers, driver_id, created_at, updated_at) VALUES (4, 'Lille', 'Bruxelles', '2024-01-20 07:45:00', 3, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO route (id, departure, arrival, departure_date, number_of_passengers, driver_id, created_at, updated_at) VALUES (5, 'Strasbourg', 'Mulhouse', '2024-01-25 12:15:00', 1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- Insérer des complaints
INSERT INTO complaint (id, description, created_at, complainer_id, target_user_id, resolved) VALUES (1, 'The driver arrived late.', CURRENT_TIMESTAMP, 1, 2, false);
INSERT INTO complaint (id, description, created_at, complainer_id, target_user_id, resolved) VALUES (2, 'The vehicle was not clean.', CURRENT_TIMESTAMP, 1, 3, false);
INSERT INTO complaint (id, description, created_at, complainer_id, target_user_id, resolved) VALUES (3, 'The passenger was noisy during the trip.', CURRENT_TIMESTAMP, 2, 1, false);
INSERT INTO complaint (id, description, created_at, complainer_id, target_user_id, resolved) VALUES (4, 'The driver did not follow the agreed route.', CURRENT_TIMESTAMP, 2, 3, true);
INSERT INTO complaint (id, description, created_at, complainer_id, target_user_id, resolved) VALUES (5, 'The driver was rude.', CURRENT_TIMESTAMP, 3, 1, false);
INSERT INTO complaint (id, description, created_at, complainer_id, target_user_id, resolved) VALUES (6, 'The passenger did not show up.', CURRENT_TIMESTAMP, 3, 2, true);
INSERT INTO complaint (id, description, created_at, complainer_id, target_user_id, resolved) VALUES (7, 'The driver overcharged for the trip.', CURRENT_TIMESTAMP, 4, 5, false);
INSERT INTO complaint (id, description, created_at, complainer_id, target_user_id, resolved) VALUES (8, 'The passenger brought extra luggage without prior notice.', CURRENT_TIMESTAMP, 5, 4, true);


-- Insérer des réservations de routes
INSERT INTO route_booking (id, passenger_id, route_id, booking_date) VALUES (1, 2, 1, CURRENT_TIMESTAMP);
INSERT INTO route_booking (id, passenger_id, route_id, booking_date) VALUES (2, 3, 1, CURRENT_TIMESTAMP);
INSERT INTO route_booking (id, passenger_id, route_id, booking_date) VALUES (3, 4, 2, CURRENT_TIMESTAMP);
INSERT INTO route_booking (id, passenger_id, route_id, booking_date) VALUES (4, 5, 2, CURRENT_TIMESTAMP);
INSERT INTO route_booking (id, passenger_id, route_id, booking_date) VALUES (5, 6, 3, CURRENT_TIMESTAMP);
INSERT INTO route_booking (id, passenger_id, route_id, booking_date) VALUES (6, 7, 3, CURRENT_TIMESTAMP);
INSERT INTO route_booking (id, passenger_id, route_id, booking_date) VALUES (7, 8, 4, CURRENT_TIMESTAMP);
INSERT INTO route_booking (id, passenger_id, route_id, booking_date) VALUES (8, 9, 4, CURRENT_TIMESTAMP);
INSERT INTO route_booking (id, passenger_id, route_id, booking_date) VALUES (9, 10, 5, CURRENT_TIMESTAMP);
INSERT INTO route_booking (id, passenger_id, route_id, booking_date) VALUES (10, 1, 5, CURRENT_TIMESTAMP);

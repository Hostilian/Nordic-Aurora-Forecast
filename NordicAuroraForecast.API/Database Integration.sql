CREATE DATABASE nordic_aurora;
USE nordic_aurora;

CREATE TABLE locations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8)
);

CREATE TABLE predictions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    location_id INT,
    prediction_date DATE,
    aurora_probability DECIMAL(5, 2),
    FOREIGN KEY (location_id) REFERENCES locations(id)
);
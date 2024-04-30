
DROP TABLE IF EXISTS phones;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created TIMESTAMP,
    modified TIMESTAMP,
    last_login TIMESTAMP,
    token VARCHAR(255),
    is_active BOOLEAN,
    role VARCHAR(10)
);

CREATE TABLE phones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(255) NOT NULL,
    city_code VARCHAR(255) NOT NULL,
    country_code VARCHAR(255) NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);



INSERT INTO users (name, email, password, created, modified, last_login, token, is_active, role)
VALUES
    ('Juan Rodriguez', 'juan@rodriguez.org', 'hunter2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'token123', true,'admin'),
    ('Maria Lopez', 'maria@lopez.org', 'password', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'token456', true,'admin'),
    ('Carlos Sanchez', 'carlos@sanchez.org', 'securepass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'token789', true,'admin'),
    ('Laura Gomez', 'laura@gomez.org', 'laurapass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'token890', true,'admin'),
    ('Pedro Perez', 'pedro@perez.org', 'pedropass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'token567', true,'admin'),
    ('Ana Martinez', 'ana@martinez.org', 'anapass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'token432', true,'admin'),
    ('Sofia Garcia', 'sofia@garcia.org', 'sofiapass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'token654', true,'admin'),
    ('David Fernandez', 'david@fernandez.org', 'davidpass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'token321', true,'admin'),
    ('Elena Torres', 'elena@torres.org', 'elenapass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'token098', true,'admin'),
    ('Pablo Ruiz', 'pablo@ruiz.org', 'pablopas', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'token234', true,'admin');

INSERT INTO phones (number, city_code, country_code, user_id)
VALUES
    ('1234567', '1', '57', 1),
    ('9876543', '2', '57', 1),
    ('1111111', '3', '57', 2),
    ('2222222', '4', '57', 2),
    ('3333333', '5', '57', 3),
    ('4444444', '6', '57', 4),
    ('5555555', '7', '57', 5),
    ('6666666', '8', '57', 6),
    ('7777777', '9', '57', 7),
    ('8888888', '10', '57', 8);


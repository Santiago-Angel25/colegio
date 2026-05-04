-- PostgreSQL
-- Crea la base desde pgAdmin o consola:
-- CREATE DATABASE parcial;

-- La aplicacion crea las tablas con JPA (spring.jpa.hibernate.ddl-auto=update).
-- Ejecuta primero la app una vez y luego corre estos inserts en la base parcial.

INSERT INTO roles (name)
VALUES
    ('RECTOR'),
    ('DOCENTE'),
    ('ESTUDIANTE')
ON CONFLICT (name) DO NOTHING;

-- Usuario rector:
-- usuario: rector
-- clave: 1234
INSERT INTO users (username, password, enabled)
VALUES ('rector', '{noop}1234', true)
ON CONFLICT (username) DO UPDATE
SET password = EXCLUDED.password,
    enabled = EXCLUDED.enabled;

-- Docentes:
-- El usuario es el nombre junto y la clave es igual al usuario.
INSERT INTO users (username, password, enabled)
VALUES
    ('anagomez', '{noop}anagomez', true),
    ('carlosruiz', '{noop}carlosruiz', true),
    ('lauramora', '{noop}lauramora', true),
    ('pedrosilva', '{noop}pedrosilva', true),
    ('mariatorres', '{noop}mariatorres', true)
ON CONFLICT (username) DO UPDATE
SET password = EXCLUDED.password,
    enabled = EXCLUDED.enabled;

-- Estudiantes:
-- El usuario es el nombre junto y la clave es igual al usuario.
INSERT INTO users (username, password, enabled)
VALUES
    ('juanperez', '{noop}juanperez', true),
    ('sofiacastro', '{noop}sofiacastro', true),
    ('andreslopez', '{noop}andreslopez', true),
    ('valeriarojas', '{noop}valeriarojas', true),
    ('nicolasmendez', '{noop}nicolasmendez', true)
ON CONFLICT (username) DO UPDATE
SET password = EXCLUDED.password,
    enabled = EXCLUDED.enabled;

INSERT INTO users_roles (user_id, role_id)
VALUES
    ((SELECT user_id FROM users WHERE username = 'rector'), (SELECT role_id FROM roles WHERE name = 'RECTOR')),
    ((SELECT user_id FROM users WHERE username = 'anagomez'), (SELECT role_id FROM roles WHERE name = 'DOCENTE')),
    ((SELECT user_id FROM users WHERE username = 'carlosruiz'), (SELECT role_id FROM roles WHERE name = 'DOCENTE')),
    ((SELECT user_id FROM users WHERE username = 'lauramora'), (SELECT role_id FROM roles WHERE name = 'DOCENTE')),
    ((SELECT user_id FROM users WHERE username = 'pedrosilva'), (SELECT role_id FROM roles WHERE name = 'DOCENTE')),
    ((SELECT user_id FROM users WHERE username = 'mariatorres'), (SELECT role_id FROM roles WHERE name = 'DOCENTE')),
    ((SELECT user_id FROM users WHERE username = 'juanperez'), (SELECT role_id FROM roles WHERE name = 'ESTUDIANTE')),
    ((SELECT user_id FROM users WHERE username = 'sofiacastro'), (SELECT role_id FROM roles WHERE name = 'ESTUDIANTE')),
    ((SELECT user_id FROM users WHERE username = 'andreslopez'), (SELECT role_id FROM roles WHERE name = 'ESTUDIANTE')),
    ((SELECT user_id FROM users WHERE username = 'valeriarojas'), (SELECT role_id FROM roles WHERE name = 'ESTUDIANTE')),
    ((SELECT user_id FROM users WHERE username = 'nicolasmendez'), (SELECT role_id FROM roles WHERE name = 'ESTUDIANTE'))
ON CONFLICT DO NOTHING;

INSERT INTO docente (nombre, usuario_id)
VALUES
    ('Ana Gomez', (SELECT user_id FROM users WHERE username = 'anagomez')),
    ('Carlos Ruiz', (SELECT user_id FROM users WHERE username = 'carlosruiz')),
    ('Laura Mora', (SELECT user_id FROM users WHERE username = 'lauramora')),
    ('Pedro Silva', (SELECT user_id FROM users WHERE username = 'pedrosilva')),
    ('Maria Torres', (SELECT user_id FROM users WHERE username = 'mariatorres'));

INSERT INTO estudiante (nombre, salon, usuario_id)
VALUES
    ('Juan Perez', 101, (SELECT user_id FROM users WHERE username = 'juanperez')),
    ('Sofia Castro', 203, (SELECT user_id FROM users WHERE username = 'sofiacastro')),
    ('Andres Lopez', 302, (SELECT user_id FROM users WHERE username = 'andreslopez')),
    ('Valeria Rojas', 401, (SELECT user_id FROM users WHERE username = 'valeriarojas')),
    ('Nicolas Mendez', 503, (SELECT user_id FROM users WHERE username = 'nicolasmendez'));

INSERT INTO asignatura (nombre, descripcion, salon, dia_semana, hora_inicio, hora_fin, docente_id)
VALUES
    ('Matematicas', 'Clase de matematicas', 101, 'LUNES', '07:00:00', '08:00:00',
     (SELECT id FROM docente WHERE nombre = 'Ana Gomez')),
    ('Ingles', 'Clase de ingles', 203, 'MARTES', '08:00:00', '09:00:00',
     (SELECT id FROM docente WHERE nombre = 'Carlos Ruiz')),
    ('Ciencias', 'Clase de ciencias', 302, 'MIERCOLES', '07:00:00', '08:00:00',
     (SELECT id FROM docente WHERE nombre = 'Laura Mora')),
    ('Sociales', 'Clase de sociales', 401, 'JUEVES', '09:00:00', '10:00:00',
     (SELECT id FROM docente WHERE nombre = 'Pedro Silva')),
    ('Lectura', 'Clase de lectura', 503, 'VIERNES', '10:00:00', '11:00:00',
     (SELECT id FROM docente WHERE nombre = 'Maria Torres'));

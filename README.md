usuarios para probar 
Rector:
rector / 1234

Docentes:
anagomez / anagomez
carlosruiz / carlosruiz
lauramora / lauramora
pedrosilva / pedrosilva
mariatorres / mariatorres

Estudiantes:
juanperez / juanperez
sofiacastro / sofiacastro
andreslopez / andreslopez
valeriarojas / valeriarojas
nicolasmendez / nicolasmendez

crear la base de datos antes de iniciar el parcial 
-- PostgreSQL
-- Crear la base de datos:
-- CREATE DATABASE parcial;

INSERT INTO roles (name)
VALUES
    ('RECTOR'),
    ('DOCENTE'),
    ('ESTUDIANTE')
ON CONFLICT (name) DO NOTHING;

INSERT INTO users (username, password, enabled)
VALUES ('rector', '{noop}1234', true)
ON CONFLICT (username) DO UPDATE
SET password = EXCLUDED.password,
    enabled = EXCLUDED.enabled;

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
    ('Sofia Castro', 101, (SELECT user_id FROM users WHERE username = 'sofiacastro')),
    ('Andres Lopez', 102, (SELECT user_id FROM users WHERE username = 'andreslopez')),
    ('Valeria Rojas', 102, (SELECT user_id FROM users WHERE username = 'valeriarojas')),
    ('Nicolas Mendez', 103, (SELECT user_id FROM users WHERE username = 'nicolasmendez'));

INSERT INTO asignatura (nombre, descripcion, salon, dia_semana, hora_inicio, hora_fin, docente_id)
VALUES
    ('Matematicas', 'Clase de matematicas', 101, 'LUNES', '07:00:00', '08:00:00',
     (SELECT id FROM docente WHERE nombre = 'Ana Gomez')),
    ('Ingles', 'Clase de ingles', 101, 'MARTES', '08:00:00', '09:00:00',
     (SELECT id FROM docente WHERE nombre = 'Carlos Ruiz')),
    ('Ingenieria Software', 'Clase de software', 101, 'MIERCOLES', '06:00:00', '08:00:00',
     (SELECT id FROM docente WHERE nombre = 'Laura Mora')),
    ('Ciencias', 'Clase de ciencias', 102, 'MIERCOLES', '07:00:00', '08:00:00',
     (SELECT id FROM docente WHERE nombre = 'Pedro Silva')),
    ('Sociales', 'Clase de sociales', 102, 'JUEVES', '09:00:00', '10:00:00',
     (SELECT id FROM docente WHERE nombre = 'Maria Torres')),
    ('Lectura', 'Clase de lectura', 103, 'VIERNES', '10:00:00', '11:00:00',
     (SELECT id FROM docente WHERE nombre = 'Ana Gomez'));


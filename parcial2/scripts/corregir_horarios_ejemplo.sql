-- Este script limpia SOLO asignaturas de ejemplo y las vuelve a crear sin cruces.
-- Si ya creaste asignaturas importantes desde la app, revisa antes de ejecutarlo.

DELETE FROM asignatura
WHERE lower(nombre) IN (
    'matematicas',
    'ingles',
    'ciencias',
    'sociales',
    'lectura',
    'ingeneria de software',
    'ingenieria de software',
    'ingenieria software'
);

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

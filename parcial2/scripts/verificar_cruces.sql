-- Cruces por salon: dos clases en el mismo salon, mismo dia y horarios superpuestos.
SELECT
    a1.id AS id_clase_1,
    a1.nombre AS clase_1,
    a1.salon,
    a1.dia_semana,
    a1.hora_inicio AS inicio_1,
    a1.hora_fin AS fin_1,
    a2.id AS id_clase_2,
    a2.nombre AS clase_2,
    a2.hora_inicio AS inicio_2,
    a2.hora_fin AS fin_2
FROM asignatura a1
JOIN asignatura a2
    ON a1.id < a2.id
    AND a1.salon = a2.salon
    AND a1.dia_semana = a2.dia_semana
    AND a1.hora_inicio < a2.hora_fin
    AND a2.hora_inicio < a1.hora_fin
ORDER BY a1.salon, a1.dia_semana, a1.hora_inicio;

-- Cruces por docente: un mismo docente con dos clases el mismo dia y en horarios superpuestos.
SELECT
    d.nombre AS docente,
    a1.id AS id_clase_1,
    a1.nombre AS clase_1,
    a1.dia_semana,
    a1.hora_inicio AS inicio_1,
    a1.hora_fin AS fin_1,
    a2.id AS id_clase_2,
    a2.nombre AS clase_2,
    a2.hora_inicio AS inicio_2,
    a2.hora_fin AS fin_2
FROM asignatura a1
JOIN asignatura a2
    ON a1.id < a2.id
    AND a1.docente_id = a2.docente_id
    AND a1.dia_semana = a2.dia_semana
    AND a1.hora_inicio < a2.hora_fin
    AND a2.hora_inicio < a1.hora_fin
JOIN docente d ON d.id = a1.docente_id
ORDER BY d.nombre, a1.dia_semana, a1.hora_inicio;

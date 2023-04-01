-- view with for reports
CREATE OR REPLACE VIEW VGBC_QUANTITY_MONTH_REPORT AS
SELECT qv.id_unit_set
           || qv.id_node
           || qv.id_material
           || TRUNC(qv.date_from, 'MM')
           || qv.io                        AS ID, --iba rep hibernate
       qv.id_unit_set                      AS ID_UNIT_SET,
       qv.id_node                          AS ID_NODE,
       (SELECT n.ID
        FROM GBC_NODE n
                 LEFT JOIN GBC_NODE_LEVEL nl ON n.ID_NODE_LEVEL = nl.ID
        WHERE nl.NODE_LEVEL = 2
        CONNECT BY n.ID = prior n.ID_NODE
        START WITH n.ID = qv.id_node)   AS ID_SUPER_PLANT,
       (SELECT n.ID
        FROM GBC_NODE n
                 LEFT JOIN GBC_NODE_LEVEL nl ON n.ID_NODE_LEVEL = nl.ID
        WHERE nl.NODE_LEVEL = 3
        CONNECT BY n.ID = prior n.ID_NODE
        START WITH n.ID = qv.id_node)   AS ID_PLANT,
       qv.id_material                      AS ID_MATERIAL,
       qv.id_material_node                 AS ID_MATERIAL_NODE,
       TRUNC(qv.date_from, 'MM')           AS MONTH,
       SUM(qv.quantity)                    AS MONTH_QUANTITY,
       qv.id_unit_to                       AS ID_UNIT,
       qv.unit_to_abbr                     AS UNIT_ABBR,
       qv.io                               AS IO,
       COUNT(*)                         AS NO_OF_ROWS,
       n.ID_COUNTRY                     AS ID_COUNTRY,
       c.ID_REGION                      AS ID_REGION
FROM VGBC_QUANTITY qv
         LEFT JOIN GBC_NODE n
     ON n.ID = qv.ID_NODE
         LEFT JOIN GBC_COUNTRY c
     ON n.ID_COUNTRY = c.ID
GROUP BY qv.id_unit_set
             || qv.id_node
             || qv.id_material
             || TRUNC(qv.date_from, 'MM')
             || qv.io, c.ID_REGION,  n.ID_COUNTRY,  qv.id_unit_set, qv.id_node, qv.id_material, qv.id_material_node, TRUNC(qv.date_from, 'MM'), qv.id_unit_to,
         qv.unit_to_abbr, qv.io;
COMMIT;

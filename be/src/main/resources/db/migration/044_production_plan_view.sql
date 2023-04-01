CREATE OR REPLACE VIEW VGBC_PRODUCTION_PLAN_TRANSLATED AS
SELECT
         l.ID                           as ID_LANGUAGE,
         l.ID || '-' || pp.ID           as ID_VIEW,
         pp.id                          as ID,
         mp.id_node                     as ID_NODE,
         NVL2(mp.id_node, l.ID || '-' || mp.id_node, NULL)  as ID_NODE_VIEW,
         m.id                           as ID_MATERIAL,
         NVL2(m.id, l.ID || '-' || m.id, NULL)  as ID_MATERIAL_VIEW,
         mp.id                          as ID_MATERIAL_NODE,
         NVL2(mp.id, l.ID || '-' || mp.id, NULL)  as ID_MATERIAL_NODE_VIEW,
         pp.month                       as MONTH,
         us.id                          as ID_UNIT_SET,
         NVL2(us.id, l.ID || '-' || us.id, NULL)  as ID_UNIT_SET_VIEW,
         unit_from.id                   as ID_UNIT_FROM,
         unit_to.id                     as ID_UNIT_TO,
         (  unit_to.k / unit_from.k * pp.quantity
                + unit_to.q
             - unit_from.q * (unit_to.k / unit_from.k))
                                        as QUANTITY,
         unit_to.abbr                   as UNIT_TO_ABBR,
         pp.memo                        as MEMO,
         pp.created                     as CREATED,
         pp.created_by                  as CREATED_BY,
         pp.modified                    as MODIFIED,
         pp.modified_by                 as MODIFIED_BY,
         pp.obj_version                 as OBJ_VERSION
FROM                        gbc_production_plan pp
                                CROSS JOIN
                            GBC_LANGUAGE l
                                CROSS JOIN
                            gbc_unit_set us
                                INNER JOIN
                            gbc_unit unit_from
                            ON unit_from.id = pp.id_unit
                                LEFT OUTER JOIN
                            gbc_material_node mp
                            ON mp.id = pp.id_material_node
                                LEFT OUTER JOIN
                            gbc_material m
                            ON m.id = mp.id_material
                                LEFT OUTER JOIN
                            gbc_material m2
                            ON m2.id = mp.id_material
                                LEFT OUTER JOIN
                            GBC_UNIT_SET_SETTINGS uss_to
                            ON uss_to.id_unit_set = us.id
                                AND (uss_to.id_material = NVL (m.id, m2.id))
                                LEFT OUTER JOIN
                            gbc_unit unit_to
                            ON unit_to.id = uss_to.id_unit
;

commit;

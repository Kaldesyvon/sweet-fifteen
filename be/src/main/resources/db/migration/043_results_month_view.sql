-- view for reports
create or replace view VGBC_RESULTS_MONTH_REPORT as
SELECT (q.id_unit_set
    || q.id_node
    || q.id_material
    || TRUNC(date_from, 'MM')
    || a.id_analysis_param
    || s.id)                         as id,
       q.id_unit_set                 as id_unit_set,
       q.id_node                     as id_node,
       (SELECT n.ID
        FROM GBC_NODE n
                 LEFT JOIN GBC_NODE_LEVEL nl ON n.ID_NODE_LEVEL = nl.ID
        WHERE nl.NODE_LEVEL = 2
        CONNECT BY n.ID = prior n.ID_NODE
        START WITH n.ID = q.id_node) AS ID_SUPER_PLANT,
       (SELECT n.ID
        FROM GBC_NODE n
                 LEFT JOIN GBC_NODE_LEVEL nl ON n.ID_NODE_LEVEL = nl.ID
        WHERE nl.NODE_LEVEL = 3
        CONNECT BY n.ID = prior n.ID_NODE
        START WITH n.ID = q.id_node) AS ID_PLANT,
       q.id_material                 as id_material,
       smn.id_material_node          as id_material_node,
       a.id_analysis_param           as id_analysis_param,
       s.id                          as id_scope,
       SUM(q.quantity)               as quantity,
       q.id_unit_to                  as id_unit,
       q.unit_to_abbr                as unit_abbr,
       SUM(DECODE(
               ut.compute,
               0,
               NULL,
               ((a.factor_a * smn.FACTOR_A
                   + NVL(a.factor_b, 0) * NVL(smn.FACTOR_B, 0))
                   * q.quantity
                   * q.IO
                   * smn.USE_CALCULATION)
           ))
                                     as analytical_value,
       a.id_unit_numen_to            as id_unit_numen,
       a.id_unit_denom_to            as id_unit_denom,
       a.unit_numen_abbr_to          as unit_abbr_av,
       TRUNC(date_from, 'MM')        as MONTH,
       COUNT(*)                      as no_of_rows,
       q.NODE_REPORT_POS             as NODE_REPORT_POS,
       a.ANALYSIS_PARAM_NAME_K       as ANALYSIS_PARAM_NAME_K,
       q.MATERIAL_NAME               as MATERIAL_NAME_K,
       q.PLANT_NAME                  as NODE_NAME_K,
       s.name_k                      as scope_name_k,
       q.io                          as io,
       nts.ID_NODE_TYPE              as id_node_type,
       c.ID_REGION                   as id_region,
       n.ID_COUNTRY                  as id_country,
       nt.ID_MATERIAL_REPORT         as id_material_report
FROM vgbc_quantity q
         CROSS JOIN
     gbc_scope s
         INNER JOIN
     gbc_scope_material_node smn
     ON smn.id_material_node = q.id_material_node
         AND s.id = smn.id_scope
         LEFT OUTER JOIN
     gbc_quantity_analysis qa
     ON qa.ID_QUANTITY = q.ID
         LEFT OUTER JOIN
     vgbc_analysis a
     ON a.ID = qa.ID_ANALYSIS
         AND a.id_unit_set = q.id_unit_set
         AND qa.ID_ANALYSIS_PARAM = a.ID_ANALYSIS_PARAM
         LEFT OUTER JOIN
     gbc_unit_type ut
     ON ut.id = a.id_unit_type_numen
         LEFT JOIN
     gbc_node_types nts
     ON nts.ID_NODE = q.ID_NODE
         LEFT JOIN
     gbc_node n
     ON n.ID = q.ID_NODE
         LEFT JOIN
     gbc_country c
     ON n.ID_COUNTRY = c.ID
         LEFT JOIN
     gbc_node_type nt
     ON nts.ID_NODE_TYPE = nt.ID
GROUP BY q.id_unit_set
             || q.id_node
             || q.id_material
             || TRUNC(date_from, 'MM')
             || a.id_analysis_param
             || s.id, q.id_unit_set, q.id_node, q.id_material, smn.id_material_node,
         a.id_analysis_param, s.id, q.id_unit_to, q.unit_to_abbr, a.id_unit_numen_to,
         a.id_unit_denom_to, a.unit_numen_abbr_to, TRUNC(date_from, 'MM'), q.NODE_REPORT_POS,
         a.ANALYSIS_PARAM_NAME_K, q.MATERIAL_NAME, q.PLANT_NAME, s.name_k, q.io, nts.ID_NODE_TYPE, c.ID_REGION, n.ID_COUNTRY,
         nt.ID_MATERIAL_REPORT;

COMMIT;

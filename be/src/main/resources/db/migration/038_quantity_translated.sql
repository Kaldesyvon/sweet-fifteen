CREATE OR REPLACE VIEW VGBC_QUANTITY_TRANSLATED AS
SELECT l.ID                                                      AS ID_LANGUAGE,
       l.ID || '-' || q.ID                                       AS ID_VIEW,

       NVL2(q.ID_NODE, l.ID || '-' || q.ID_NODE, NULL)           AS ID_NODE_VIEW,
       NVL2(m.ID, l.ID || '-' || m.ID, NULL)                     AS ID_MATERIAL_VIEW,
       NVL2(mn.ID, l.ID || '-' || mn.ID, NULL)                   AS ID_MATERIAL_NODE_VIEW,
       NVL2(q.ID_METER, l.ID || '-' || q.ID_METER, NULL)         AS ID_METER_VIEW,
       NVL2(ap.ID, l.ID || '-' || ap.ID, NULL)                   AS ID_ANALYSIS_PARAM_VIEW,
       NVL2(q.ID_UNIT_FROM, l.ID || '-' || q.ID_UNIT_FROM, NULL) AS ID_UNIT_VIEW,

       q.UNIT_TO_ABBR                                            AS UNIT_TO_ABBR,
       a.FORMATED_FACTOR_A                                       AS FORMATED_FACTOR_A,
       a.FORMATED_FACTOR_B                                       AS FORMATED_FACTOR_B,
       a.FORMATED_FACTOR_C                                       AS FORMATED_FACTOR_C,
       a.MDL_A                                                   AS MDL_A,
       a.FORMATED_UNIT_ABBR_TO                                   AS FORMATED_UNIT_ABBR_TO,
       mc.BC_MATERIAL_CODE                                       AS REMOTE_MATERIAL_CODE,

       q.ID                                                      AS ID,
       q.REMOTE_CODE                                             AS REMOTE_CODE,
       q.QUANTITY                                                AS QUANTITY,
       q.MEMO                                                    AS MEMO,
       q.DATE_FROM                                               AS DATE_FROM,
       q.DATE_TO                                                 AS DATE_TO,
       q.AUTO_ANALYSIS                                           AS AUTO_ANALYSIS,
       q.EDITABLE                                                AS EDITABLE,
       q.IO                                                      AS IO,
       q.UNCERTAINTY                                             AS UNCERTAINTY,
       q.NUMBER_OF_MEASUREMENTS                                  AS NUMBER_OF_MEASUREMENTS,

       q.CREATED                                                 AS CREATED,
       q.CREATED_BY                                              AS CREATED_BY,
       q.MODIFIED                                                AS MODIFIED,
       q.MODIFIED_BY                                             AS MODIFIED_BY,

       -- filtering params
       NVL2(qa.ID_ANALYSIS, l.ID || '-' || qa.ID_ANALYSIS, NULL) AS ID_ANALYSIS_VIEW,
       q.ID_NODE                                                 AS ID_NODE,
       q.ID_METER                                                AS ID_METER,
       m.ID                                                      AS ID_MATERIAL,
       q.ID_UNIT_SET                                             AS ID_UNIT_SET,
       mnt.ID_MATERIAL_TYPE                                      AS ID_MATERIAL_TYPE,
       q.ID_REMOTE_MATERIAL_CODE                                 AS ID_REMOTE_MATERIAL_CODE,
       ap.ID                                                     AS ID_ANALYSIS_PARAM
       --
FROM VGBC_QUANTITY q
         CROSS JOIN GBC_LANGUAGE l

         LEFT OUTER JOIN gbc_quantity_analysis qa ON q.ID = qa.ID_QUANTITY
         LEFT OUTER JOIN VGBC_ANALYSIS a ON qa.ID_ANALYSIS = a.ID AND a.id_unit_set = q.ID_UNIT_SET
         LEFT OUTER JOIN gbc_analysis_param ap ON qa.ID_ANALYSIS_PARAM = ap.ID AND a.ID_ANALYSIS_PARAM = ap.ID
         INNER JOIN gbc_material_node mn ON mn.ID = q.ID_MATERIAL_NODE
         INNER JOIN gbc_material m ON m.ID = mn.ID_MATERIAL
         LEFT OUTER JOIN gbc_material_conversion mc ON mc.id = q.ID_REMOTE_MATERIAL_CODE
         LEFT OUTER JOIN gbc_material_node_type mnt ON mnt.id_material_node = mn.id;

COMMIT;

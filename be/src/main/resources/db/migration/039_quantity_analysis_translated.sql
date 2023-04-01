create or replace view VGBC_QUANTITY_ANALYSIS_TRANSLATED as
SELECT l.ID                                                                  AS ID_LANGUAGE,
       l.ID || '-' || qa.ID                                                  AS ID_VIEW,
       NVL2(qa.ID_QUANTITY, l.ID || '-' || qa.ID_QUANTITY, NULL)             AS ID_QUANTITY_VIEW,
       NVL2(qa.ID_ANALYSIS, l.ID || '-' || qa.ID_ANALYSIS, NULL)             AS ID_ANALYSIS_VIEW,
       NVL2(qa.ID_ANALYSIS_PARAM, l.ID || '-' || qa.ID_ANALYSIS_PARAM, NULL) AS ID_ANALYSIS_PARAM_VIEW,
       qa."ID",
       qa."ID_QUANTITY",
       qa."ID_ANALYSIS",
       qa."ID_ANALYSIS_PARAM",
       qa."CREATED_BY",
       qa."CREATED",
       qa."MODIFIED_BY",
       qa."MODIFIED",
       qa."OBJ_VERSION",
       qa."REMOTE_CODE",
       qa."MEMO"
FROM GBC_QUANTITY_ANALYSIS qa,
     GBC_LANGUAGE l;

COMMIT;
CREATE OR REPLACE VIEW VGBC_FORMULA_CALCULATION_TRANSLATED AS
SELECT l.ID                                                                          AS ID_LANGUAGE,
       l.ID || '-' || m.ID                                                           AS ID_VIEW,
       NVL2(m.ID_ANALYSIS_CALCULATED, l.ID || '-' || m.ID_ANALYSIS_CALCULATED, NULL) AS ID_ANALYSIS_CALCULATED_VIEW,
       NVL2(m.ID_ANALYSIS_INPUT, l.ID || '-' || m.ID_ANALYSIS_INPUT, NULL)           AS ID_ANALYSIS_INPUT_VIEW,
       a.ID_UNIT_SET                                                                 AS ID_ANALYSIS_UNIT_SET,
       m.*
FROM GBC_FORMULA_CALCULATION m
         CROSS JOIN GBC_LANGUAGE l
--     CALCULATED_ANALYSIS AND INPUT_ANALYSIS HAS THE SAME UNIT SET
         INNER JOIN VGBC_ANALYSIS_TRANSLATED a
                    ON NVL2(m.ID_ANALYSIS_INPUT, l.ID || '-' || m.ID_ANALYSIS_INPUT, NULL) = a.ID_VIEW;

commit;
create or replace view VGBC_UNIT_SET_SETTINGS_AP_TRANSLATED as
select l.ID                                                                         AS ID_LANGUAGE,
       l.ID || '-' || ussap.ID                                                      AS ID_VIEW,
       NVL2(ussap.ID_UNIT, l.ID || '-' || ussap.ID_UNIT, NULL)                      AS ID_UNIT_VIEW,
       NVL2(ussap.ID_ANALYSIS_PARAM, l.ID || '-' || ussap.ID_ANALYSIS_PARAM, NULL)  AS ID_ANALYSIS_PARAM_VIEW,
       NVL2(ussap.ID_UNIT_SET, l.ID || '-' || ussap.ID_UNIT_SET, NULL)              AS ID_UNIT_SET_VIEW,
       ussap.*
from GBC_UNIT_SET_SETTINGS_AP ussap,
     GBC_LANGUAGE l;

-- always at the bottom commit
commit;
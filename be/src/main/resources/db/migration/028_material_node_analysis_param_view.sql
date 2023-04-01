create or replace view VGBC_MATERIAL_NODE_ANALYSIS_PARAM_TRANSLATED             as
select
    l.ID                                       as ID_LANGUAGE,
    l.ID || '-' || mnap.ID                      as ID_VIEW,
    NVL2(mnap.ID_ANALYSIS_PARAM, l.ID || '-' || mnap.ID_ANALYSIS_PARAM, NULL)  as ID_ANALYSIS_PARAM_VIEW,
    NVL2(mnap.ID_MATERIAL_NODE, l.ID || '-' || mnap.ID_MATERIAL_NODE, NULL)  as ID_MATERIAL_NODE_VIEW,
    NVL2(mnap.ID_MATERIAL_NODE_ADV_BASIS, l.ID || '-' || mnap.ID_MATERIAL_NODE_ADV_BASIS, NULL)  as ID_MATERIAL_NODE_ADV_BASIS_VIEW,
    NVL2(mnap.ID_ANALYSIS_PARAM_EXPR, l.ID || '-' || mnap.ID_ANALYSIS_PARAM_EXPR, NULL)  as ID_ANALYSIS_PARAM_EXPR_VIEW,
    mnap.*
from GBC_MATERIAL_NODE_AP mnap, GBC_LANGUAGE l;

-- always at the bottom commit
commit;
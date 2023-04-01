create or replace view VGBC_ANALYSIS_PARAM_EXPR_TRANSLATED             as
select
    l.ID                                       as ID_LANGUAGE,
    l.ID || '-' || ape.ID                      as ID_VIEW,
    NVL2(ape.ID_ANALYSIS_PARAM, l.ID || '-' || ape.ID_ANALYSIS_PARAM, NULL)  as ID_ANALYSIS_PARAM_VIEW,
    ape.*
from GBC_ANALYSIS_PARAM_EXPR ape, GBC_LANGUAGE l;

-- always at the bottom commit
commit;
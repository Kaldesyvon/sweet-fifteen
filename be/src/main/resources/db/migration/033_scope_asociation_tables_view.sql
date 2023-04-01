create or replace view VGBC_SCOPE_DENOMINATOR_TRANSLATED             as
select
    l.ID                                       as ID_LANGUAGE,
    l.ID || '-' || sd.ID                        as ID_VIEW,
    NVL2(sd.ID_MATERIAL, l.ID || '-' || sd.ID_MATERIAL, NULL)      as ID_MATERIAL_VIEW,
    NVL2(sd.ID_SCOPE, l.ID || '-' || sd.ID_SCOPE, NULL)            as ID_SCOPE_VIEW,
    sd.*
from GBC_SCOPE_DENOMINATOR sd, GBC_LANGUAGE l;

create or replace view VGBC_SCOPE_ANALYSIS_PARAM_TRANSLATED             as
select
    l.ID                                       as ID_LANGUAGE,
    l.ID || '-' || sap.ID                        as ID_VIEW,
    NVL2(sap.ID_ANALYSIS_PARAM, l.ID || '-' || sap.ID_ANALYSIS_PARAM, NULL)     as ID_ANALYSIS_PARAM_VIEW,
    NVL2(sap.ID_SCOPE, l.ID || '-' || sap.ID_SCOPE, NULL)                       as ID_SCOPE_VIEW,
    sap.*
from GBC_SCOPE_ANALYSIS_PARAM sap, GBC_LANGUAGE l;

-- always at the bottom commit
commit;
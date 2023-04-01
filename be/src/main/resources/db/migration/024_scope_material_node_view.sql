create or replace view VGBC_SCOPE_MATERIAL_NODE_TRANSLATED             as
select
    l.ID                                       as ID_LANGUAGE,
    l.ID || '-' || smn.ID                        as ID_VIEW,
    NVL2(smn.ID_SCOPE, l.ID || '-' || smn.ID_SCOPE, NULL)                   as ID_SCOPE_VIEW,
    NVL2(smn.ID_MATERIAL_NODE, l.ID || '-' || smn.ID_MATERIAL_NODE, NULL)   as ID_MATERIAL_NODE_VIEW,
    smn.*
from GBC_SCOPE_MATERIAL_NODE smn, GBC_LANGUAGE l;

-- always at the bottom commit
commit;
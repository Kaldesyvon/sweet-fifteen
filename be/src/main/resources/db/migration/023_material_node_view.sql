create or replace view VGBC_MATERIAL_NODE_TRANSLATED             as
select
    l.ID                                       as ID_LANGUAGE,
    l.ID || '-' || mn.ID                        as ID_VIEW,
    NVL2(mn.ID_MATERIAL, l.ID || '-' || mn.ID_MATERIAL, NULL)      as ID_MATERIAL_VIEW,
    NVL2(mn.ID_NODE, l.ID || '-' || mn.ID_NODE, NULL)              as ID_NODE_VIEW,
    mn.*
from GBC_MATERIAL_NODE mn, GBC_LANGUAGE l;

-- always at the bottom commit
commit;

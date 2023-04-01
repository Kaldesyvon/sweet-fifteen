create or replace view VGBC_MATERIAL_NODE_TYPE_TRANSLATED           as
select
    l.ID                                       as ID_LANGUAGE,
    l.ID || '-' || mnt.ID                      as ID_VIEW,
    NVL2(mnt.ID_MATERIAL_TYPE, l.ID || '-' || mnt.ID_MATERIAL_TYPE, NULL)  as ID_MATERIAL_TYPE_VIEW,
    NVL2(mnt.ID_MATERIAL_NODE, l.ID || '-' || mnt.ID_MATERIAL_NODE, NULL)  as ID_MATERIAL_NODE_VIEW,
    mnt.*
from GBC_MATERIAL_NODE_TYPE mnt, GBC_LANGUAGE l;

-- always at the bottom commit
commit;
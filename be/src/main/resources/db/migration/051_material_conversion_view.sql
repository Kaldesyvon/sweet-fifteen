create or replace view VGBC_MATERIAL_CONVERSION_TRANSLATED             as
select
       l.ID                                       as ID_LANGUAGE,
       l.ID || '-' || m.ID                        as ID_VIEW,
       NVL2(m.ID_MATERIAL, l.ID || '-' || m.ID_MATERIAL, NULL)      as ID_MATERIAL_VIEW,
       NVL2(m.ID_NODE, l.ID || '-' || m.ID_NODE, NULL)              as ID_NODE_VIEW,
       NVL2(m.ID_UNIT, l.ID || '-' || m.ID_UNIT, NULL)              as ID_UNIT_VIEW,
       m.*
from GBC_MATERIAL_CONVERSION m, GBC_LANGUAGE l;

-- always at the bottom commit
commit;

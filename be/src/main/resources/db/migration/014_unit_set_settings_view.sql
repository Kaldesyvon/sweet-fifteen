create or replace view VGBC_UNIT_SET_SETTINGS_TRANSLATED as
select l.ID                                                     as ID_LANGUAGE,
       l.ID || '-' || uss.ID                                      as ID_VIEW,
       l.ID || '-' || uss.ID_UNIT                                      as ID_UNIT_VIEW,
       l.ID || '-' || uss.ID_MATERIAL                                      as ID_MATERIAL_VIEW,
       l.ID || '-' || uss.ID_UNIT_SET                                      as ID_UNIT_SET_VIEW,
       uss.*
from GBC_UNIT_SET_SETTINGS uss,
     GBC_LANGUAGE l;

-- always at the bottom commit
commit;

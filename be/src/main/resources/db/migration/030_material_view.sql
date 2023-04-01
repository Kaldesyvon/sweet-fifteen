-- NVL2 - https://docm.oracle.com/cd/B19306_01/server.102/b14200/functions106.htm
create or replace view VGBC_MATERIAL_TRANSLATED             as
select
       l.ID                                       as ID_LANGUAGE,
       l.ID || '-' || m.ID                        as ID_VIEW,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = m.NAME_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(m.NAME_K, '???' || m.NAME_K || '???', null)) as NAME_EN,         -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = m.NAME_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = m.NAME_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(m.NAME_K, '???' || m.NAME_K || '???', null)) as NAME_TRANSLATED, -- or return name key
       NVL2(m.ID_MATERIAL, l.ID || '-' || m.ID_MATERIAL, NULL)               as ID_PARENT_MATERIAL_VIEW,
       NVL2(m.ID_UNIT_TYPE, l.ID || '-' || m.ID_UNIT_TYPE, NULL)              as ID_UNIT_TYPE_VIEW,
       NVL2(m.ID_USEPA_MATERIAL_TYPE, l.ID || '-' || m.ID_USEPA_MATERIAL_TYPE, NULL)    as ID_USEPA_MATERIAL_TYPE_VIEW,
       m.*
from GBC_MATERIAL m, GBC_LANGUAGE l;

-- always at the bottom commit
commit;

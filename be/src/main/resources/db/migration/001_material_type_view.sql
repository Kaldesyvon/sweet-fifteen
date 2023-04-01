create or replace view VGBC_MATERIAL_TYPE_TRANSLATED as
select l.ID                                                       as ID_LANGUAGE,
       l.ID || '-' || m.ID                                        as ID_VIEW,
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
       m.*
from GBC_MATERIAL_TYPE m,
     GBC_LANGUAGE l;


-- always at the bottom commit
commit;

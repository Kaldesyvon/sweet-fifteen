create or replace view VGBC_COUNTRY_TRANSLATED as
select l.ID                                                     as ID_LANGUAGE,
       l.ID || '-' || c.ID                                      as ID_VIEW,
       nvl2(c.ID_REGION, l.ID || '-' || c.ID_REGION, null)      as ID_REGION_VIEW,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = c.NAME_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(c.NAME_K, '???' || c.NAME_K || '???', null)) as NAME_EN, -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = c.NAME_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                                from GBC_DICTIONARY
                                                where KEY = c.NAME_K
                                                  and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(c.NAME_K, '???' || c.NAME_K || '???', null)) as NAME_TRANSLATED,
       c.*
from GBC_COUNTRY c,
     GBC_LANGUAGE l;

-- always at the bottom commit
commit;
create or replace view VGBC_EVENT_TYPE_TRANSLATED as
select l.ID                                                             as ID_LANGUAGE,
       l.ID || '-' || et.ID                                             as ID_VIEW,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = et.DETAIL_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(et.DETAIL_K, '???' || et.DETAIL_K || '???', null)) as DETAIL_EN,         -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = et.DETAIL_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = et.DETAIL_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(et.DETAIL_K, '???' || et.DETAIL_K || '???', null)) as DETAIL_TRANSLATED, -- or return name key

       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = et.TEXT_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(et.TEXT_K, '???' || et.TEXT_K || '???', null))     as TEXT_EN,           -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = et.TEXT_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = et.TEXT_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(et.TEXT_K, '???' || et.TEXT_K || '???', null))     as TEXT_TRANSLATED,   -- or return name key
       et.*
from GBC_EVENT_TYPE et,
     GBC_LANGUAGE l;-- or if translation does not exists


CREATE OR REPLACE VIEW VGBC_EVENT_TRANSLATED AS
select l.ID                                            AS ID_LANGUAGE,
       l.ID || '-' || e.ID                             AS ID_VIEW,
       NVL2(e.ID_TYPE, l.ID || '-' || e.ID_TYPE, NULL) AS ID_EVENT_TYPE_VIEW,
       e.*
from GBC_EVENT e,
     GBC_LANGUAGE l;


-- always at the bottom commit
commit;

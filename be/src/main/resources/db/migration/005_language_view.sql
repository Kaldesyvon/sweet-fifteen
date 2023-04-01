create or replace view VGBC_LANGUAGE_TRANSLATED as
select l.ID                                                             as ID_LANGUAGE,
       l.ID || '-' || lang.ID                                           as ID_VIEW,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = lang.NAME_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ), '???' || lang.NAME_K ||
                   '???')                                               as NAME_EN,         -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = lang.NAME_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = lang.NAME_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(lang.NAME_K, '???' || lang.NAME_K || '???', null)) as NAME_TRANSLATED, -- or return name key
       lang.*
from GBC_LANGUAGE lang,
     GBC_LANGUAGE l;


-- always at the bottom commit
commit;

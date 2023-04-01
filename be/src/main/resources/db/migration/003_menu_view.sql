create or replace view VGBC_MENU_TRANSLATED as
select l.ID                                                       as ID_LANGUAGE,
       l.ID || '-' || m.ID                                        as ID_VIEW,
       nvl2(m.ID_MENU, l.ID || '-' || m.ID_MENU, null)            as ID_PARENT_VIEW,
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
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = m.MEMO_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(m.MEMO_K, '???' || m.MEMO_K || '???', null)) as MEMO_EN,         -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = m.MEMO_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = m.MEMO_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(m.MEMO_K, '???' || m.MEMO_K || '???', null)) as MEMO_TRANSLATED, -- or return name key
       m.*
from GBC_MENU m,
     GBC_LANGUAGE l;

-- always at the bottom commit
commit;

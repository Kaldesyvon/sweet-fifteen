create or replace view VGBC_UNIT_TYPE_TRANSLATED as
select l.ID                                                         as ID_LANGUAGE,
       l.ID || '-' || ut.ID                                         as ID_VIEW,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = ut.NAME_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(ut.NAME_K, '???' || ut.NAME_K || '???', null)) as NAME_EN,         -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = ut.NAME_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = ut.NAME_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(ut.NAME_K, '???' || ut.NAME_K || '???', null)) as NAME_TRANSLATED, -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = ut.MEMO_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(ut.MEMO_K, '???' || ut.MEMO_K || '???', null)) as MEMO_EN,         -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = ut.MEMO_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = ut.MEMO_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(ut.MEMO_K, '???' || ut.MEMO_K || '???', null)) as MEMO_TRANSLATED, -- or return name key
       ut.*
from GBC_UNIT_TYPE ut,
     GBC_LANGUAGE l;


create or replace view VGBC_UNIT_TRANSLATED as
select l.ID                                                          as ID_LANGUAGE,
       l.ID || '-' || u.ID                                           as ID_VIEW,
       nvl2(u.ID_UNIT_TYPE, l.ID || '-' || u.ID_UNIT_TYPE, null)     as ID_UNIT_TYPE_VIEW,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = u.NAME_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(u.NAME_K, '???' || u.NAME_K || '???', null))    as NAME_EN, -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = u.NAME_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = u.NAME_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ), nvl2(u.NAME_K, '???' || u.NAME_K || '???', null)) as NAME_TRANSLATED,
       u.*
from GBC_UNIT u,
     GBC_LANGUAGE l;;


-- create view for unit set
create or replace view VGBC_UNIT_SET_TRANSLATED as
select l.ID                                                       as ID_LANGUAGE,
       l.ID || '-' || s.ID                                        as ID_VIEW,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = s.NAME_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(s.NAME_K, '???' || s.NAME_K || '???', null)) as NAME_EN,         -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = s.NAME_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = s.NAME_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(s.NAME_K, '???' || s.NAME_K || '???', null)) as NAME_TRANSLATED, -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = s.MEMO_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(s.MEMO_K, '???' || s.MEMO_K || '???', null)) as MEMO_EN,         -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = s.MEMO_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = s.MEMO_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(s.MEMO_K, '???' || s.MEMO_K || '???', null)) as MEMO_TRANSLATED, -- or return name key
       s.*
from GBC_UNIT_SET s,
     GBC_LANGUAGE l;


-- always at the bottom commit
commit;

create or replace view VGBC_BUSINESS_UNIT_TRANSLATED as
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
       ut.*
from GBC_BUSINESS_UNIT ut,
     GBC_LANGUAGE l;


create or replace view VGBC_FUEL_TYPE_TRANSLATED as
select l.ID                                                          as ID_LANGUAGE,
       l.ID || '-' || u.ID                                           as ID_VIEW,
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
from GBC_FUEL_TYPE u,
     GBC_LANGUAGE l;

create or replace view VGBC_JOURNAL_TYPE_TRANSLATED as
select l.ID                                                          as ID_LANGUAGE,
       l.ID || '-' || u.ID                                           as ID_VIEW,
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
from GBC_JOURNAL_TYPE u,
     GBC_LANGUAGE l;


create or replace view VGBC_NODE_LEVEL_TRANSLATED as
select l.ID                                                          as ID_LANGUAGE,
       l.ID || '-' || u.ID                                           as ID_VIEW,
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
from GBC_NODE_LEVEL u,
     GBC_LANGUAGE l;


create or replace view VGBC_NODE_TYPE_TRANSLATED as
select l.ID                                                          as ID_LANGUAGE,
       l.ID || '-' || u.ID                                           as ID_VIEW,
       nvl2(u.ID_MATERIAL_REPORT, l.ID || '-' || u.ID_MATERIAL_REPORT,
            null)                                                    as ID_MATERIAL_REPORT_VIEW,
       nvl2(u.ID_NODE_LEVEL, l.ID || '-' || u.ID_NODE_LEVEL, null)   as ID_NODE_LEVEL_VIEW,
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
from GBC_NODE_TYPE u,
     GBC_LANGUAGE l;



create or replace view VGBC_REGION_TRANSLATED as
select l.ID                                                          as ID_LANGUAGE,
       l.ID || '-' || u.ID                                           as ID_VIEW,
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
from GBC_REGION u,
     GBC_LANGUAGE l;


create or replace view VGBC_ROLE_TRANSLATED as
select l.ID                                                          as ID_LANGUAGE,
       l.ID || '-' || u.ID                                           as ID_VIEW,
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
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = u.MEMO_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(u.MEMO_K, '???' || u.MEMO_K || '???', null))    as MEMO_EN, -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = u.MEMO_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = u.MEMO_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ), nvl2(u.MEMO_K, '???' || u.MEMO_K || '???', null)) as MEMO_TRANSLATED,
       u.*
from GBC_ROLE u,
     GBC_LANGUAGE l;

create or replace view VGBC_SCREEN_TRANSLATED as
select l.ID                                                                 as ID_LANGUAGE,
       l.ID || '-' || u.ID                                                  as ID_VIEW,
       nvl2(u.ID_MENU, l.ID || '-' || u.ID_MENU, null)                      as ID_MENU_VIEW,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = u.TITLE_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(u.TITLE_K, '???' || u.TITLE_K || '???', null))         as TITLE_EN,     -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = u.TITLE_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = u.TITLE_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ), nvl2(u.TITLE_K, '???' || u.TITLE_K || '???', null))      as TITLE_TRANSLATED,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = u.HELPLABEL_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(u.HELPLABEL_K, '???' || u.HELPLABEL_K || '???', null)) as HELPLABEL_EN, -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = u.HELPLABEL_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = u.HELPLABEL_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(u.HELPLABEL_K, '???' || u.HELPLABEL_K || '???', null)) as HELPLABEL_TRANSLATED,
       u.*
from GBC_SCREEN u,
     GBC_LANGUAGE l;


create or replace view VGBC_USEPA_MATERIAL_TYPE_TRANSLATED as
select l.ID                                                          as ID_LANGUAGE,
       l.ID || '-' || u.ID                                           as ID_VIEW,
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
from GBC_USEPA_MATERIAL_TYPE u,
     GBC_LANGUAGE l;


-- always at the bottom commit
commit;

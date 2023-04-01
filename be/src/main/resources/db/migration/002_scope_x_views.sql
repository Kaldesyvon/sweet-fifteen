create or replace view VGBC_SCOPE_FUEL_SPEC_TRANSLATED as
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
       s.*
from GBC_SCOPE_FUEL_SPEC s,
     GBC_LANGUAGE l;


create or replace view VGBC_SCOPE_PROCESS_SPEC_TRANSLATED as
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
       s.*
from GBC_SCOPE_PROCESS_SPEC s,
     GBC_LANGUAGE l;

create or replace view VGBC_SCOPE_TYPE_SPEC_TRANSLATED as
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
       s.*
from GBC_SCOPE_TYPE_SPEC s,
     GBC_LANGUAGE l;


create or replace view VGBC_SCOPE_TRANSLATED as
select l.ID                                                       as ID_LANGUAGE,
       l.ID || '-' || s.ID                                        as ID_VIEW,
       nvl2(s.ID_SCOPE_PROCESS_SPEC, l.ID || '-' || s.ID_SCOPE_PROCESS_SPEC,
            null)                                                 as ID_SCOPE_PROCESS_SPEC_VIEW,
       nvl2(s.ID_SCOPE_TYPE_SPEC, l.ID || '-' || s.ID_SCOPE_TYPE_SPEC,
            null)                                                 as ID_SCOPE_TYPE_SPEC_VIEW,
       nvl2(s.ID_SCOPE_FUEL_SPEC, l.ID || '-' || s.ID_SCOPE_FUEL_SPEC,
            null)                                                 as ID_SCOPE_FUEL_SPEC_VIEW,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = s.NAME_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(s.NAME_K, '???' || s.NAME_K || '???', null)) as NAME_EN, -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = s.NAME_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = s.NAME_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(s.NAME_K, '???' || s.NAME_K || '???', null)) as NAME_TRANSLATED,
       coalesce((select count(*)
                 from GBC_SCOPE_DENOMINATOR sd
                 where sd.ID_SCOPE = s.ID), 0)                    as DENOMINATORS,
       s.*
from GBC_SCOPE s,
     GBC_LANGUAGE l;

-- always at the bottom commit
commit;

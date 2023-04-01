CREATE OR REPLACE VIEW VGBC_UNIT_ANALYSIS_FORMAT_TRANSLATED AS
SELECT l.ID                                                           AS ID_LANGUAGE,
       l.ID || '-' || uaf.ID                                          AS ID_VIEW,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = uaf.NAME_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = uaf.NAME_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(uaf.NAME_K, '???' || uaf.NAME_K || '???', null)) as NAME_TRANSLATED, -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = uaf.NAME_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(uaf.NAME_K, '???' || uaf.NAME_K || '???', null)) as NAME_EN,
       uaf.*
FROM GBC_UNIT_ANALYSIS_FORMAT uaf,
     GBC_LANGUAGE l;

CREATE OR REPLACE VIEW VGBC_ANALYSIS_PARAM_TYPE_TRANSLATED AS
SELECT l.ID                                                           AS ID_LANGUAGE,
       l.ID || '-' || apt.ID                                          AS ID_VIEW,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = apt.NAME_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = apt.NAME_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(apt.NAME_K, '???' || apt.NAME_K || '???', null)) as NAME_TRANSLATED, -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = apt.NAME_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(apt.NAME_K, '???' || apt.NAME_K || '???', null)) as NAME_EN,
       apt.*
FROM GBC_ANALYSIS_PARAM_TYPE apt,
     GBC_LANGUAGE l;


CREATE OR REPLACE VIEW VGBC_ANALYSIS_PARAM_TRANSLATED AS
SELECT l.ID                                                         AS ID_LANGUAGE,
       l.ID || '-' || ap.ID                                         AS ID_VIEW,
       NVL2(ap.ID_PARENT_ANALYSIS_PARAM, l.ID || '-' || ap.ID_PARENT_ANALYSIS_PARAM,
            NULL)                                                   AS ID_PARENT_ANALYSIS_PARAM_VIEW,
       NVL2(ap.ID_UNIT_TYPE, l.ID || '-' || ap.ID_UNIT_TYPE,
            NULL)                                                   AS ID_UNIT_TYPE_VIEW,
       NVL2(ap.ID_ANALYSIS_FORMAT, l.ID || '-' || ap.ID_ANALYSIS_FORMAT,
            NULL)                                                   AS ID_ANALYSIS_FORMAT_VIEW,
       NVL2(ap.ID_TYPE, l.ID || '-' || ap.ID_TYPE, NULL)            AS ID_TYPE_VIEW,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = ap.NAME_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(ap.NAME_K, '???' || ap.NAME_K || '???', null)) as NAME_EN,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = ap.NAME_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = ap.NAME_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(ap.NAME_K, '???' || ap.NAME_K || '???', null)) as NAME_TRANSLATED, -- or return name key
       ap.*
FROM GBC_ANALYSIS_PARAM ap,
     GBC_LANGUAGE l;


commit;

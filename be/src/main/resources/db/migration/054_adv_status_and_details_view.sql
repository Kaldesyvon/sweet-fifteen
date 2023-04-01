create or replace view VGBC_ADV_STATUS_TRANSLATED as
select l.ID                                                             as ID_LANGUAGE,
       l.ID || '-' || advs.ID                                           as ID_VIEW,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = advs.NAME_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = advs.NAME_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(advs.NAME_K, '???' || advs.NAME_K || '???', null)) as NAME_TRANSLATED,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = advs.NAME_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(advs.NAME_K, '???' || advs.NAME_K || '???', null)) as NAME_EN, -- or return name key
       advs.*
from GBC_ADV_STATUS advs,
     GBC_LANGUAGE l;



create or replace view VGBC_ADV_DETAILS_TRANSLATED as
select l.ID                                                                      as ID_LANGUAGE,
       l.ID || '-' || advd.ID                                                    as ID_VIEW,
       NVL2(advd.ID_NODE, l.ID || '-' || advd.ID_NODE, NULL)                     as ID_NODE_VIEW,
       NVL2(advd.ID_MATERIAL, l.ID || '-' || advd.ID_MATERIAL, NULL)             as ID_MATERIAL_VIEW,
       NVL2(advd.ID_ANALYSIS_PARAM, l.ID || '-' || advd.ID_ANALYSIS_PARAM, NULL) as ID_ANALYSIS_PARAM_VIEW,
       NVL2(advd.ID_ADV_STATUS, l.ID || '-' || advd.ID_ADV_STATUS, NULL)         as ID_ADV_STATUS_VIEW,
       NVL2(advd.ID_MAT_BASIS, l.ID || '-' || advd.ID_MAT_BASIS, NULL)           as ID_MATERIAL_BASIS_VIEW,
       NVL2(advd.ID_UNIT_TO, l.ID || '-' || advd.ID_UNIT_TO, NULL)           as ID_UNIT_TO_VIEW,
       advd.*
from VGBC_ADV_DETAILS advd,
     GBC_LANGUAGE l;

-- always at the bottom commit
commit;

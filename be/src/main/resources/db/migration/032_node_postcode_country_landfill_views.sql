CREATE OR REPLACE VIEW VGBC_NODE_TRANSLATED AS
select l.ID                                            AS ID_LANGUAGE,
       l.ID || '-' || n.ID                             AS ID_VIEW,
       nvl2(n.ID_NODE, l.ID || '-' || n.ID_NODE, NULL) AS ID_NODE_VIEW,
       nvl2(n.ID_COUNTRY, l.ID || '-' || n.ID_COUNTRY, NULL) AS ID_COUNTRY_VIEW,
       nvl2(n.ID_NODE_LEVEL, l.ID || '-' || n.ID_NODE_LEVEL, NULL) AS ID_NODE_LEVEL_VIEW,
       nvl2(n.ID_LANDFILL, l.ID || '-' || n.ID_LANDFILL, NULL) AS ID_LANDFILL_VIEW,
       nvl2(n.ID_FUEL_TYPE, l.ID || '-' || n.ID_FUEL_TYPE, NULL) AS ID_FUEL_TYPE_VIEW,
       nvl2(n.ID_BUSINESS_UNIT, l.ID || '-' || n.ID_BUSINESS_UNIT, NULL) AS ID_BUSINESS_UNIT_VIEW,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = n.NAME_K
                   and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(n.NAME_K, '???' || n.NAME_K || '???', null)) as NAME_EN, -- or return name key
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = n.NAME_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = n.NAME_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(n.NAME_K, '???' || n.NAME_K || '???', null)) as NAME_TRANSLATED,
       n.*
from GBC_NODE n,
     GBC_LANGUAGE l;

-- always at the bottom commit
commit;
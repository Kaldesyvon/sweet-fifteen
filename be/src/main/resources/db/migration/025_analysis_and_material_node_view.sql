CREATE OR REPLACE VIEW VGBC_ANALYSIS_TRANSLATED AS
SELECT l.ID                                                                    AS ID_LANGUAGE,
       us.ID                                                                   AS ID_UNIT_SET,
       p.ID                                                                    AS ID_NODE,
       m.ID                                                                    AS ID_MATERIAL,
       l.ID || '-' || a.ID                                                     AS ID_VIEW,
       NVL2(a.ID_MATERIAL_NODE, l.ID || '-' || a.ID_MATERIAL_NODE, NULL)       AS ID_MATERIAL_NODE_VIEW,
       NVL2(a.ID_ANALYSIS_PARAM, l.ID || '-' || a.ID_ANALYSIS_PARAM, NULL)     AS ID_ANALYSIS_PARAM_VIEW,
       NVL2(a.ID_UNIT_DENOMINATOR, l.ID || '-' || a.ID_UNIT_DENOMINATOR, NULL) AS ID_UNIT_DENOMINATOR_VIEW,
       NVL2(a.ID_UNIT_NUMENATOR, l.ID || '-' || a.ID_UNIT_NUMENATOR, NULL)     AS ID_UNIT_NUMENATOR_VIEW,
       NVL2(a.ID_REMOTE_MATERIAL_CODE, l.ID || '-' || a.ID_REMOTE_MATERIAL_CODE, NULL)     AS ID_REMOTE_MATERIAL_CODE_VIEW,

       (unit_numen_to.k / unit_numen_from.k * a.factor_a + unit_numen_to.q -
        unit_numen_from.q * (unit_numen_to.k / unit_numen_from.k)) / NVL(
               (unit_denom_to.k / unit_denom_from.k + unit_denom_to.q -
                unit_denom_from.q * (unit_denom_to.k / unit_denom_from.k)),
               1)                                                                 factor_a_from_view, -- factor_a je deklarovany uz v entite
       (unit_numen_to.k / unit_numen_from.k * a.factor_b + unit_numen_to.q -
        unit_numen_from.q * (unit_numen_to.k / unit_numen_from.k)) / NVL(
               (unit_denom_to.k / unit_denom_from.k + unit_denom_to.q -
                unit_denom_from.q * (unit_denom_to.k / unit_denom_from.k)),
               1)                                                                 factor_b_from_view, -- factor_b je deklarovany uz v entite
       (unit_numen_to.k / unit_numen_from.k * a.factor_c + unit_numen_to.q -
        unit_numen_from.q * (unit_numen_to.k / unit_numen_from.k)) / NVL(
               (unit_denom_to.k / unit_denom_from.k + unit_denom_to.q -
                unit_denom_from.q * (unit_denom_to.k / unit_denom_from.k)),
               1)                                                                 factor_c_from_view, -- factor_c je deklarovany uz v entite
       --analysis format
       ROUND(DECODE(unit_numen_to.id_unit_type, unit_denom_from.id_unit_type,
                 --ak su typy jednotiek rovnake mozme skusit naformatovat
                    (unit_numen_to.k / unit_numen_from.k * a.factor_a + unit_numen_to.q -
                     unit_numen_from.q * (unit_numen_to.k / unit_numen_from.k)) / NVL(
                            (unit_numen_to.k / unit_denom_from.k + unit_numen_to.q -
                             unit_denom_from.q * (unit_numen_to.k / unit_denom_from.k)), 1) * uaf.koef,
                 --ak nie su typy jednotiek rovnake
                    (unit_numen_to.k / unit_numen_from.k * a.factor_a + unit_numen_to.q -
                     unit_numen_from.q * (unit_numen_to.k / unit_numen_from.k)) / NVL(
                            (unit_denom_to.k / unit_denom_from.k + unit_denom_to.q -
                             unit_denom_from.q * (unit_denom_to.k / unit_denom_from.k)), 1)),
             uaf.max_fraction_digits)                                             formatted_factor_a,
       ROUND(DECODE(unit_numen_to.id_unit_type, unit_denom_from.id_unit_type,
                    (unit_numen_to.k / unit_numen_from.k * a.factor_b + unit_numen_to.q -
                     unit_numen_from.q * (unit_numen_to.k / unit_numen_from.k)) / NVL(
                            (unit_numen_to.k / unit_denom_from.k + unit_numen_to.q -
                             unit_denom_from.q * (unit_numen_to.k / unit_denom_from.k)), 1) * uaf.koef,
                    (unit_numen_to.k / unit_numen_from.k * a.factor_b + unit_numen_to.q -
                     unit_numen_from.q * (unit_numen_to.k / unit_numen_from.k)) / NVL(
                            (unit_denom_to.k / unit_denom_from.k + unit_denom_to.q -
                             unit_denom_from.q * (unit_denom_to.k / unit_denom_from.k)), 1)),
             uaf.max_fraction_digits)                                             formatted_factor_b,
       ROUND(DECODE(unit_numen_to.id_unit_type, unit_denom_from.id_unit_type,
                    (unit_numen_to.k / unit_numen_from.k * a.factor_c + unit_numen_to.q -
                     unit_numen_from.q * (unit_numen_to.k / unit_numen_from.k)) / NVL(
                            (unit_numen_to.k / unit_denom_from.k + unit_numen_to.q -
                             unit_denom_from.q * (unit_numen_to.k / unit_denom_from.k)), 1) * uaf.koef,
                    (unit_numen_to.k / unit_numen_from.k * a.factor_c + unit_numen_to.q -
                     unit_numen_from.q * (unit_numen_to.k / unit_numen_from.k)) / NVL(
                            (unit_denom_to.k / unit_denom_from.k + unit_denom_to.q -
                             unit_denom_from.q * (unit_denom_to.k / unit_denom_from.k)), 1)),
             uaf.max_fraction_digits)                                             formatted_factor_c,
       DECODE(unit_numen_to.id_unit_type, unit_denom_from.id_unit_type,
              NVL(uaf.abbr, DECODE(a.id_unit_denominator, NULL, unit_numen_to.abbr, unit_numen_to.abbr
                  || '/'
                  || unit_denom_to.abbr)), DECODE(a.id_unit_denominator, NULL, unit_numen_to.abbr, unit_numen_to.abbr
           || '/'
           || unit_denom_to.abbr))                                                formatted_unit_abbr_to,
       --end of analysis format

       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = p.NAME_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = p.NAME_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(p.NAME_K, '???' || p.NAME_K || '???', null)) as NODE_NAME_TRANSLATED, -- or return name key

       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = m.NAME_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = m.NAME_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(m.NAME_K, '???' || m.NAME_K || '???', null)) as MATERIAL_NAME_TRANSLATED, -- or return name key

       mp.NAME as MATERIAL_NODE_NAME_TRANSLATED, -- or return name key

       coalesce((select TRANSLATION
                 from GBC_DICTIONARY
                 where KEY = ap.NAME_K
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY
                                             where KEY = ap.NAME_K
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(ap.NAME_K, '???' || ap.NAME_K || '???', null)) as ANALYSYS_PARAM_TRANSLATED, -- or return name key

       a.*
FROM gbc_analysis a
         CROSS JOIN gbc_unit_set us
         CROSS JOIN GBC_LANGUAGE l
         INNER JOIN gbc_material_node mp
                    ON mp.ID = a.ID_MATERIAL_NODE
         INNER JOIN gbc_material m
                    ON m.id = mp.id_material
         INNER JOIN gbc_node p
                    ON p.id = mp.id_node
         INNER JOIN gbc_unit unit_numen_from
                    ON unit_numen_from.id = a.id_unit_numenator
         INNER JOIN GBC_ANALYSIS_PARAM ap
                    ON ap.id = a.id_analysis_param
         INNER JOIN GBC_UNIT_ANALYSIS_FORMAT uaf
                    ON uaf.id = ap.ID_ANALYSIS_FORMAT
         LEFT OUTER JOIN gbc_unit unit_denom_from
                         ON unit_denom_from.id = a.id_unit_denominator
         LEFT OUTER JOIN GBC_UNIT_SET_SETTINGS_AP uss_ap_to
                         ON uss_ap_to.id_unit_set = us.id
                             AND uss_ap_to.id_analysis_param = ap.id
         LEFT OUTER JOIN gbc_unit unit_numen_to
                         ON unit_numen_to.id = uss_ap_to.id_unit
         LEFT OUTER JOIN GBC_UNIT_SET_SETTINGS uss_to
                         ON uss_to.id_unit_set = us.id
                             AND uss_to.id_material = m.id
         LEFT OUTER JOIN gbc_unit unit_denom_to
                         ON unit_denom_to.id = uss_to.id_unit;


COMMIT;

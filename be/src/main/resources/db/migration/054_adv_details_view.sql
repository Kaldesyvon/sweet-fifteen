create or replace view VGBC_ADV_DETAILS as
SELECT   adv.ID as ID,
         material_node.ID_MATERIAL as ID_MATERIAL,
         adv.MONTH as MONTH,
         (  unit_to.k / unit_from.k * adv.quantity
                + unit_to.q
             - unit_from.q * (unit_to.k / unit_from.k))
             as QUANTITY,
         unit_from.id ID_UNIT_FROM,
         unit_to.id ID_UNIT_TO,
         adv.VAL_INTENSITY as VAL_INTENSITY,
         adv.VAL_INTENSITY_MIN as VAL_INTENSITY_MIN,
         adv.VAL_INTENSITY_MAX as VAL_INTENSITY_MAX,
         adv.VAL_INTENSITY_MONTHS as VAL_INTENSITY_MONTHS,
         adv.CREATED_BY as CREATED_BY,
         adv.CREATED as CREATED,
         adv.MODIFIED_BY as MODIFIED_BY,
         adv.MODIFIED as MODIFIED,
         adv.MARKED_AS_VALID_BY as MARKED_AS_VALID_BY,
         adv.MARKED_AS_VALID as MARKED_AS_VALID,
         material_node.ID_NODE as ID_NODE,
         DECODE (adv.ADV_VALID, 1, 1, adv.ID_ADV_STATUS) as ID_ADV_STATUS,
         adv.ID_MATERIAL_BASIS as ID_MAT_BASIS,
         adv.INTENSITY_STD as INTENSITY_STD,
         adv.ADV_PARAMS as ADV_PARAMS,
         adv.INTENSITY_MEAN as INTENSITY_MEAN,
         adv.ID_ANALYSIS_PARAM as ID_ANALYSIS_PARAM,
         adv.ADV_VALID as ADV_VALID,
         us.id as ID_UNIT_SET,
         unit_numen_from.id as ID_UNIT_NUMEN_FROM,
         unit_numen_to.id as ID_UNIT_NUMEN_TO,
         unit_denom_from.id as ID_UNIT_DENOM_FROM,
         unit_denom_to.id as ID_UNIT_DENOM_TO,
         uss_to.id as ID_UNIT_SET_SETTINGS,
         uss_ap_to.id as ID_UNIT_SET_SETTINGS_AP,
         (  unit_numen_to.k / unit_numen_from.k * adv.factor_a
                + unit_numen_to.q
             - unit_numen_from.q * (unit_numen_to.k / unit_numen_from.k))
             / NVL (
                 (  unit_denom_to.k / unit_denom_from.k
                        + unit_denom_to.q
                     - unit_denom_from.q * (unit_denom_to.k / unit_denom_from.k)),
                 1
             )
             as FACTOR_A,
         (  unit_numen_to.k / unit_numen_from.k * adv.factor_b
                + unit_numen_to.q
             - unit_numen_from.q * (unit_numen_to.k / unit_numen_from.k))
             / NVL (
                 (  unit_denom_to.k / unit_denom_from.k
                        + unit_denom_to.q
                     - unit_denom_from.q * (unit_denom_to.k / unit_denom_from.k)),
                 1
             )
             as FACTOR_B,
         DECODE (adv.id_unit_denominator,
                 NULL, unit_numen_to.abbr,
                 unit_numen_to.abbr || '/' || unit_denom_to.abbr)
             as UNIT_ABBR_TO,
         --analysis format
         ROUND (
                 DECODE (
                         unit_numen_to.id_unit_type,
                         unit_denom_from.id_unit_type,
                     --ak su typy jednotiek rovnake mozme skusit naformatovat
                         (unit_numen_to.k / unit_numen_from.k * adv.factor_a
                              + unit_numen_to.q
                             - unit_numen_from.q
                              * (unit_numen_to.k / unit_numen_from.k))
                             / NVL (
                                 (unit_numen_to.k / unit_denom_from.k + unit_numen_to.q
                                     - unit_denom_from.q
                                      * (unit_numen_to.k / unit_denom_from.k)),
                                 1
                             )
                             * uaf.koef,
                     --ak nie su typy jednotiek rovnake
                         (unit_numen_to.k / unit_numen_from.k * adv.factor_a
                              + unit_numen_to.q
                             - unit_numen_from.q
                              * (unit_numen_to.k / unit_numen_from.k))
                             / NVL (
                                 (unit_denom_to.k / unit_denom_from.k + unit_denom_to.q
                                     - unit_denom_from.q
                                      * (unit_denom_to.k / unit_denom_from.k)),
                                 1
                             )
                     ),
                 uaf.max_fraction_digits
             )
             as FORMATED_FACTOR_A,
         ROUND (
                 DECODE (
                         unit_numen_to.id_unit_type,
                         unit_denom_from.id_unit_type,
                         (unit_numen_to.k / unit_numen_from.k * adv.factor_b
                              + unit_numen_to.q
                             - unit_numen_from.q
                              * (unit_numen_to.k / unit_numen_from.k))
                             / NVL (
                                 (unit_numen_to.k / unit_denom_from.k + unit_numen_to.q
                                     - unit_denom_from.q
                                      * (unit_numen_to.k / unit_denom_from.k)),
                                 1
                             )
                             * uaf.koef,
                         (unit_numen_to.k / unit_numen_from.k * adv.factor_b
                              + unit_numen_to.q
                             - unit_numen_from.q
                              * (unit_numen_to.k / unit_numen_from.k))
                             / NVL (
                                 (unit_denom_to.k / unit_denom_from.k + unit_denom_to.q
                                     - unit_denom_from.q
                                      * (unit_denom_to.k / unit_denom_from.k)),
                                 1
                             )
                     ),
                 uaf.max_fraction_digits
             )
             as FORMATED_FACTOR_B,
         DECODE (
                 unit_numen_to.id_unit_type,
                 unit_denom_from.id_unit_type,
                 NVL (
                         uaf.abbr,
                         DECODE (adv.id_unit_denominator,
                                 NULL, unit_numen_to.abbr,
                                 unit_numen_to.abbr || '/' || unit_denom_to.abbr)
                     ),
                 DECODE (adv.id_unit_denominator,
                         NULL, unit_numen_to.abbr,
                         unit_numen_to.abbr || '/' || unit_denom_to.abbr)
             )
             as FORMATED_UNIT_ABBR_TO,
         --end of analysis format
         unit_numen_to.abbr as UNIT_NUMEN_ABBR_TO,
         unit_numen_to.id_unit_type as ID_UNIT_TYPE_NUMEN
FROM                                    gbc_adv_details adv
                                            CROSS JOIN
                                        gbc_unit_set us
                                            INNER JOIN
                                        gbc_material_node material_node
                                        ON material_node.id =
                                           adv.id_material_node
                                            INNER JOIN
                                        gbc_unit unit_numen_from
                                        ON unit_numen_from.id =
                                           adv.id_unit_numenator
                                            INNER JOIN
                                        GBC_ANALYSIS_PARAM ap
                                        ON ap.id = adv.id_analysis_param
                                            INNER JOIN
                                        GBC_UNIT_ANALYSIS_FORMAT uaf
                                        ON uaf.id = ap.ID_ANALYSIS_FORMAT
                                            LEFT OUTER JOIN
                                        gbc_unit unit_denom_from
                                        ON unit_denom_from.id = adv.id_unit_denominator
                                            LEFT OUTER JOIN
                                        GBC_UNIT_SET_SETTINGS_AP uss_ap_to
                                        ON uss_ap_to.id_unit_set = us.id
                                            AND uss_ap_to.id_analysis_param = ap.id
                                            LEFT OUTER JOIN
                                        gbc_unit unit_numen_to
                                        ON unit_numen_to.id = uss_ap_to.id_unit
                                            LEFT OUTER JOIN
                                        GBC_UNIT_SET_SETTINGS uss_to
                                        ON uss_to.id_unit_set = us.id
                                            AND uss_to.id_material = material_node.id_material
                                            LEFT OUTER JOIN
                                        gbc_unit unit_denom_to
                                        ON unit_denom_to.id = uss_to.id_unit
                                            INNER JOIN
                                        gbc_unit unit_from
                                        ON unit_from.id = adv.id_unit
                                            LEFT OUTER JOIN
                                        gbc_unit unit_to
                                        ON unit_to.id = uss_to.id_unit;

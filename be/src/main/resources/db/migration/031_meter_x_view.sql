CREATE OR REPLACE VIEW VGBC_METER_TRANSLATED AS
SELECT l.ID                                                              AS ID_LANGUAGE,
       l.ID || '-' || m.ID                                               AS ID_VIEW,
       NVL2(m.ID_METER_TYPE, l.ID || '-' || m.ID_METER_TYPE, NULL)       AS ID_METER_TYPE_VIEW,
       NVL2(m.ID_UNIT_TYPE, l.ID || '-' || m.ID_UNIT_TYPE, NULL)         AS ID_UNIT_TYPE_VIEW,
       NVL2(m.ID_FUEL_TYPE, l.ID || '-' || m.ID_FUEL_TYPE, NULL)         AS ID_FUEL_TYPE_VIEW,
       NVL2(m.ID_NODE_LOCATION, l.ID || '-' || m.ID_NODE_LOCATION, NULL) AS ID_NODE_LOCATION_VIEW,
       (select max(mc.VALID_TO) from GBC_METER_CERTIFICATE mc where m.ID = mc.ID_METER) as MAX_VALID_DATE,
       m.*
FROM GBC_METER m,
     GBC_LANGUAGE l;

-- always at the bottom commit
COMMIT;
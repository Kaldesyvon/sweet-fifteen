CREATE OR REPLACE VIEW VGBC_METER_UNCERTAINTY_TRANSLATED AS
SELECT l.ID                                                AS ID_LANGUAGE,
       l.ID || '-' || mu.ID                                AS ID_VIEW,
       NVL2(mu.ID_METER, l.ID || '-' || mu.ID_METER, NULL) AS ID_METER_VIEW,
       NVL2(mu.ID_UNIT, l.ID || '-' || mu.ID_UNIT, NULL)   AS ID_UNIT_VIEW,
       mu.*
FROM GBC_METER_UNCERTAINTY mu,
     GBC_LANGUAGE l;


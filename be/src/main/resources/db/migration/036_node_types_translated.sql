CREATE OR REPLACE VIEW VGBC_NODE_TYPES_TRANSLATED AS
SELECT l.ID                                                        AS ID_LANGUAGE,
       l.ID || '-' || mu.ID                                        AS ID_VIEW,
       NVL2(mu.ID_NODE, l.ID || '-' || mu.ID_NODE, NULL)           AS ID_NODE_VIEW,
       NVL2(mu.ID_NODE_TYPE, l.ID || '-' || mu.ID_NODE_TYPE, NULL) AS ID_NODE_TYPE_VIEW,
       mu.*
FROM GBC_NODE_TYPES mu,
     GBC_LANGUAGE l;


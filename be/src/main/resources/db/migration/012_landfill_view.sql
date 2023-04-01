CREATE OR REPLACE VIEW VGBC_LANDFILL_TRANSLATED AS
select l.ID                                            AS ID_LANGUAGE,
       l.ID || '-' || la.ID                             AS ID_VIEW,
       NVL2(la.ID_UNIT, l.ID || '-' || la.ID_UNIT, NULL) AS ID_UNIT_VIEW,
       la.*
from GBC_LANDFILL la,
     GBC_LANGUAGE l;

-- always at the bottom commit
commit;

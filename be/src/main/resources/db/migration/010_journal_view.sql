CREATE OR REPLACE VIEW VGBC_JOURNAL_TRANSLATED AS
select l.ID                                            AS ID_LANGUAGE,
       l.ID || '-' || j.ID                             AS ID_VIEW,
       NVL2(j.ID_TYPE, l.ID || '-' || j.ID_TYPE, NULL) AS ID_JOURNAL_TYPE_VIEW,
       j.*
from GBC_JOURNAL j,
     GBC_LANGUAGE l;

-- always at the bottom commit
commit;
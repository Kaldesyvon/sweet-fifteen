create or replace view VGBC_NODE_DATALOCK_TRANSLATED as
select l.ID                                                     as ID_LANGUAGE,
       l.ID || '-' || nd.ID                                      as ID_VIEW,
       l.ID || '-' || nd.ID                                      as ID_NODE_VIEW,
       nd.*
from GBC_NODE_DATALOCK nd,
     GBC_LANGUAGE l;

-- always at the bottom commit
commit;

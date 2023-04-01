create or replace view VGBC_AUTHORIZATION_TRANSLATED  as
select
    l.ID                                       as ID_LANGUAGE,
    l.ID || '-' || a.ID                      as ID_VIEW,
    NVL2(a.ID_NODE, l.ID || '-' || a.ID_NODE, NULL)  as ID_NODE_VIEW,
    NVL2(a.ID_USER, l.ID || '-' || a.ID_USER, NULL)  as ID_USER_VIEW,
    NVL2(a.ID_ROLE, l.ID || '-' || a.ID_ROLE, NULL)  as ID_ROLE_VIEW,
    a.*
from GBC_AUTHORIZATION a, GBC_LANGUAGE l;

-- always at the bottom commit
commit;
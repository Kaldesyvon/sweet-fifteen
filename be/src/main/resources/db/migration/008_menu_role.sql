create or replace view VGBC_MENU_ROLE_TRANSLATED as
select l.ID                                            as ID_LANGUAGE,
       l.ID || '-' || u.ID                             as ID_VIEW,
       nvl2(u.ID_MENU, l.ID || '-' || u.ID_MENU, null) as ID_MENU_VIEW,
       nvl2(u.ID_ROLE, l.ID || '-' || u.ID_ROLE, null) as ID_ROLE_VIEW,
       u.*
from GBC_MENU_ROLE u,
     GBC_LANGUAGE l;



commit;

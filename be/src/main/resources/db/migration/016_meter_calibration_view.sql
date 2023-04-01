create or replace view VGBC_METER_CALIBRATION_TRANSLATED as
select l.ID                                                     as ID_LANGUAGE,
       l.ID || '-' || m.ID                                      as ID_VIEW,
       l.ID || '-' || m.ID_METER                                      as ID_METER_VIEW,
       m.*
from GBC_METER_CALIBRATION m,
     GBC_LANGUAGE l;

-- always at the bottom commit
commit;
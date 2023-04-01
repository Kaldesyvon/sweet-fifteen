create or replace view VGBC_WI_ANALYSIS_TRANSLATED as
select l.ID                                              as ID_LANGUAGE,
       l.ID || '-' || a.ID                               as ID_VIEW,
       NVL2(a.ID_SCOPE, l.ID || '-' || a.ID_SCOPE, NULL) as ID_SCOPE_VIEW,
       NVL2(a.ID_AP, l.ID || '-' || a.ID_AP, NULL) as ID_AP_VIEW,
       NVL2(a.ID_M_PRODUCED, l.ID || '-' || a.ID_M_PRODUCED, NULL) as ID_M_PRODUCED_VIEW,
       a.*
from GBC_WI_ANALYSIS a,
     GBC_LANGUAGE l;


create or replace view VGBC_WI_ANALYSIS_NODE_TRANSLATED as
select l.ID                                              as ID_LANGUAGE,
       l.ID || '-' || an.ID_WI_A                         as ID_VIEW_A,
       l.ID || '-' || an.ID_NODE                         as ID_VIEW_NODE,
       an.*
from GBC_WI_ANALYSIS_NODE an,
     GBC_LANGUAGE l;


insert into GBC_MENU(ID, ID_MENU, NAME_K, MEMO_K, ENABLED, ITEM_URL, MENU_ORDER, ITEM_URL_N)
values (MENU_SEQ.nextval, 5, 'menu.wi.analysis', 'menu.wi.analysis.memo', 1, null, 20, null);

INSERT INTO GBC_DICTIONARY (ID_LANGUAGE, KEY, TRANSLATION, OBJ_VERSION, ID, CREATED_BY, CREATED)
VALUES (1, 'menu.wi.analysis', 'What-if analysis', 0, DICTIONARY_UI_SEQ.nextval, 'sql', sysdate);
INSERT INTO GBC_DICTIONARY (ID_LANGUAGE, KEY, TRANSLATION, OBJ_VERSION, ID, CREATED_BY, CREATED)
VALUES (2, 'menu.wi.analysis', 'What-if analýza', 0, DICTIONARY_UI_SEQ.nextval, 'sql', sysdate);


INSERT INTO GBC_DICTIONARY (ID_LANGUAGE, KEY, TRANSLATION, OBJ_VERSION, ID, CREATED_BY, CREATED)
VALUES (1, 'menu.wi.analysis.memo', 'What-if analysis memo', 0, DICTIONARY_UI_SEQ.nextval, 'sql', sysdate);
INSERT INTO GBC_DICTIONARY (ID_LANGUAGE, KEY, TRANSLATION, OBJ_VERSION, ID, CREATED_BY, CREATED)
VALUES (2, 'menu.wi.analysis.memo', 'What-if analýza popis', 0, DICTIONARY_UI_SEQ.nextval, 'sql', sysdate);

insert into GBC_MENU_ROLE(ID, ID_MENU, ID_ROLE, ALL_PLANTS)
values (MENU_ROLE_SEQ.nextval, (select id from GBC_MENU where NAME_K = 'menu.wi.analysis'), 1, null);

insert into GBC_SCREEN(ID, CODE, TITLE_K, HELPLABEL_K, ID_MENU)
VALUES ((select max(id + 1) from GBC_SCREEN), 'GP278', 'screen.wi.analysis', 'screen.wi.analysis.help',
        (select id from GBC_MENU where NAME_K = 'menu.wi.analysis'));

INSERT INTO GBC_DICTIONARY (ID_LANGUAGE, KEY, TRANSLATION, OBJ_VERSION, ID, CREATED_BY, CREATED)
VALUES (1, 'screen.wi.analysis', 'What-if analysis', 0, DICTIONARY_UI_SEQ.nextval, 'sql', sysdate);
INSERT INTO GBC_DICTIONARY (ID_LANGUAGE, KEY, TRANSLATION, OBJ_VERSION, ID, CREATED_BY, CREATED)
VALUES (2, 'screen.wi.analysis', 'What-if analýza', 0, DICTIONARY_UI_SEQ.nextval, 'sql', sysdate);


INSERT INTO GBC_DICTIONARY (ID_LANGUAGE, KEY, TRANSLATION, OBJ_VERSION, ID, CREATED_BY, CREATED)
VALUES (1, 'screen.wi.analysis.help', 'What-if analysis help', 0, DICTIONARY_UI_SEQ.nextval, 'sql', sysdate);
INSERT INTO GBC_DICTIONARY (ID_LANGUAGE, KEY, TRANSLATION, OBJ_VERSION, ID, CREATED_BY, CREATED)
VALUES (2, 'screen.wi.analysis.help', 'What-if analýza help', 0, DICTIONARY_UI_SEQ.nextval, 'sql', sysdate);

-- todo uncomment after implementation
-- update GBC_MENU set ITEM_URL_N = '/administration/wi-analysis' where ID = (select id from GBC_MENU where NAME_K = 'menu.wi.analysis');

-- always at the bottom commit
commit;

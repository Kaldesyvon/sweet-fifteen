-- create translated column
ALTER TABLE GBC_MATERIAL
    ADD NAME_K varchar2(200);
-- migrate original material data
update GBC_MATERIAL g
set g.NAME_K = regexp_replace(convert('material.' || g.NAME, 'US7ASCII'), '[[:space:]]+', '')
where g.NAME_K is null;

ALTER TABLE GBC_MATERIAL
    MODIFY NAME_K not null;


-- create GBC_DICTIONARY_TABLE insert
insert into GBC_DICTIONARY_TABLE(ID, TABLE_NAME)
select DICTIONARY_TABLE_SEQ.nextval, 'GBC_MATERIAL'
from dual
where not exists(select *
                 from GBC_DICTIONARY_TABLE
                 where (TABLE_NAME = 'GBC_MATERIAL'));


--create nameK column in MATERIAL_FL
ALTER TABLE GBC_MATERIAL_FL
    ADD NAME_K varchar2(200);
-- migrate keys
update GBC_MATERIAL_FL p
set p.NAME_K = (select m.NAME_K from GBC_MATERIAL m where m.ID = p.ID_MASTER)
where p.NAME_K is null;

-- migrate GBC_MATERIAL_FL data into GBC_DICTIONARY
insert into GBC_DICTIONARY(ID, ID_LANGUAGE, KEY, TRANSLATION, CREATED_BY, CREATED,
                           ID_DICTIONARY_TABLE)
select DICTIONARY_SEQ.nextval,
       fl.ID_LANGUAGE,
       fl.NAME_K,
       fl.NAME,
       fl.CREATED_BY,
       fl.CREATED,
       (select ID from GBC_DICTIONARY_TABLE where TABLE_NAME = 'GBC_MATERIAL')
from GBC_MATERIAL_FL fl;

-- migrate GBC_MATERIAL data into GBC_DICTIONARY
insert into GBC_DICTIONARY(ID, ID_LANGUAGE, KEY, TRANSLATION, CREATED_BY, CREATED,
                           ID_DICTIONARY_TABLE)
select DICTIONARY_SEQ.nextval,
       1,
       fl.NAME_K,
       fl.NAME,
       fl.CREATED_BY,
       fl.CREATED,
       (select ID from GBC_DICTIONARY_TABLE where TABLE_NAME = 'GBC_MATERIAL')
from GBC_MATERIAL fl;


-- replace view
create or replace view VGBC_MATERIAL_TRANSLATED as
select
    l.ID                                       as ID_LANGUAGE,
    l.ID || '-' || m.ID                        as ID_VIEW,
    coalesce((select TRANSLATION
              from GBC_DICTIONARY
              where KEY = m.NAME_K
                and ID_LANGUAGE = 1 -- default EN language key
             ),
             nvl2(m.NAME_K, '???' || m.NAME_K || '???', null)) as NAME_EN,         -- or return name key
    coalesce((select TRANSLATION
              from GBC_DICTIONARY
              where KEY = m.NAME_K
                and ID_LANGUAGE = l.ID), (select TRANSLATION
                                          from GBC_DICTIONARY
                                          where KEY = m.NAME_K
                                            and ID_LANGUAGE = 1 -- default EN language key
             ),
             nvl2(m.NAME_K, '???' || m.NAME_K || '???', null)) as NAME_TRANSLATED, -- or return name key
    NVL2(m.ID_MATERIAL, l.ID || '-' || m.ID_MATERIAL, NULL)               as ID_PARENT_MATERIAL_VIEW,
    NVL2(m.ID_UNIT_TYPE, l.ID || '-' || m.ID_UNIT_TYPE, NULL)              as ID_UNIT_TYPE_VIEW,
    NVL2(m.ID_USEPA_MATERIAL_TYPE, l.ID || '-' || m.ID_USEPA_MATERIAL_TYPE, NULL)    as ID_USEPA_MATERIAL_TYPE_VIEW,
    m.*
from GBC_MATERIAL m, GBC_LANGUAGE l;


-- update trigger trigger
create or replace trigger TRG_BIUD_MATERIAL_AUDIT
    before insert or update of ID,CODE,NAME,NAME_K,MEMO,ID_UNIT_TYPE,PRODUCT,REPORT_POS,MATERIAL_REPORT,ID_MATERIAL,MATERIAL_GROUP,ID_USEPA_MATERIAL_TYPE or delete
    on GBC_MATERIAL
    for each row
DECLARE
    lv_detail   VARCHAR2(32767 BYTE);
    lv_iud      VARCHAR2(1 BYTE);
    lv_app_user VARCHAR2(20 BYTE);
BEGIN
    lv_app_user := USER;

    -- INSERT
    IF INSERTING THEN
        BEGIN
            lv_iud := 'I';

            lv_detail := 'ID: "' || TO_CHAR(:NEW.ID) || '"';
            lv_detail := lv_detail || ' CODE: "' || :NEW.CODE || '"';
            lv_detail := lv_detail || ' NAME: "' || :NEW.NAME || '"';
            lv_detail := lv_detail || ' NAME_K: "' || :NEW.NAME_K || '"';
            lv_detail := lv_detail || ' MEMO: "' || :NEW.MEMO || '"';
            lv_detail := lv_detail || ' ID_UNIT_TYPE: "' || TO_CHAR(:NEW.ID_UNIT_TYPE) || '"';
            lv_detail := lv_detail || ' PRODUCT: "' || TO_CHAR(:NEW.PRODUCT) || '"';
            lv_detail := lv_detail || ' REPORT_POS: "' || TO_CHAR(:NEW.REPORT_POS) || '"';
            lv_detail := lv_detail || ' MATERIAL_REPORT: "' || TO_CHAR(:NEW.MATERIAL_REPORT) || '"';
            lv_detail := lv_detail || ' ID_MATERIAL: "' || TO_CHAR(:NEW.ID_MATERIAL) || '"';
            lv_detail := lv_detail || ' MATERIAL_GROUP: "' || TO_CHAR(:NEW.MATERIAL_GROUP) || '"';
            lv_detail := lv_detail || ' ID_USEPA_MATERIAL_TYPE: "' || TO_CHAR(:NEW.ID_USEPA_MATERIAL_TYPE) || '"';

            lv_app_user := :NEW.CREATED_BY;
        END;
    END IF;

    -- UPDATE
    IF UPDATING THEN
        BEGIN
            lv_iud := 'U';


            IF UPDATING('ID') AND :NEW.ID <> :OLD.ID THEN lv_detail := lv_detail || ' ID: "' || TO_CHAR(:OLD.ID) || '"->"' || TO_CHAR(:NEW.ID) || '"'; ELSE lv_detail := lv_detail || ' ID: "' || TO_CHAR(:OLD.ID) || '"'; END IF;
            IF UPDATING('CODE') AND :NEW.CODE <> :OLD.CODE THEN lv_detail := lv_detail || ' CODE: "' || :OLD.CODE || '"->"' || :NEW.CODE || '"'; END IF;
            IF UPDATING('NAME_K') AND :NEW.NAME_K <> :OLD.NAME_K THEN lv_detail := lv_detail || ' NAME_K: "' || :OLD.NAME_K || '"->"' || :NEW.NAME_K || '"'; END IF;
            IF UPDATING('NAME') AND :NEW.NAME <> :OLD.NAME THEN lv_detail := lv_detail || ' NAME: "' || :OLD.NAME || '"->"' || :NEW.NAME || '"'; END IF;
            IF UPDATING('MEMO') AND (:NEW.MEMO <> :OLD.MEMO OR (:NEW.MEMO IS NOT NULL AND :OLD.MEMO IS NULL) OR (:NEW.MEMO IS NULL AND :OLD.MEMO IS NOT NULL)) THEN lv_detail := lv_detail || ' MEMO: "' || :OLD.MEMO || '"->"' || :NEW.MEMO || '"'; END IF;
            IF UPDATING('ID_UNIT_TYPE') AND (:NEW.ID_UNIT_TYPE <> :OLD.ID_UNIT_TYPE OR (:NEW.ID_UNIT_TYPE IS NOT NULL AND :OLD.ID_UNIT_TYPE IS NULL) OR (:NEW.ID_UNIT_TYPE IS NULL AND :OLD.ID_UNIT_TYPE IS NOT NULL)) THEN lv_detail := lv_detail || ' ID_UNIT_TYPE: "' || TO_CHAR(:OLD.ID_UNIT_TYPE) || '"->"' || TO_CHAR(:NEW.ID_UNIT_TYPE) || '"'; END IF;
            IF UPDATING('PRODUCT') AND :NEW.PRODUCT <> :OLD.PRODUCT THEN lv_detail := lv_detail || ' PRODUCT: "' || TO_CHAR(:OLD.PRODUCT) || '"->"' || TO_CHAR(:NEW.PRODUCT) || '"'; END IF;
            IF UPDATING('REPORT_POS') AND (:NEW.REPORT_POS <> :OLD.REPORT_POS OR (:NEW.REPORT_POS IS NOT NULL AND :OLD.REPORT_POS IS NULL) OR (:NEW.REPORT_POS IS NULL AND :OLD.REPORT_POS IS NOT NULL)) THEN lv_detail := lv_detail || ' REPORT_POS: "' || TO_CHAR(:OLD.REPORT_POS) || '"->"' || TO_CHAR(:NEW.REPORT_POS) || '"'; END IF;
            IF UPDATING('MATERIAL_REPORT') AND (:NEW.MATERIAL_REPORT <> :OLD.MATERIAL_REPORT OR (:NEW.MATERIAL_REPORT IS NOT NULL AND :OLD.MATERIAL_REPORT IS NULL) OR (:NEW.MATERIAL_REPORT IS NULL AND :OLD.MATERIAL_REPORT IS NOT NULL)) THEN lv_detail := lv_detail || ' MATERIAL_REPORT: "' || TO_CHAR(:OLD.MATERIAL_REPORT) || '"->"' || TO_CHAR(:NEW.MATERIAL_REPORT) || '"'; END IF;
            IF UPDATING('ID_MATERIAL') AND (:NEW.ID_MATERIAL <> :OLD.ID_MATERIAL OR (:NEW.ID_MATERIAL IS NOT NULL AND :OLD.ID_MATERIAL IS NULL) OR (:NEW.ID_MATERIAL IS NULL AND :OLD.ID_MATERIAL IS NOT NULL)) THEN lv_detail := lv_detail || ' ID_MATERIAL: "' || TO_CHAR(:OLD.ID_MATERIAL) || '"->"' || TO_CHAR(:NEW.ID_MATERIAL) || '"'; END IF;
            IF UPDATING('MATERIAL_GROUP') AND :NEW.MATERIAL_GROUP <> :OLD.MATERIAL_GROUP THEN lv_detail := lv_detail || ' MATERIAL_GROUP: "' || TO_CHAR(:OLD.MATERIAL_GROUP) || '"->"' || TO_CHAR(:NEW.MATERIAL_GROUP) || '"'; END IF;
            IF UPDATING('ID_USEPA_MATERIAL_TYPE') AND (:NEW.ID_USEPA_MATERIAL_TYPE <> :OLD.ID_USEPA_MATERIAL_TYPE OR (:NEW.ID_USEPA_MATERIAL_TYPE IS NOT NULL AND :OLD.ID_USEPA_MATERIAL_TYPE IS NULL) OR (:NEW.ID_USEPA_MATERIAL_TYPE IS NULL AND :OLD.ID_USEPA_MATERIAL_TYPE IS NOT NULL)) THEN lv_detail := lv_detail || ' ID_USEPA_MATERIAL_TYPE: "' || TO_CHAR(:OLD.ID_USEPA_MATERIAL_TYPE) || '"->"' || TO_CHAR(:NEW.ID_USEPA_MATERIAL_TYPE) || '"'; END IF;

            IF SUBSTR(lv_detail,1,1) = ' ' THEN lv_detail := SUBSTR(lv_detail,2); END IF;

            IF UPDATING('MODIFIED_BY') THEN lv_app_user := :NEW.MODIFIED_BY; ELSE lv_app_user := NVL(:OLD.MODIFIED_BY, USER); END IF;
        END;
    END IF;

    -- DELETE
    IF DELETING THEN
        BEGIN
            lv_iud := 'D';

            lv_detail := 'ID: "' || TO_CHAR(:OLD.ID) || '"';
            lv_detail := lv_detail || ' CODE: "' || :OLD.CODE || '"';
            lv_detail := lv_detail || ' NAME: "' || :OLD.NAME || '"';
            lv_detail := lv_detail || ' NAME_K: "' || :OLD.NAME_K || '"';
            lv_detail := lv_detail || ' MEMO: "' || :OLD.MEMO || '"';
            lv_detail := lv_detail || ' ID_UNIT_TYPE: "' || TO_CHAR(:OLD.ID_UNIT_TYPE) || '"';
            lv_detail := lv_detail || ' PRODUCT: "' || TO_CHAR(:OLD.PRODUCT) || '"';
            lv_detail := lv_detail || ' REPORT_POS: "' || TO_CHAR(:OLD.REPORT_POS) || '"';
            lv_detail := lv_detail || ' MATERIAL_REPORT: "' || TO_CHAR(:OLD.MATERIAL_REPORT) || '"';
            lv_detail := lv_detail || ' ID_MATERIAL: "' || TO_CHAR(:OLD.ID_MATERIAL) || '"';
            lv_detail := lv_detail || ' MATERIAL_GROUP: "' || TO_CHAR(:OLD.MATERIAL_GROUP) || '"';
            lv_detail := lv_detail || ' ID_USEPA_MATERIAL_TYPE: "' || TO_CHAR(:OLD.ID_USEPA_MATERIAL_TYPE) || '"';
        END;
    END IF;

    -- insert audit record
    INSERT INTO GBC_JOURNAL (ID, DETAIL, CREATED_BY, CREATED, ID_TYPE, IUD)
    VALUES (JOURNAL_SEQ.NEXTVAL, SUBSTR(lv_detail,1,4000), lv_app_user, SYSTIMESTAMP, 18, lv_iud);
END;
/

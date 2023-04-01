insert into GBC_PARAM (CODE, VALUE, DATE_VALUE, CREATED_BY, CREATED, MODIFIED_BY, MODIFIED, EDITABLE, OBJ_VERSION, ID)
values ('param.emailNotiEnabled', '1', null, 'admin', sysdate, 'admin', sysdate, 1, 0,
        (select max(param.ID + 1)
         from GBC_PARAM param));

insert into GBC_PARAM (CODE, VALUE, DATE_VALUE, CREATED_BY, CREATED, MODIFIED_BY, MODIFIED, EDITABLE, OBJ_VERSION, ID)
values ('param.emailNotiTestAddress', 'sima@esten.sk', null, 'admin', sysdate, 'admin', sysdate, 1, 0,
        (select max(param.ID + 1)
         from GBC_PARAM param));


INSERT INTO GBC_DICTIONARY (ID_LANGUAGE, KEY, TRANSLATION, CREATED_BY, CREATED, OBJ_VERSION, ID)
VALUES (1, 'param.emailNotiEnabled', 'Globally toggle sending emails form application', 'admin', sysdate, 0, DICTIONARY_SEQ.nextval);
INSERT INTO GBC_DICTIONARY (ID_LANGUAGE, KEY, TRANSLATION, CREATED_BY, CREATED, OBJ_VERSION, ID)
VALUES (2, 'param.emailNotiEnabled', 'Globálne vypnutie/zapnutie odosielania e-mailov z aplikácie', 'admin', sysdate, 0, DICTIONARY_SEQ.nextval);

INSERT INTO GBC_DICTIONARY(ID_LANGUAGE, KEY, TRANSLATION, CREATED_BY, CREATED, OBJ_VERSION, ID)
VALUES (1, 'param.emailNotiTestAddress', 'Sending email to test addresses instead of original, use delimiter ;', 'admin', sysdate, 0, DICTIONARY_SEQ.nextval);
INSERT INTO GBC_DICTIONARY (ID_LANGUAGE, KEY, TRANSLATION, CREATED_BY, CREATED, OBJ_VERSION, ID)
VALUES (2, 'param.emailNotiTestAddress', 'Odosielanie e-mailu na testovacie adresy namiesto pôvodných, použite oddeľovač ;', 'admin', sysdate, 0, DICTIONARY_SEQ.nextval);

commit ;

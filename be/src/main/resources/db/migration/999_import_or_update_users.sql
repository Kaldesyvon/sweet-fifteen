create table user_import_tmp
(
    email     varchar2(100),
    ad_login  varchar2(100),
    firstname varchar2(100),
    lastname  varchar2(100),
    ext       number(1) default 0
);

insert into user_import_tmp
values ('cta2232@sk.uss.com', 'cta2232', 'Vladimir', 'Sima', 1);

insert into user_import_tmp
values ('cta2633@sk.uss.com', 'cta2633', 'Lubomir', 'Druga', 1);

insert into user_import_tmp
values ('cta2696@sk.uss.com', 'cta2696', 'Martin', 'Fekete', 1);

insert into user_import_tmp
values ('cta2690@sk.uss.com', 'cta2690', 'Robert', 'Jacko', 1);

insert into user_import_tmp
values ('cta2697@sk.uss.com', 'cta2697', 'Erik', 'Novysedlak', 1);

insert into user_import_tmp
values ('hor7957@sk.uss.com', 'hor7957', 'Pavol', 'Hornak', 1);



insert into user_import_tmp
values ('rzaboj@sk.uss.com', 'zab1274', 'Roman', 'Zaboj', 0);

insert into user_import_tmp
values ('abari@sk.uss.com', 'bar4352', 'Arpad', 'Bari', 0);

insert into user_import_tmp
values ('mcibula@sk.uss.com', 'cib1522', 'Martin', 'Cibula', 0);

insert into user_import_tmp
values ('jbecker@uss.com', 'beckerj', 'Jeffrey', 'Becker', 0);

insert into user_import_tmp
values ('mvida@sk.uss.com', 'vid6274', 'Marko', 'Vida', 0);

insert into user_import_tmp
values ('jansimko@sk.uss.com', 'sim2590', 'Jan', 'Simko', 0);

insert into user_import_tmp
values ('jgamrat@sk.uss.com', 'gam6377', 'Jan', 'Gamrat', 0);

insert into user_import_tmp
values ('kvojacek@sk.uss.com', 'voj9088', 'Katarina', 'Vojacek', 0);


commit;

BEGIN
    FOR rec IN (select * from user_import_tmp ui where ui.ad_login not in (select u.LOGIN from GBC_USER u))
        LOOP
            dbms_output.put_line('Insert: ' || rec.ad_login);
            INSERT INTO GBC_USER (ID, ID_NODE, LOGIN, LOGIN_N, NAME, SURNAME, FUNCTION, ENABLED, EMAIL, PHONE,
                                  CREATED_BY, CREATED,
                                  MODIFIED_BY, MODIFIED, ID_LANGUAGE, ID_NODE_DEF, TIMEZONE, SKIN, OBJ_VERSION,
                                  MAIL_NOTIFICATION,
                                  ID_UNIT_SET, ID_SCOPE_DEF, ID_ANALYSIS_PARAM_DEF)
            VALUES (USER_SEQ.nextval, 1620, rec.ad_login, rec.email, rec.firstname, rec.lastname, 'KE developer', 1,
                    null, '730808', 'CO2',
                    sysdate, rec.email,
                    sysdate, 2, 1412, 'Europe/Prague',
                    'phase5', 23,
                    1, 2, 1553, 4);
            COMMIT;

            INSERT INTO GBC_AUTHORIZATION (ID, ID_USER, ID_ROLE, ID_NODE, CREATED_BY, CREATED, MODIFIED_BY,
                                           MODIFIED, OBJ_VERSION)
            VALUES (AUTHORIZATION_SEQ.nextval, (SELECT ID FROM GBC_USER WHERE LOGIN = rec.ad_login), 1, NULL, 'SQL',
                    sysdate, NULL, NULL, 0);
            INSERT INTO GBC_AUTHORIZATION (ID, ID_USER, ID_ROLE, ID_NODE, CREATED_BY, CREATED, MODIFIED_BY,
                                           MODIFIED, OBJ_VERSION)
            VALUES (AUTHORIZATION_SEQ.nextval, (SELECT ID FROM GBC_USER WHERE LOGIN = rec.ad_login), 2, NULL, 'SQL',
                    sysdate, NULL, NULL, 0);
            INSERT INTO GBC_AUTHORIZATION (ID, ID_USER, ID_ROLE, ID_NODE, CREATED_BY, CREATED, MODIFIED_BY,
                                           MODIFIED, OBJ_VERSION)
            VALUES (AUTHORIZATION_SEQ.nextval, (SELECT ID FROM GBC_USER WHERE LOGIN = rec.ad_login), 3, NULL, 'SQL',
                    sysdate, NULL, NULL, 0);
            INSERT INTO GBC_AUTHORIZATION (ID, ID_USER, ID_ROLE, ID_NODE, CREATED_BY, CREATED, MODIFIED_BY,
                                           MODIFIED, OBJ_VERSION)
            VALUES (AUTHORIZATION_SEQ.nextval, (SELECT ID FROM GBC_USER WHERE LOGIN = rec.ad_login), 4, NULL, 'SQL',
                    sysdate, NULL, NULL, 0);
            INSERT INTO GBC_AUTHORIZATION (ID, ID_USER, ID_ROLE, ID_NODE, CREATED_BY, CREATED, MODIFIED_BY,
                                           MODIFIED, OBJ_VERSION)
            VALUES (AUTHORIZATION_SEQ.nextval, (SELECT ID FROM GBC_USER WHERE LOGIN = rec.ad_login), 5, NULL, 'SQL',
                    sysdate, NULL, NULL, 0);
            INSERT INTO GBC_AUTHORIZATION (ID, ID_USER, ID_ROLE, ID_NODE, CREATED_BY, CREATED, MODIFIED_BY,
                                           MODIFIED, OBJ_VERSION)
            VALUES (AUTHORIZATION_SEQ.nextval, (SELECT ID FROM GBC_USER WHERE LOGIN = rec.ad_login), 6, NULL, 'SQL',
                    sysdate, NULL, NULL, 0);
            INSERT INTO GBC_AUTHORIZATION (ID, ID_USER, ID_ROLE, ID_NODE, CREATED_BY, CREATED, MODIFIED_BY,
                                           MODIFIED, OBJ_VERSION)
            VALUES (AUTHORIZATION_SEQ.nextval, (SELECT ID FROM GBC_USER WHERE LOGIN = rec.ad_login), 7, NULL, 'SQL',
                    sysdate, NULL, NULL, 0);
            INSERT INTO GBC_AUTHORIZATION (ID, ID_USER, ID_ROLE, ID_NODE, CREATED_BY, CREATED, MODIFIED_BY,
                                           MODIFIED, OBJ_VERSION)
            VALUES (AUTHORIZATION_SEQ.nextval, (SELECT ID FROM GBC_USER WHERE LOGIN = rec.ad_login), 8, NULL, 'SQL',
                    sysdate, NULL, NULL, 0);
            INSERT INTO GBC_AUTHORIZATION (ID, ID_USER, ID_ROLE, ID_NODE, CREATED_BY, CREATED, MODIFIED_BY,
                                           MODIFIED, OBJ_VERSION)
            VALUES (AUTHORIZATION_SEQ.nextval, (SELECT ID FROM GBC_USER WHERE LOGIN = rec.ad_login), 9, NULL, 'SQL',
                    sysdate, NULL, NULL, 0);
            INSERT INTO GBC_AUTHORIZATION (ID, ID_USER, ID_ROLE, ID_NODE, CREATED_BY, CREATED, MODIFIED_BY,
                                           MODIFIED, OBJ_VERSION)
            VALUES (AUTHORIZATION_SEQ.nextval, (SELECT ID FROM GBC_USER WHERE LOGIN = rec.ad_login), 10, NULL,
                    'SQL',
                    sysdate, NULL, NULL, 0);
            INSERT INTO GBC_AUTHORIZATION (ID, ID_USER, ID_ROLE, ID_NODE, CREATED_BY, CREATED, MODIFIED_BY,
                                           MODIFIED, OBJ_VERSION)
            VALUES (AUTHORIZATION_SEQ.nextval, (SELECT ID FROM GBC_USER WHERE LOGIN = rec.ad_login), 11, NULL,
                    'SQL',
                    sysdate, NULL, NULL, 0);
            INSERT INTO GBC_AUTHORIZATION (ID, ID_USER, ID_ROLE, ID_NODE, CREATED_BY, CREATED, MODIFIED_BY,
                                           MODIFIED, OBJ_VERSION)
            VALUES (AUTHORIZATION_SEQ.nextval, (SELECT ID FROM GBC_USER WHERE LOGIN = rec.ad_login), 12, NULL,
                    'SQL',
                    sysdate, NULL, NULL, 0);
            INSERT INTO GBC_AUTHORIZATION (ID, ID_USER, ID_ROLE, ID_NODE, CREATED_BY, CREATED, MODIFIED_BY,
                                           MODIFIED, OBJ_VERSION)
            VALUES (AUTHORIZATION_SEQ.nextval, (SELECT ID FROM GBC_USER WHERE LOGIN = rec.ad_login), 13, NULL,
                    'SQL',
                    sysdate, NULL, NULL, 0);

            COMMIT;

        END LOOP;

END;


BEGIN
    FOR rec IN (select * from user_import_tmp ui where ui.ad_login in (select u.LOGIN from GBC_USER u))
        LOOP
            dbms_output.put_line('Update: ' || rec.ad_login);
            update GBC_USER u set u.LOGIN_N = rec.email, u.ENABLED = 1 where u.LOGIN = rec.ad_login;
            commit;
        END LOOP;

END;

drop table user_import_tmp;

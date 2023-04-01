drop table GBC_DICTIONARY_UI;
create table GBC_DICTIONARY_UI
(
    ID          NUMBER(18)           not null
        constraint GBC_DICTIONARY_UI_PK
            primary key,
    ID_LANGUAGE NUMBER(18),
    KEY         VARCHAR2(80),
    TRANSLATION VARCHAR2(2000)       not null,
    CREATED_BY  VARCHAR2(20)         not null,
    CREATED     TIMESTAMP(6)         not null,
    MODIFIED_BY VARCHAR2(20),
    MODIFIED    TIMESTAMP(6),
    OBJ_VERSION NUMBER(18) default 0 not null
);

comment on table GBC_DICTIONARY_UI is 'Dictionary of static text displayed on pages, forms and dialogs.';
comment on column GBC_DICTIONARY_UI.ID is 'Primary key';
comment on column GBC_DICTIONARY_UI.ID_LANGUAGE is 'Language foreign key';
comment on column GBC_DICTIONARY_UI.KEY is 'Unique key for translation';
comment on column GBC_DICTIONARY_UI.TRANSLATION is 'Translation';
comment on column GBC_DICTIONARY_UI.CREATED_BY is 'Login of user who created the record';
comment on column GBC_DICTIONARY_UI.CREATED is 'Record creation date';
comment on column GBC_DICTIONARY_UI.MODIFIED_BY is 'Login of user who modified the record.';
comment on column GBC_DICTIONARY_UI.MODIFIED is 'Record modification date';
comment on column GBC_DICTIONARY_UI.OBJ_VERSION is 'Object version';

drop sequence DICTIONARY_UI_SEQ;
create sequence DICTIONARY_UI_SEQ nocache;

-- migrate data
insert into GBC_DICTIONARY_UI(ID,
                              ID_LANGUAGE,
                              KEY,
                              TRANSLATION,
                              CREATED_BY,
                              CREATED,
                              MODIFIED_BY,
                              MODIFIED,
                              OBJ_VERSION)
select (DICTIONARY_UI_SEQ.nextval),
       d.ID_LANGUAGE,
       d.KEY,
       d.TRANSLATION,
       d.CREATED_BY,
       d.CREATED,
       d.MODIFIED_BY,
       d.MODIFIED,
       d.OBJ_VERSION
from GBC_DICTIONARY d
where regexp_like(d.KEY, '^[^.]+$');

-- create view for dictionary ui
create or replace view VGBC_DICTIONARY_UI_TRANSLATED as
select l.ID                                                 as ID_LANGUAGE,
       l.ID || '-' || d.ID                                  as ID_VIEW,
       coalesce((select TRANSLATION
                 from GBC_DICTIONARY_UI
                 where KEY = d.KEY
                   and ID_LANGUAGE = l.ID), (select TRANSLATION
                                             from GBC_DICTIONARY_UI
                                             where KEY = d.KEY
                                               and ID_LANGUAGE = 1 -- default EN language key
                ),
                nvl2(d.KEY, '???' || d.KEY || '???', null)) as VALUE_TRANSLATED, -- or return name key
       d.ID,
       d.TRANSLATION,
       d.KEY,
       d.CREATED_BY,
       d.CREATED,
       d.MODIFIED_BY,
       d.MODIFIED,
       d.OBJ_VERSION
from GBC_DICTIONARY_UI d,
     GBC_LANGUAGE l
where d.ID_LANGUAGE = 1;



-- always at the bottom commit
commit;

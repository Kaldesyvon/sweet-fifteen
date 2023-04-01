create table GBC_REPORTED_ISSUES_N
(
    ID            NUMBER         not null,
    TYPE        VARCHAR2(20)   not null,
    ISSUE_COMMENT VARCHAR2(4000) not null,
    PRIORITY    VARCHAR2(30)   not null,
    CREATED_BY    VARCHAR2(30)   not null,
    CREATED_DATE  TIMESTAMP(6)   not null,
    STATE       VARCHAR2(30)   not null,
    FIXED_BY      VARCHAR2(30),
    FIXED_DATE    TIMESTAMP(6),
    FIXED_COMMENT VARCHAR2(4000),
    SCREEN        VARCHAR2(100)  not null,
    constraint GBC_REPORTED_ISSUES_N_PK
        primary key (ID),
    constraint GBC_REPORTED_ISSUES_N_UK1
        unique (ISSUE_COMMENT)
);

comment on column GBC_REPORTED_ISSUES_N.ID is 'Primary key';

comment on column GBC_REPORTED_ISSUES_N.TYPE is 'Type of issue';

comment on column GBC_REPORTED_ISSUES_N.ISSUE_COMMENT is 'Issue - description/comment by user';

comment on column GBC_REPORTED_ISSUES_N.PRIORITY is 'Priority';

comment on column GBC_REPORTED_ISSUES_N.CREATED_BY is 'Login of user who created the record';

comment on column GBC_REPORTED_ISSUES_N.CREATED_DATE is 'Record creation date';

comment on column GBC_REPORTED_ISSUES_N.STATE is 'Issue state';

comment on column GBC_REPORTED_ISSUES_N.FIXED_BY is 'Issue was fixed by';

comment on column GBC_REPORTED_ISSUES_N.FIXED_DATE is 'Date of fix';

comment on column GBC_REPORTED_ISSUES_N.FIXED_COMMENT is 'Fix comment';

comment on column GBC_REPORTED_ISSUES_N.SCREEN is 'Screen';

alter table GBC_REPORTED_ISSUES_N
    add constraint GBC_REPORTED_ISSUES_N_CHK1
        check ("TYPE" = 'BUG' OR "TYPE" = 'ISSUE' OR "TYPE" = 'ENHANCEMENT' OR "TYPE" = 'OTHER');

alter table GBC_REPORTED_ISSUES_N
    add constraint GBC_REPORTED_ISSUES_N_CHK2
        check ("PRIORITY" = 'LOW' OR "PRIORITY" = 'NORMAL' OR "PRIORITY" = 'HIGH');

alter table GBC_REPORTED_ISSUES_N
    add constraint GBC_REPORTED_ISSUES_N_CHK3
        check ("STATE" = 'NEW' OR "STATE" = 'ACCEPTED' OR "STATE" = 'FROZEN' OR "STATE" = 'RESOLVED' OR
               "STATE" = 'REFUSED');


create sequence REPORTED_ISSUES_N_SEQ nocache;


create table GBC_REPORTED_ISSUES_N_ATT
(
    ID              NUMBER not null,
    IS_ID           NUMBER not null,
    ATTACHMENT_NAME VARCHAR2(50),
    ATTACHMENT      BLOB,
    constraint GBC_REPORTED_ISSUES_N_ATT_PK
        primary key (ID),
    constraint GBC_REPORTED_ISSUES_N_ATT_FK
        foreign key (IS_ID) references GBC_REPORTED_ISSUES_N
);

comment on column GBC_REPORTED_ISSUES_N_ATT.ID is 'Primary key';

comment on column GBC_REPORTED_ISSUES_N_ATT.IS_ID is 'GBC_REPORTED_ISSUES_N_ATT foreign key';

comment on column GBC_REPORTED_ISSUES_N_ATT.ATTACHMENT_NAME is 'Attachment name';

comment on column GBC_REPORTED_ISSUES_N_ATT.ATTACHMENT is 'Attachment - binary content';

create sequence REPORTED_ISSUES_N_ATT_SEQ nocache;

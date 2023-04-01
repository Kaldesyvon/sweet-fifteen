-- zoznam analyz
create table GBC_WI_ANALYSIS
(
    ID            NUMBER(18)                   not null,
    NAME          VARCHAR2(200)                not null,
    DATE_FROM     TIMESTAMP(6)                 not null,
    DATE_TO       TIMESTAMP(6)                 not null,
    ID_SCOPE      NUMBER(18)                   not null,
    ID_AP         NUMBER(18)                   not null,
    ID_M_PRODUCED NUMBER(18),
    CREATED_BY    VARCHAR2(20)                 not null,
    CREATED       TIMESTAMP(6) default sysdate not null,
    MODIFIED_BY   VARCHAR2(20),
    MODIFIED      TIMESTAMP(6) default sysdate not null,
    OBJ_VERSION   NUMBER(18)                   not null,

    constraint GBC_WI_ANALYSIS_PK
        primary key (ID),
    constraint GBC_WI_ANALYSIS_FK1
        foreign key (ID_SCOPE) references GBC_SCOPE,
    constraint GBC_WI_ANALYSIS_FK2
        foreign key (ID_AP) references GBC_ANALYSIS_PARAM,
    constraint GBC_WI_ANALYSIS_FK3
        foreign key (ID_M_PRODUCED) references GBC_MATERIAL
);

create sequence WI_ANALYSIS_SEQ nocache;


-- zoznam analyz- filter nody
create table GBC_WI_ANALYSIS_NODE
(
    ID_WI_A     NUMBER(18)                   not null,
    ID_NODE     NUMBER(18)                   not null,
    CREATED_BY  VARCHAR2(20)                 not null,
    CREATED     TIMESTAMP(6) default sysdate not null,
    MODIFIED_BY VARCHAR2(20),
    MODIFIED    TIMESTAMP(6) default sysdate not null,
    OBJ_VERSION NUMBER(18)                   not null,
    constraint GBC_WI_ANALYSIS_NODE_FK1
        foreign key (ID_WI_A) references GBC_WI_ANALYSIS,
    constraint GBC_WI_ANALYSIS_NODE_FK2
        foreign key (ID_NODE) references GBC_NODE
);

-- zoskupenie/skupiny - 1 krok prava strana
create table GBC_WI_ANALYSIS_SOURCE_GRP
(
    ID          NUMBER(18)                   not null,
    SELF_GROUP  NUMBER(1)    default 0       not null, -- sam o sebe skupina
    NAME        VARCHAR2(200)                not null,
    CREATED_BY  VARCHAR2(20)                 not null,
    CREATED     TIMESTAMP(6) default sysdate not null,
    MODIFIED_BY VARCHAR2(20),
    MODIFIED    TIMESTAMP(6) default sysdate not null,
    OBJ_VERSION NUMBER(18)                   not null,

    constraint GBC_WI_ANALYSIS_SOURCE_GRP_PK
        primary key (ID)
);

create sequence WI_ANALYSIS_SOURCE_GRP_SEQ nocache;


-- snapshot vybratych dat podla tabuliek  hore, 1 krok, lava str
create table GBC_WI_ANALYSIS_SOURCE
(
    ID               NUMBER(18)                   not null,
    ID_WI_A          NUMBER(18)                   not null,
    ID_WI_S_GRP      NUMBER(18),
    ID_MATERIAL_NODE NUMBER(18)                   not null,
    IO               NUMBER(1)                    not null,
    QUANTITY     NUMBER                       not null,
    M_PRODUCED   NUMBER,
    CREATED_BY       VARCHAR2(20)                 not null,
    CREATED          TIMESTAMP(6) default sysdate not null,
    MODIFIED_BY      VARCHAR2(20),
    MODIFIED         TIMESTAMP(6) default sysdate not null,
    OBJ_VERSION      NUMBER(18)                   not null,

    constraint GBC_WI_ANALYSIS_SOURCE_PK
        primary key (ID),
    constraint GBC_WI_ANALYSIS_SOURCE_FK1
        foreign key (ID_WI_A) references GBC_WI_ANALYSIS,
    constraint GBC_WI_ANALYSIS_SOURCE_FK2
        foreign key (ID_WI_S_GRP) references GBC_WI_ANALYSIS_SOURCE_GRP,
    constraint GBC_WI_ANALYSIS_SOURCE_FK3
        foreign key (ID_MATERIAL_NODE) references GBC_MATERIAL_NODE
);

create sequence WI_ANALYSIS_SOURCE_SEQ nocache;

-- 2 krok zmeny
create table GBC_WI_ANALYSIS_GRP_MOD
(
    ID             NUMBER(18)                   not null,
    VERSION        NUMBER(18)   default 1       not null,
    ID_WI_S_GRP NUMBER(18), -- bud GBC_WI_GRP alebo NAME
    NAME           VARCHAR2(200),
    SUM_QUANTITY   NUMBER,
    SUN_M_PRODUCED   NUMBER,
    INDEX_NUM      NUMBER       default 1       not null,
    CREATED_BY     VARCHAR2(20)                 not null,
    CREATED        TIMESTAMP(6) default sysdate not null,
    MODIFIED_BY    VARCHAR2(20),
    MODIFIED       TIMESTAMP(6) default sysdate not null,
    OBJ_VERSION    NUMBER(18)                   not null,

    constraint GBC_WI_ANALYSIS_AP_MOD_PK
        primary key (ID),
    constraint GBC_WI_ANALYSIS_AP_MOD_FK1
        foreign key (ID_WI_S_GRP) references GBC_WI_ANALYSIS_SOURCE_GRP,
    constraint GBC_WI_ANALYSIS_AP_MOD_CHK1
        check ((ID_WI_S_GRP IS NULL AND NAME IS NOT NULL) OR
               (ID_WI_S_GRP IS NOT NULL AND NAME IS NULL))
);
create sequence WI_ANALYSIS_GRP_MOD_SEQ nocache;

commit;

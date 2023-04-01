create table GBC_MENU_FAV
(
    ID_MENU NUMBER(18) not null,
    ID_USER NUMBER(18) not null,
    constraint GBC_FAV_MENU_FK1
        foreign key (ID_MENU) references GBC_MENU,
    constraint GBC_FAV_MENU_FK2
        foreign key (ID_USER) references GBC_USER
)
/

ALTER TABLE GBC_MENU_FAV
    add CONSTRAINT GBC_FAV_MENU_UK1 UNIQUE (ID_MENU, ID_USER);

comment on table GBC_MENU_FAV is 'User`s favourite menu items'
/

commit;

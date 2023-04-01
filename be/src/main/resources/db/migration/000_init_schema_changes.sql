alter table GBC_MENU add ITEM_URL_N varchar2(250);
comment on column GBC_MENU.ITEM_URL_N is 'Menu item url used by spring boot (2023) application';

--
alter table GBC_USER
    add LOGIN_N varchar2(100);
update GBC_USER set LOGIN_N = LOGIN where LOGIN is not null;
alter table GBC_USER
    modify LOGIN_N not null ;
comment on column GBC_USER.LOGIN_N is 'User login used by spring boot (2023) application';
comment on column GBC_USER.LOGIN is 'User ldap login (AD login)';
CREATE UNIQUE INDEX GBC_USER_UK2 ON GBC_USER(LOGIN_N);


-- always at the bottom commit
commit;

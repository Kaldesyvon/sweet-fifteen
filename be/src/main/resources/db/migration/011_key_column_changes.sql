alter table GBC_MENU
    modify NAME_K VARCHAR2(70);
alter table GBC_BUSINESS_UNIT
    modify NAME_K VARCHAR2(70);
alter table GBC_SCOPE_FUEL_SPEC
    modify NAME_K VARCHAR2(70);
alter table GBC_SCOPE_PROCESS_SPEC
    modify NAME_K VARCHAR2(70);
alter table GBC_SCOPE_TYPE_SPEC
    modify NAME_K VARCHAR2(70);
alter table GBC_SCOPE
    modify NAME_K VARCHAR2(70);
alter table GBC_ROLE
    modify (NAME_K VARCHAR2(70), MEMO_K VARCHAR2(70));
alter table GBC_UNIT_TYPE
    modify (NAME_K VARCHAR2(70), MEMO_K VARCHAR2(70));
alter table GBC_UNIT
    modify NAME_K VARCHAR2(70);
alter table GBC_UNIT_SET
    modify NAME_K VARCHAR2(70);

-- always at the bottom commit
commit;

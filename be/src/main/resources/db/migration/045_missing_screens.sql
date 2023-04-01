INSERT INTO GBC_SCREEN(ID, CODE, TITLE_K, HELPLABEL_K, ID_MENU)
VALUES (176, 'GP176', 'menu.meterConversion', 'pageDescriptionMeterConversion', 439);

INSERT INTO GBC_SCREEN(ID, CODE, TITLE_K, HELPLABEL_K, ID_MENU)
VALUES (276, 'GP276', 'addMeterConversion', 'pageDescriptionAddMeterConversion', NULL);

INSERT INTO GBC_SCREEN(ID, CODE, TITLE_K, HELPLABEL_K, ID_MENU)
VALUES (203, 'GP203', 'addUser', 'pageDescriptionAddEditUser_v2', NULL);

INSERT INTO GBC_SCREEN(ID, CODE, TITLE_K, HELPLABEL_K, ID_MENU)
VALUES (410, 'GR110', 'menu.auditExportSpecial', 'pageDescriptionAuditExport', 446);

UPDATE GBC_SCREEN
SET HELPLABEL_K = 'pageDescriptionMaterialsDetail'
WHERE CODE = 'GP156';

INSERT INTO GBC_SCREEN(ID, CODE, TITLE_K, HELPLABEL_K, ID_MENU)
VALUES (177, 'GP177', 'usepaMaterialType', 'pageDescriptionUsepaMaterialTypes', 50);

INSERT INTO GBC_SCREEN(ID, CODE, TITLE_K, HELPLABEL_K, ID_MENU)
VALUES (277, 'GP277', 'addUsepaMaterialType', 'pageDescriptionEditUsepaMaterialType', NULL);

UPDATE GBC_SCREEN
SET TITLE_K     = 'addFuelType',
    HELPLABEL_K = 'pageDescriptionAddFuelType'
WHERE CODE = 'GP270';
UPDATE GBC_SCREEN
SET TITLE_K     = 'addMeter',
    HELPLABEL_K = 'pageDescriptionMeterEditStep1'
WHERE CODE = 'GP271';

UPDATE GBC_SCREEN
SET TITLE_K     = 'addMeterCalibration',
    HELPLABEL_K = 'pageDescriptionEditMeterCalibration'
WHERE CODE = 'GP272';

UPDATE GBC_SCREEN
SET TITLE_K     = 'addLandfill',
    HELPLABEL_K = 'pageDescriptionAddLandfill'
WHERE CODE = 'GP274';

UPDATE GBC_SCREEN
SET TITLE_K     = 'addMeterType',
    HELPLABEL_K = 'pageDescriptionAddMeterType'
WHERE CODE = 'GP273';

UPDATE GBC_SCREEN
SET TITLE_K     = 'addMaterialType',
    HELPLABEL_K = 'pageDescriptionAddMaterialType'
WHERE CODE = 'GP262';

UPDATE GBC_SCREEN
SET TITLE_K     = 'addScopeProcessSpec',
    HELPLABEL_K = 'pageDescriptionAddScopeProcessSpec'
WHERE CODE = 'GP264';

UPDATE GBC_SCREEN
SET TITLE_K     = 'addScopeTypeSpec',
    HELPLABEL_K = 'pageDescriptionAddScopeTypeSpec'
WHERE CODE = 'GP263';

UPDATE GBC_SCREEN
SET TITLE_K     = 'addPlantRegion'
WHERE CODE = 'GP230';

UPDATE GBC_SCREEN
SET TITLE_K     = 'addScopeFuelSpec',
    HELPLABEL_K = 'pageDescriptionAddScopeFuelSpec'
WHERE CODE = 'GP265';


INSERT INTO GBC_SCREEN(ID, CODE, TITLE_K, HELPLABEL_K, ID_MENU)
VALUES (2502, 'GP244', 'reportedBugDetail', 'reportedBugDetailDescription', NULL);


COMMIT;

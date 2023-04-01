-- OVERVIEW
update GBC_MENU set ITEM_URL_N = '/overview/plant-result' where ID = 18;
UPDATE GBC_MENU SET ITEM_URL_N = '/overview/quantities' WHERE ID = 21;
update GBC_MENU set ITEM_URL_N = '/overview/pareto-corporation' where ID = 38;
UPDATE GBC_MENU SET ITEM_URL_N = '/overview/pareto-plant' WHERE ID = 30;
UPDATE GBC_MENU SET ITEM_URL_N = '/overview/analysis' WHERE ID = 270;

-- INPUTS
UPDATE GBC_MENU SET ITEM_URL_N = '/inputs/data-audit' WHERE ID = 284;
UPDATE GBC_MENU SET ITEM_URL_N = '/inputs/quantities' WHERE ID = 32;
UPDATE GBC_MENU SET ITEM_URL_N = '/inputs/data-lock' WHERE ID = 285;
UPDATE GBC_MENU SET ITEM_URL_N = '/inputs/data-check' WHERE ID = 286;
UPDATE GBC_MENU SET ITEM_URL_N = '/inputs/analysis' WHERE ID = 336;

-- MATERIALS AND WORKS
update GBC_MENU set ITEM_URL_N = '/materials-and-works/material' where ID = 10;
update GBC_MENU set ITEM_URL_N = '/materials-and-works/material-type' where ID = 421;
update GBC_MENU set ITEM_URL_N = '/materials-and-works/usepa-material-type' where ID = 444;
update GBC_MENU set ITEM_URL_N = '/materials-and-works/business-unit' where ID = 418;
update GBC_MENU set ITEM_URL_N = '/materials-and-works/nodes' where ID = 9;
update GBC_MENU set ITEM_URL_N = '/materials-and-works/fuel-type' where ID = 438;
update GBC_MENU set ITEM_URL_N = '/materials-and-works/meter-type' where ID = 426;
update GBC_MENU set ITEM_URL_N = '/materials-and-works/meter' where ID = 429;
update GBC_MENU set ITEM_URL_N = '/materials-and-works/node-level' where ID = 290;
update GBC_MENU set ITEM_URL_N = '/materials-and-works/node-types' where ID = 278;
update GBC_MENU set ITEM_URL_N = '/materials-and-works/region' where ID = 279;
update GBC_MENU set ITEM_URL_N = '/materials-and-works/landfill' where ID = 427;
update GBC_MENU set ITEM_URL_N = '/materials-and-works/meter-calibration' where ID = 433;
update GBC_MENU set ITEM_URL_N = '/materials-and-works/component' where ID = 356;
update GBC_MENU set ITEM_URL_N = '/materials-and-works/material-node' where ID = 13;
update GBC_MENU set ITEM_URL_N = '/materials-and-works/material-unit' where ID = 376;

-- DATA PROCESS
update GBC_MENU set ITEM_URL_N = '/data-processing/calculations' where ID = 257;
update GBC_MENU set ITEM_URL_N = '/data-processing/scope-type-spec' where ID = 423;
update GBC_MENU set ITEM_URL_N = '/data-processing/scope-process-spec' where ID = 424;
update GBC_MENU set ITEM_URL_N = '/data-processing/scope-fuel-spec' where ID = 425;

-- OUTPUTS
update GBC_MENU set ITEM_URL_N = '/outputs/emission-factor' where ID = 443;
update GBC_MENU set ITEM_URL_N = '/outputs/audit-report' where ID = 430;
update GBC_MENU set ITEM_URL_N = '/outputs/audit-special-report' where ID = 446;
update GBC_MENU set ITEM_URL_N = '/outputs/balance-results' where ID = 431;
update GBC_MENU set ITEM_URL_N = '/outputs/balance-results-corporation' where ID = 447;


-- ADMINS
update GBC_MENU set ITEM_URL_N = '/administration/roles' where ID = 7;
update GBC_MENU set ITEM_URL_N = '/administration/logged-users' where ID = 33;
update GBC_MENU set ITEM_URL_N = '/administration/units' where ID = 11;
update GBC_MENU set ITEM_URL_N = '/administration/unit-conversion' where ID = 62;
update GBC_MENU set ITEM_URL_N = '/administration/unit-types' where ID = 60;
update GBC_MENU set ITEM_URL_N = '/administration/lang' where ID = 280;
update GBC_MENU set ITEM_URL_N = '/administration/dictionary' where ID = 254;
update GBC_MENU set ITEM_URL_N = '/administration/journal-types' where ID = 297;
update GBC_MENU set ITEM_URL_N = '/administration/journals' where ID = 296;
update GBC_MENU set ITEM_URL_N = '/administration/system-events' where ID = 36;
update GBC_MENU set ITEM_URL_N = '/administration/system-params' where ID = 37;
update GBC_MENU set ITEM_URL_N = '/administration/screens' where ID = 88;
update GBC_MENU set ITEM_URL_N = '/administration/component-units' where ID = 396;
update GBC_MENU set ITEM_URL_N = '/administration/users' where ID = 6;
update GBC_MENU set ITEM_URL_N = '/administration/meter-conversion' where ID = 439;
update GBC_MENU set ITEM_URL_N = '/administration/login-history' where ID = 34;
update GBC_MENU set ITEM_URL_N = '/administration/webservice-calls-overview' where ID = 282;
update GBC_MENU set ITEM_URL_N = '/administration/user-roles' where ID = 260;
update GBC_MENU set ITEM_URL_N = '/administration/component-calculation-overview' where ID = 434;
update GBC_MENU set ITEM_URL_N = '/administration/countries' where ID = 289;
update GBC_MENU set ITEM_URL_N = '/administration/sent-emails' where ID = 283;
update GBC_MENU set ITEM_URL_N = '/administration/unit-sets' where ID = 61;
update GBC_MENU set ITEM_URL_N = '/administration/user-settings' where ID = 14;
UPDATE GBC_MENU SET ENABLED = 0 WHERE ID = 416;
update GBC_MENU set ITEM_URL_N = '/administration/reported-issues', ENABLED = 1 where ID = 94;

-- EXPORTS
update GBC_MENU set ITEM_URL_N = '/exports/summary' where ID = 295;
update GBC_MENU set ITEM_URL_N = '/exports/material-quantity' where ID = 255;
update GBC_MENU set ITEM_URL_N = 'http://ibmcognos.sk.uss.com/ibmcognos' where ID = 256;
update GBC_MENU set ITEM_URL_N = '/exports/intensity' where ID = 40;
update GBC_MENU set ITEM_URL_N = '/exports/intensity-combined' where ID = 288;
update GBC_MENU set ITEM_URL_N = '/exports/intensity-special' where ID = 417;
update GBC_MENU set ITEM_URL_N = '/exports/component-production' where ID = 41;

-- todo add new screens

commit;

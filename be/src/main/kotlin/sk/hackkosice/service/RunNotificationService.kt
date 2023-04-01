package sk.esten.uss.gbco2.service

import java.time.LocalDate
import java.util.*
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.config.jwt.PrincipalUser
import sk.esten.uss.gbco2.exceptions.ForbiddenException
import sk.esten.uss.gbco2.exceptions.NotFoundException
import sk.esten.uss.gbco2.model.entity.adv_details.VAdvDetails
import sk.esten.uss.gbco2.model.entity.adv_expr_log.AdvExprLog
import sk.esten.uss.gbco2.model.entity.adv_status.AdvStatusSuper
import sk.esten.uss.gbco2.model.entity.event.Event
import sk.esten.uss.gbco2.model.entity.node.Node
import sk.esten.uss.gbco2.model.entity.param.Param
import sk.esten.uss.gbco2.model.entity.user.User
import sk.esten.uss.gbco2.service.common.EmailSenderService
import sk.esten.uss.gbco2.utils.principal

@Service
@Transactional
class RunNotificationService(
    private val userService: UserService,
    private val emailService: EmailService,
    private val dictionaryService: DictionaryService,
    private val paramService: ParamService,
    private val eventService: EventService,
    private val nodeService: NodeService,
    private val advStatusService: AdvStatusService,
    private val advDetailsService: AdvDetailsService,
    private val advExprLogService: AdvExprLogService,
    private val emailSenderService: EmailSenderService
) {
    @Async
    @Synchronized
    fun runNotification(year: Int) {
        val users = userService.getAllUsersForNotification()
        for (user in users) {
            val languageId = user.languageUser?.id ?: 1

            val authorizations = user.authorizations.filter { it.role?.code == "r_admin_plant" }
            val nodes =
                if (authorizations.any { it.node?.id == null }) nodeService.getAll()
                else authorizations.map { it.node }.distinctBy { it?.id }

            val advStatus = advStatusService.getByCode(AdvStatusSuper.STATUS_INVALID)

            val data: MutableMap<Node, List<VAdvDetails>> = mutableMapOf()
            val resultData: MutableList<VAdvDetails> = mutableListOf()

            for (node in nodes) {
                var advDetailsForNode = data[node]
                if (advDetailsForNode == null) {
                    if (node != null) {
                        advDetailsForNode =
                            advDetailsService.findAllByCriteria(
                                user.unitSet?.id,
                                node.id,
                                null,
                                null,
                                year,
                                advStatus.id,
                                null
                            )

                        data[node] = advDetailsForNode
                    }
                }
                if (advDetailsForNode != null) {
                    resultData.addAll(advDetailsForNode)
                }
            }

            val sheets: MutableList<MutableMap<String, Any?>> = mutableListOf(mutableMapOf())
            val dataAdvSheet = generateDataMap(resultData, user, principal())
            var sheetName: String? =
                dictionaryService
                    .findTranslationByKeyAndLanguage("notifEmailTitleTab1", languageId)
                    ?.uppercase(Locale.getDefault())
            if (sheetName != null && sheetName.length > 31) sheetName = sheetName.substring(0, 31)

            dataAdvSheet["sheetName"] = sheetName
            sheets.add(dataAdvSheet)

            val exprLogs =
                advExprLogService.findAllNotValidByNodesAndYear(nodes.mapNotNull { it?.id }, year)
            val dataExprSheet = generateExprMap(exprLogs.toMutableList(), user, principal())
            var exprSheetName =
                dictionaryService
                    .findTranslationByKeyAndLanguage("notifEmailTitleTab3", languageId)
                    ?.uppercase(Locale.getDefault())
            if (exprSheetName != null && exprSheetName.length > 31)
                exprSheetName = exprSheetName.substring(0, 31)

            dataAdvSheet["sheetName"] = exprSheetName
            sheets.add(dataExprSheet)

            val subBodyMsg =
                "${dictionaryService.findTranslationByKeyAndLanguage("notificationMailSubBody1", languageId)} ${resultData.size}\n"
            val sheetMap = sheets[0] + sheets[1]

            // todo: create xlsx file
            //            val attachment = null
            //            val attachment: EmailSenderService.GbcoEmailAttachment =
            //                EmailSenderService.GbcoEmailAttachment("filename", Resource.)

            if (user.email != null && user.email != "") {
                val subject =
                    dictionaryService.findTranslationByKeyAndLanguage(
                        "notificationMailSubject",
                        languageId
                    )
                        ?: throw NotFoundException(
                            "Can't find translation with key: notificationMailSubject"
                        )
                val mailBodyTranslation =
                    dictionaryService.findTranslationByKeyAndLanguage(
                        "notificationMailBody",
                        languageId
                    )
                        ?: ""
                val params =
                    listOf(
                        year.toString(),
                        getNodeNames(nodes.mapNotNull { it?.id }, languageId),
                        subBodyMsg
                    )
                val body = String.format(mailBodyTranslation, params)
                val email =
                    user.email
                        ?: throw ForbiddenException(
                            "Invalid email address with login: ${user.loginAd}"
                        )

                // todo attachment
                //                emailSenderService.sendEmail(subject, body, listOf(email),
                // listOf(attachment))

                //                emailService.saveEmail(
                //                    email,
                //                    subject,
                //                    body,
                //                    attachment,
                //                    attachment.filename,
                //                    user.loginAd
                //                )
            } else {
                throw ForbiddenException(
                    "Run Notification: User with login: " +
                        user.loginAd +
                        " doesn't have valid email address."
                )
            }
        }

        /*

              for(GbcNode p: plants){


                  if (u.getMailNotification() != null && u.getMailNotification().equals(Boolean.TRUE)) {
                    List<GbcAdvDetailsView> values = data.get(p);
                    if (values == null) {

                      values = resultService.getAdvDetailsDAO().findByExample(u.getIdUnitSet().getId(), p, null, null, calFrom.getTime(), calTo.getTime(), advStatus, null);
                      data.put(p, values);
                    }
                    resultDetails.addAll(values);
                  }
              }

              List<HashMap<String, Object>> sheets = new ArrayList<HashMap<String,Object>>();
        //      Map<String, Object>[] sheets = new HashMap[1];
        //      List<String> sheetsToRemove = new ArrayList<String>();
              StringBuffer subBodyMsg = new StringBuffer();

        //      if (resultDetails.size() > 0) {

                if (u.getMailNotification() != null && u.getMailNotification().equals(Boolean.TRUE)) {
                	// Generating adv sheet
                	HashMap<String,Object> dataAdvSheet = new HashMap<String,Object>(); //ADV
                  dataAdvSheet = generateDataMap(resultDetails, u, userRunTheJob);
                  String sheetName = resultService.getDictionaryService().getTranslation(userLocale.getLanguage().toUpperCase(), "notifEmailTitleTab1").toUpperCase();
                  if (sheetName.length() > 31) {
                     sheetName = sheetName.substring(0, 31);
                  }
                  dataAdvSheet.put("sheetName", sheetName);

                  sheets.add(dataAdvSheet);
        //          sheets[0] = dataAdvSheet;

        //          sheetsToRemove.add("ADV material-submaterial");

                }
        //      } /*else {
        //      	sheetsToRemove.add("ADV based on 12 historical data");
        //      	sheetsToRemove.add("ADV material-submaterial");
        //      }*/

              List<GbcAdvExprLog> exprLogs = resultService.getAdvService().findNotValidExprByNodeAndYear(plants, year);

        //      if(exprLogs != null && exprLogs.size() > 0) {
        	      if (u.getMailNotification() != null && u.getMailNotification().equals(Boolean.TRUE)) {
        	          // Expressions sheet
        	      		HashMap<String,Object> exprSheet = new HashMap<String,Object>();
        	          exprSheet = generateExprMap(exprLogs, u, userRunTheJob);
                      String sheetName = resultService.getDictionaryService().getTranslation(userLocale.getLanguage().toUpperCase(), "notifEmailTitleTab3").toUpperCase();
                      if (sheetName.length() > 31) {
                       sheetName = sheetName.substring(0, 31);
                      }
                      exprSheet.put("sheetName", sheetName);

        	          sheets.add(exprSheet);
        //	          sheets[1] = exprSheet;
        	      }
        //      } /*else {
        //      	sheetsToRemove.add("ADV Expressions");
        //      }*/

              if(sheets != null && /*sheets.length*/sheets.size() > 0) {
              	subBodyMsg.append((resultService.getDictionaryService().getTranslation(userLocale.getLanguage().toUpperCase(), "notificationMailSubBody1")));
                subBodyMsg.append(" " + resultDetails.size() + "\n");
              }

              Map<String, Object> hm = new HashMap<String, Object>();

        //      HashMap[] sheets2 = new HashMap[1]; //ADV
        //      sheets2[0] = sheets.get(0);

              hm.put("sheet", /*sheets*/sheets.toArray(new HashMap[sheets.size()]));

              String tempPath = System.getProperty("java.io.tmpdir");
              String contextPath = getClass().getResource("/WEB-INF/").getPath();

              //Creating temp directory if not exist
              try {
                (new File(tempPath + File.separator +"gbco2-temp")).mkdir();
              } catch(Exception e){
                logger.error(Utils.getStackTrace(e));
                throw new Exception("Failed to create temp directory: "+ tempPath);
              }

              HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(contextPath+"reports/xsl/notifyResult.xls"));

        //      logger.debug(wb.getSheetIndex("ADV based on 12 historical data")); // 1
        //      logger.debug(wb.getSheetIndex("ADV material-submaterial")); // 0
        //      logger.debug(wb.getSheetIndex("ADV Expressions")); // 2

        //      if (sheetsToRemove != null && sheetsToRemove.size() > 0) {
        //      	for(String name : sheetsToRemove) {
        //      		wb.removeSheetAt(wb.getSheetIndex(name));
        //      	}
        //      }

        //      logger.debug(wb.getSheetIndex("ADV based on 12 historical data")); //-1
        //      logger.debug(wb.getSheetIndex("ADV material-submaterial")); // -1
        //      logger.debug(wb.getSheetIndex("ADV Expressions")); // 0

              byte[] content = FOPUtils.generateBinaryXls(wb, contextPath+"reports/xsl/", "notifyResult.xls", (HashMap<String, Object>)hm);
              File attachFile = FOPUtils.writeToTempFile(tempPath + File.separator + "gbco2-temp" + File.separator, "InvalidDataOverview", "xls", content);

              //Sending mail
              String receiver = u.getEmail();
              String subject = resultService.getDictionaryService().getTranslation(userLocale.getLanguage().toUpperCase(), "notificationMailSubject");

              String[] params = new String[] {year.toString(), getPlantsNames(plants, userLocale), subBodyMsg.toString()};
              String text =  resultService.getDictionaryService().getTranslation(userLocale.getLanguage().toUpperCase(), "notificationMailBody", params);

              //Test if user have valid email address
              if(receiver != null && !receiver.equals("")){
                mailUtils.sendMessage(receiver, subject, text, attachFile.getPath());
                resultService.getEmailService().saveEmail(new GbcEmail(receiver, subject, text,
                    new BlobImpl(content), attachFile.getName(),
                    new Timestamp(System.currentTimeMillis()),
                    u.getLogin()));
              } else {
                logger.info("Run Notification: User with login: "+u.getLogin()+" doesn't have valid email address.");
              }
                *
                * */

        //            eventService.logEvent(
        //                Event.RUN_NOTIFICATION,
        //                Event.SUCCESSFUL_END_OF_NOTIFICATION,
        //                params + ";" + (userService.getCurrentUser().loginAd ?: "admin")
        //            )
    }

    private fun generateDataMap(
        resultData: MutableList<VAdvDetails>,
        user: User,
        principal: PrincipalUser?
    ): MutableMap<String, Any?> {
        TODO("Not yet implemented")

        //        val map = HashMap<String, Any>()
        //
        //        val dictionaryService: DictionaryService = resultService.getDictionaryService()
        //        val locale = Locale(u.getIdLanguage().getCode())
        //        val code = locale.language.uppercase(Locale.getDefault())
        //
        //        map["ussSteelCorporation"] = dictionaryService.getTranslation(code,
        // "ussSteelCorporation").toUpperCase()
        //        map["plantDataValidationReportHeader1"] =
        //            dictionaryService.getTranslation(code, "plantDataValidationReportHeader1")
        //
        //        map["headerId"] = dictionaryService.getTranslation(code, "id")
        //        map["headerPlantName"] = dictionaryService.getTranslation(code, "node")
        //        map["headerMaterialName"] = dictionaryService.getTranslation(code, "materialName")
        //        map["headerAP"] = dictionaryService.getTranslation(code, "aParamName")
        //        map["headerPassed"] = dictionaryService.getTranslation(code, "passed")
        //        map["headerMinDev"] = dictionaryService.getTranslation(code, "minDev")
        //        map["headerCurrent"] = dictionaryService.getTranslation(code, "current")
        //        map["headerMaxDev"] = dictionaryService.getTranslation(code, "maxDev")
        //        map["headerMean"] = dictionaryService.getTranslation(code, "mean")
        //        map["headerStd"] = dictionaryService.getTranslation(code, "std")
        //        map["headerMonth"] = dictionaryService.getTranslation(code, "month")
        //        map["headerQuantity"] = dictionaryService.getTranslation(code, "quantity")
        //        map["headerQuantityUnit"] = dictionaryService.getTranslation(code, "unit")
        //        map["headerFactorA"] = dictionaryService.getTranslation(code, "factorA")
        //        map["headerFactorB"] = dictionaryService.getTranslation(code, "factorB")
        //        map["headerFactorUnit"] = dictionaryService.getTranslation(code, "unit")
        //        map["headerIO"] = dictionaryService.getTranslation(code, "io")
        //
        //        map["GBCO2Env"] = dictionaryService.getTranslation(code, "GBCO2Env")
        //        map["paramGBCO2Env"] = dictionaryService.getGBCO2Env(code)
        //        map["createdBy"] = dictionaryService.getTranslation(code, "createdBy")
        //
        //        map["paramCreatedBy"] = dictionaryService.getCreatedBy(
        //            TimeZone.getTimeZone(u.getTimeZone()),
        //            code,
        //            userRunTheJob.getName(),
        //            userRunTheJob.getSurname()
        //        )
        //
        //        map["plantData"] = getGroupDataMap(resultDetails, locale)
        //
        //        return map
    }

    //    @Throws(Gbco2Exception::class)
    //    private fun getGroupExprMap(exprLogs: List<GbcAdvExprLog>?, locale: Locale):
    // Array<HashMap<String, Any>>? {
    //        if (exprLogs == null) return null
    //        val dictionaryService: DictionaryService = resultService.getDictionaryService()
    //        val maps: Array<HashMap<String, Any>> = arrayOfNulls<HashMap<*, *>>(exprLogs.size)
    //        for (i in exprLogs.indices) {
    //            val m = HashMap<String, Any>()
    //            val materialNodeAp: GbcMaterialNodeAp =
    //
    // resultService.getMaterialNodeService().getMaterialNodeAPById(exprLogs[i].getIdMaterialNodeAp())
    //            //GbcMaterial material =
    // exprLogs.get(i).getIdMaterialNodeAp().getIdMaterialNode().getIdMaterial();
    //            logger.debug("materialNodeAp(): $materialNodeAp")
    //            logger.debug("materialNodeAp.getIdMaterialNode(): " +
    // materialNodeAp.getIdMaterialNode())
    //            val material: GbcMaterial = materialNodeAp.getIdMaterialNode().getIdMaterial()
    //
    ////      Translator.translateMaterial(material, locale.getLanguage());
    //            val rd: GbcAdvExprLog = exprLogs[i]
    //            //      m.put("materialName", material.getTranslatedName());
    //            m["nodeName"] =
    // resultService.getDictionaryService().getTranslation(locale.language, rd.getNodeName())
    //
    //            //m.put("componentName",
    // resultService.getDictionaryService().getTranslation(locale.getLanguage(),
    // rd.getIdMaterialNodeAp().getIdAnalysisParam().getNameK()));
    //            m["componentName"] =
    //                resultService.getDictionaryService()
    //                    .getTranslation(locale.language,
    // materialNodeAp.getIdAnalysisParam().getNameK())
    //
    //            //m.put("materialNodeName",
    // rd.getIdMaterialNodeAp().getIdMaterialNode().getName());
    //            m["materialNodeName"] = materialNodeAp.getIdMaterialNode().getName()
    //            val quantity: GbcQuantity =
    // resultService.getQuantityDAO().getById(rd.getIdQuantity())
    //            m["quantity"] = setScale(quantity.getQuantity()
    // /*rd.getIdQuantity().getQuantity()*/, EXCEL_SCALE)
    //            m["quantityUnit"] = quantity.getIdUnit().getAbbr()
    //            m["expression"] = rd.getExpr()
    //            if
    // (rd.getIdAdvStatus().getNameK().equals(GbcAdvStatus.STATUS_MISSING_EXPR_PARAMETERS)) {
    //                m["status"] = dictionaryService.getTranslation(
    //                    locale.language,
    //                    rd.getIdAdvStatus().getNameK()
    //                ) + ": " + rd.getMissingParameters()
    //            } else {
    //                m["status"] = dictionaryService.getTranslation(locale.language,
    // rd.getIdAdvStatus().getNameK())
    //            }
    //            maps[i] = m
    //        }
    //        return maps
    //    }

    private fun generateExprMap(
        resultData: MutableList<AdvExprLog>,
        user: User,
        principal: PrincipalUser?
    ): MutableMap<String, Any?> {
        TODO("Not yet implemented")

        //        val map = HashMap<String, Any>()
        //
        //        val dictionaryService: DictionaryService = resultService.getDictionaryService()
        //        val locale = Locale(u.getIdLanguage().getCode())
        //        val code = locale.language.uppercase(Locale.getDefault())
        //
        //        map["ussSteelCorporation"] = dictionaryService.getTranslation(code,
        // "ussSteelCorporation").toUpperCase()
        //        map["plantDataValidationReportHeader3"] =
        //            dictionaryService.getTranslation(code, "plantDataValidationReportHeader3")
        //
        //        map["headerNodeName"] = dictionaryService.getTranslation(code, "node")
        //        map["headerMaterialName"] = dictionaryService.getTranslation(code, "materialName")
        //        map["headerMaterialNodeName"] = dictionaryService.getTranslation(code,
        // "materialNodeName")
        //        map["headerComponentName"] = dictionaryService.getTranslation(code,
        // "componentName")
        //        map["headerQuantity"] = dictionaryService.getTranslation(code, "quantity")
        //        map["headerQuantityUnit"] = dictionaryService.getTranslation(code, "unit")
        //        map["headerExpression"] = dictionaryService.getTranslation(code, "Expression")
        //        map["headerStatus"] = dictionaryService.getTranslation(code, "status")
        //
        //        map["GBCO2Env"] = dictionaryService.getTranslation(code, "GBCO2Env")
        //        map["paramGBCO2Env"] = dictionaryService.getGBCO2Env(code)
        //        map["createdBy"] = dictionaryService.getTranslation(code, "createdBy")
        //
        //        map["paramCreatedBy"] = dictionaryService.getCreatedBy(
        //            TimeZone.getTimeZone(u.getTimeZone()),
        //            code,
        //            userRunTheJob.getName(),
        //            userRunTheJob.getSurname()
        //        )
        //
        //        map["exprData"] = getGroupExprMap(exprLogs, locale)
        //
        //        return map
    }

    //    @Throws(Gbco2Exception::class)
    //    private fun getGroupDataMap(data: List<GbcAdvDetailsView>?, locale: Locale):
    // Array<HashMap<String, Any>>? {
    //        if (data == null) return null
    //        val maps: Array<HashMap<String, Any>> = arrayOfNulls<HashMap<*, *>>(data.size)
    //        for (i in data.indices) {
    //            val m = HashMap<String, Any>()
    //            Translator.translateMaterial(data[i].getIdMaterial(), locale.language)
    //            val rd: GbcAdvDetailsView = data[i]
    //            m["materialName"] = rd.getIdMaterial().getTranslatedName()
    //            m["plantName"] =
    // resultService.getDictionaryService().getTranslation(locale.language, rd.getNodeName())
    //            data[i].getIdAnalysisParam().setNameTranslated(
    //                resultService.getDictionaryService()
    //                    .getTranslation(locale.language, data[i].getIdAnalysisParam().getNameK())
    //            )
    //            m["apName"] = rd.getComponentName()
    //            m["id"] = BigDecimal(rd.getId())
    //            val month: Date = rd.getMonth()
    //            m["month"] = SimpleDateFormat("MM/yyyy", locale).format(month.time)
    //            m["quantity"] = setScale(rd.getQuantity(), EXCEL_SCALE)
    //            m["quantityUnit"] = rd.getIdUnit().getAbbr()
    //            m["minDev"] = setScale(rd.getValIntensityMin(), EXCEL_SCALE)
    //            m["current"] = setScale(rd.getValIntensity(), EXCEL_SCALE)
    //            m["maxDev"] = setScale(rd.getValIntensityMax(), EXCEL_SCALE)
    //            m["mean"] = setScale(rd.getIntensityMean(), EXCEL_SCALE)
    //            m["std"] = setScale(rd.getIntensityStd(), EXCEL_SCALE)
    //            m["factorA"] = setScale(rd.getFactorA(), EXCEL_SCALE)
    //            m["factorB"] = setScale(rd.getFactorB(), EXCEL_SCALE)
    //            m["factorUnit"] = rd.getIdUnitNumenator().getAbbr() + "/" +
    // rd.getIdUnitDenominator().getAbbr()
    //            //m.put("io", data.get(i).getIdMaterial().getIo() == 1 ?
    //            //
    // resultService.getDictionaryService().getTranslation(locale.getLanguage().toUpperCase(),
    // "input") :
    //            //
    // resultService.getDictionaryService().getTranslation(locale.getLanguage().toUpperCase(),
    // "output"));
    //            maps[i] = m
    //        }
    //        return maps
    //    }

    fun getNodeNames(nodeIds: List<Long>, languageId: Long): String {
        var names = ""
        for (nodeId in nodeIds) {
            names += "${nodeService.getTranslatedName(nodeId, languageId)};"
        }
        return names.dropLast(1)
    }

    fun initializeRunNotification(year: Int, isJob: Boolean): Boolean {
        var notificationStarted = false
        val params = ""

        try {
            val param: Param = paramService.getParamByCode(Param.IS_NOTIFICATION_RUNNING)
            if (param.value == Param.RUNNING)
                throw ForbiddenException("notificationIsAlreadyRunning")
            param.value = Param.RUNNING
            paramService.updateParam(param)

            notificationStarted = true
            if (isJob) {
                val lastExecParam: Param =
                    paramService.getParamByCode(Param.LAST_NOTIFICATION_JOB_EXEC)
                lastExecParam.dateValue = LocalDate.now()
                paramService.updateParam(lastExecParam)
            }

            eventService.logEvent(
                Event.RUN_NOTIFICATION,
                Event.START_OF_NOTIFICATION,
                params + ";" + (userService.getCurrentUser().loginAd ?: "admin")
            )

            runNotification(year)

            return true
        } catch (e: Exception) {
            if (notificationStarted) {
                eventService.logEvent(
                    Event.RUN_NOTIFICATION,
                    Event.UNSUCCESSFUL_END_OF_NOTIFICATION,
                    params
                )
            }
            return false
        }
    }
}

package sk.esten.uss.gbco2.service.exports.data

class SheetData<DTO : Exportable>(val data: MutableMap<String, List<DTO>>, val param: Any? = null)

package sk.esten.uss.gbco2.config.enums

enum class EnvEnum(val key: String) {
    UNKNOWN("none"),
    DEVELOPMENT("developmentEnvironment"),
    TESTING("testingEnvironment"),
    QUALITY("qualityEnvironment"),
    PRODUCTION("productionEnvironment"),
}

package love.chihuyu.game

enum class TimePresets(val seconds: Long) {
    FIVE_MIN(300),
    TEN_MIN(600),
    HALF_HOUR(1800),
    ONE_HOUR(3600),
    TWO_HOUR(7200)
}
package love.chihuyu.utils

import java.time.Instant

object EpochUtil {

    fun formatTime(timeSeconds: Long): String {
        return "${"%02d".format(timeSeconds.floorDiv(3600))}:" + "${"%02d".format(timeSeconds.floorDiv(60) % 60)}:" + "%02d".format(timeSeconds % 60)
    }

    fun nowEpoch(): Long {
        return Instant.now().epochSecond
    }
}

package love.chihuyu.utils

import java.io.File

object FileUtil {

    fun deleteDirectory(path: File): Boolean {
        if (path.exists()) {
            val files = path.listFiles()
            files?.forEach { file2 ->
                if (file2.isDirectory) {
                    deleteDirectory(file2)
                } else {
                    file2.delete()
                }
            }
        }
        return path.delete()
    }
}
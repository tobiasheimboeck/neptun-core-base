package world.neptuns.core.base.common.api.file

import world.neptuns.core.base.api.file.FileService
import world.neptuns.core.base.api.file.NeptunFile
import world.neptuns.streamline.api.StreamlineApi
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass

class FileControllerImpl : FileService {

    override fun <T> read(file: File, clazz: Class<T>): T? {
        return try {
            StreamlineApi.GSON.fromJson(FileReader(file), clazz)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    override fun save(file: File, result: Any) {
        try {
            val fileWriter = FileWriter(file)
            StreamlineApi.GSON.toJson(result, fileWriter)
            fileWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun <T : NeptunFile> createOrLoadFile(dataFolderPath: Path, subFolderName: String, fileName: String, clazz: KClass<T>, content: T): T {
        val filePath = File("${dataFolderPath}/$subFolderName")
        val result: T

        if (!Files.exists(filePath.toPath())) Files.createDirectories(filePath.toPath())
        val file: File = Paths.get("${filePath}/$fileName.json").toFile()

        if (!Files.exists(file.toPath())) {
            result = content
            save(file, result)
        } else {
            result = read(file, clazz.java)!!
        }

        return result
    }

    override fun readRawFile(dataFolderPath: Path, subFolderName: String, fileName: String): File? {
        val filePath = File("${dataFolderPath}/$subFolderName")

        if (!Files.exists(filePath.toPath())) return null
        val file: File = Paths.get("${filePath}/$fileName.json").toFile()

        return file
    }

}
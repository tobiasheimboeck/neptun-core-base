package world.neptuns.core.base.api.file

import java.io.File
import java.nio.file.Path
import kotlin.reflect.KClass

interface FileService {

    fun <T> read(file: File, clazz: Class<T>): T?

    fun save(file: File, result: Any)

    fun <T : NeptunFile> createOrLoadFile(dataFolderPath: Path, subFolderName: String, fileName: String, clazz: KClass<T>, content: T): T

    fun readRawFile(dataFolderPath: Path, subFolderName: String, fileName: String): File?

}
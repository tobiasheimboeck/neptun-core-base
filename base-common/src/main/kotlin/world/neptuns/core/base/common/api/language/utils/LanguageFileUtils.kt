package world.neptuns.core.base.common.api.language.utils

import world.neptuns.core.base.api.CoreBaseApi
import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.LineKey
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URISyntaxException
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream

object LanguageFileUtils {

    fun getLanguageKeysFromJarFile(rawPath: String, clazz: Class<*>): Set<LangKey> {
        val languageKeys: MutableSet<LangKey> = mutableSetOf()

        try {
            val fileSystem: FileSystem = FileSystems.newFileSystem(clazz.getResource("")!!.toURI(), emptyMap<String, Any>())
            val pathStream: Stream<Path> = Files.list(fileSystem.rootDirectories.iterator().next().resolve(rawPath))

            loadLanguageKeys(languageKeys, rawPath, pathStream)

            fileSystem.close()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return languageKeys
    }

    @Suppress("UNCHECKED_CAST")
    fun getLanguageFileContent(rawPath: String, langKey: LangKey, clazz: Class<*>): Map<LineKey, String> {
        val content: MutableMap<LineKey, String> = mutableMapOf()

        val path = "$rawPath/${langKey.asString()}.json"
        val resource = clazz.getResource(path) ?: throw NullPointerException("Path $path not found")

        try {
            InputStreamReader(resource.openStream(), StandardCharsets.UTF_8).use { inputStreamReader ->
                BufferedReader(inputStreamReader).use { bufferedReader ->
                    val map = CoreBaseApi.GSON.fromJson(bufferedReader, Map::class.java) as Map<String, String>

                    for ((key, value) in map) {
                        val keySplitted = key.split(".")
                        val keyNamespace = "${keySplitted[0]}.${keySplitted[1]}"
                        val keyValue = keySplitted.subList(2, keySplitted.size).joinToString(".")

                        content[LineKey.key(keyNamespace, keyValue)] = value
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return content
    }

    private fun loadLanguageKeys(languageKeys: MutableSet<LangKey>, rawPath: String, pathStream: Stream<Path>) {
        val isShared = rawPath.contains("lang/shared")
        val correctedPathStream = if (isShared) pathStream else pathStream.filter { !it.toString().contains("lang/shared") }

        for (path in correctedPathStream) {
            val splittedPath = path.toString().split("/")
            val languageCompactPath = splittedPath[if (isShared) 3 else 2].split(".")[0].split("_")
            languageKeys.add(LangKey.key(languageCompactPath[0], languageCompactPath[1]))
        }
    }

}
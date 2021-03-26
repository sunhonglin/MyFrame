import org.gradle.api.Project
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

const val KeystorePropertiesFilePath = ".keystore/release/release.properties"
private const val DependenciesVersionPropertiesFilePath = "versions.properties"
private const val DefaultFormat = "yyyyMMdd"

fun systemDate(format: String? = null): String {
    var f = DefaultFormat
    if (!format.isNullOrBlank()) f = format
    return SimpleDateFormat(f, Locale.CHINA).format(System.currentTimeMillis())
}

fun dependenciesVersion(rootProject: Project, name: String): Any? {
    return property(rootProject, DependenciesVersionPropertiesFilePath, name)
}

fun property(rootProject: Project, filePath: String, propertyName: String): Any? {
    val keystorePropertiesFile = rootProject.file(filePath)
    val keystoreProperties = Properties()
    keystoreProperties.load(keystorePropertiesFile.inputStream())
    return keystoreProperties[propertyName]
}
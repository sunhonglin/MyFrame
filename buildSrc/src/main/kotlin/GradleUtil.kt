import org.gradle.api.Project
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
    val propertiesFileInputStream = rootProject.file(filePath).inputStream()
    val keystoreProperties = Properties()
    keystoreProperties.load(propertiesFileInputStream)
    propertiesFileInputStream.close()
    return keystoreProperties[propertyName]
}

fun dependenciesUrl(rootProject: Project, url : String, versionName: String): String {
    var result = url
    if (result.endsWith("_")) {
        result = result.substring(0, url.length - 1)
    }
    return result + (dependenciesVersion(rootProject, versionName) ?: versionName)
}
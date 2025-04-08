package com.alexius.copytool

import com.github.difflib.text.DiffRow
import com.github.difflib.text.DiffRowGenerator
import java.io.File

data class PatternWithReason(
    val pattern: Regex,
    val reason: String? = null
)

data class CopyConfig(
    val sourceFolder: String,
    val tempFolder: String,
    val folderToCompare: String? = null,
    val substitutions: Map<Regex, String>,
    val detectionPatterns: List<PatternWithReason>,
    val ignorePatterns: List<PatternWithReason>,
    val ignoreFiles: List<String> = listOf(
        "build", ".gradle", ".git", ".localproperties", ".DS_Store"
    ),
    val fileNameSubstitutions: Map<Regex, String> = mapOf(),
    val directorySubstitutions: Map<Regex, String> = mapOf(),
    val ignoreDirectories: List<PatternWithReason> = listOf(
        PatternWithReason("""build""".toRegex(), "no build folder"),
        PatternWithReason("""\.gradle""".toRegex(), "no caches"),
        PatternWithReason("""\.git""".toRegex(), "git is separate for every copy"),
    ),
    val detectDirectories: List<PatternWithReason> = listOf(),
    val skipNewerFiles: Boolean = false,
    val removeDeleted: Boolean = false
)


fun verifyResults(tmpDir: String, targetToCompareDir: String): String {
    val sb = StringBuilder()
    val tmpDirFile = File(tmpDir)
    val targetToCompareDirFile = File(targetToCompareDir)

    // Get all files from both directories
    val tmpFiles = tmpDirFile.walkTopDown()
        .filter { it.isFile }
        .map { it.relativeTo(tmpDirFile).path }
        .toSet()

    val targetFiles = targetToCompareDirFile.walkTopDown()
        .filter { it.isFile }
        .map { it.relativeTo(targetToCompareDirFile).path }
        .toSet()

    // Check for missing or extra files
    val missingFiles = targetFiles - tmpFiles
    val extraFiles = tmpFiles - targetFiles

    if (missingFiles.isNotEmpty()) {
        sb.appendLine("Missing files in output:")
        missingFiles.forEach { sb.appendLine("  $it") }
        sb.appendLine()
    }

    if (extraFiles.isNotEmpty()) {
        sb.appendLine("Extra files in output:")
        extraFiles.forEach { sb.appendLine("  $it") }
        sb.appendLine()
    }

    // Compare content of common files
    tmpFiles.intersect(targetFiles).forEach { relativePath ->
        val tmpFile = File(tmpDirFile, relativePath)
        val targetFile = File(targetToCompareDirFile, relativePath)

        val diff = compareFiles(tmpFile, targetFile)
        if (diff.isNotEmpty()) {
            sb.appendLine("Differences in $relativePath:")
            sb.appendLine(diff)
        }
    }

    return sb.toString()
}

private fun compareFiles(file1: File, file2: File): String {
    val original = file1.readLines()
    val revised = file2.readLines()

    val generator = DiffRowGenerator.create()
        .showInlineDiffs(true)
        .mergeOriginalRevised(false)  // Don't merge the differences
        .inlineDiffByWord(true)       // Compare by word instead of character
        .oldTag { "~" }               // Markdown style for deleted text
        .newTag { "**" }              // Markdown style for new text
        .build()

    val rows = generator.generateDiffRows(original, revised)

    if (rows.all { it.oldLine == it.newLine }) return ""

    val sb = StringBuilder()
    sb.appendLine("Differences found:")

    rows.forEachIndexed { index, row ->
        when (row.tag) {
            DiffRow.Tag.CHANGE -> {
                sb.appendLine("Line ${index + 1}:")
                sb.appendLine("- Original: ${row.oldLine}")
                sb.appendLine("- Modified: ${row.newLine}")
                sb.appendLine()
            }
            DiffRow.Tag.DELETE -> {
                sb.appendLine("Line ${index + 1}:")
                sb.appendLine("- Deleted: ${row.oldLine}")
                sb.appendLine()
            }
            DiffRow.Tag.INSERT -> {
                sb.appendLine("Line ${index + 1}:")
                sb.appendLine("- Inserted: ${row.newLine}")
                sb.appendLine()
            }
            // Skip EQUAL tags as we only want to show differences
            else -> { }
        }
    }

    return sb.toString()
}


fun copyProject(config: CopyConfig, ) {
    try {
        val sourceDir = File(config.sourceFolder)
        val tempDir = File(config.tempFolder)

        if (!sourceDir.exists() || !sourceDir.isDirectory) {
            throw IllegalArgumentException("Source directory doesn't exist or is not a directory")
        }

        // Clear temp directory if removeDeleted is true
        if (config.removeDeleted && tempDir.exists()) {
            tempDir.walkBottomUp()
                .filter { it.isFile }
                .forEach { it.delete() }
        } else {
            tempDir.mkdirs()
        }

        // Collect all source files before copying
        val sourceFiles = sourceDir.walkTopDown()
            .filter { it.isFile }
            .map { it.relativeTo(sourceDir).path }
            .toSet()

        copyDirectory(sourceDir, tempDir, config)

        // If removeDeleted is true, remove files that don't exist in source
        if (config.removeDeleted) {
            val transformedSourceFiles = sourceFiles.map { fileName ->
                transformFileName(fileName, config.fileNameSubstitutions)
            }.toSet()

            tempDir.walkTopDown()
                .filter { it.isFile }
                .forEach { file ->
                    val relativePath = file.relativeTo(tempDir).path
                    if (relativePath !in transformedSourceFiles) {
                        println("Removing file not present in source: $relativePath")
                        file.delete()
                    }
                }

            // Clean up empty directories
            tempDir.walkBottomUp()
                .filter { it.isDirectory }
                .filter { it.listFiles()?.isEmpty() ?: true }
                .filter { it != tempDir }  // Don't delete the root temp directory
                .forEach {
                    println("Removing empty directory: ${it.relativeTo(tempDir).path}")
                    it.delete()
                }
        }

        val targetToCompare = config.folderToCompare ?: config.sourceFolder

        if (config.folderToCompare != null) {
            // Verify results and return differences
            println(verifyResults(config.tempFolder, targetToCompare))
        }

    } catch (e: Exception) {
        println("Error: ${e.message}")
        e.printStackTrace()
    }
}/**/

private fun copyDirectory(sourceDir: File, tempDir: File, config: CopyConfig) {
    // Check if directory should be ignored
    config.ignoreDirectories.firstOrNull { it.pattern.matches(sourceDir.name) }?.let { pattern ->
        println("Ignoring directory: ${sourceDir.name} - Reason: ${pattern.reason}")
        return
    }

    // Check for detected directory patterns
    config.detectDirectories.firstOrNull { it.pattern.matches(sourceDir.name) }?.let { pattern ->
        throw SecurityException("Detected sensitive directory pattern: ${sourceDir.name} - Reason: ${pattern.reason}")
    }

    // Transform directory name if needed
    var targetDirName = tempDir.name
    config.directorySubstitutions.forEach { (regex, replacement) ->
        targetDirName = regex.replace(targetDirName, replacement)
    }

    val actualTargetDir = if (targetDirName != tempDir.name) {
        File(tempDir.parent, targetDirName)
    } else {
        tempDir
    }

    actualTargetDir.mkdirs()

    sourceDir.listFiles()?.forEach { file ->
        if (config.ignoreFiles.contains(file.name)) {
            return@forEach
        }

        // Transform the file name using substitutions
        val newFileName = transformFileName(file.name, config.fileNameSubstitutions)
        val targetFile = File(actualTargetDir, newFileName)

        when {
            file.isDirectory -> {
                copyDirectory(file, targetFile, config)
            }
            file.extension in listOf(
                "kt",
                "xml",
                "gradle",
                "toml",
                "kts",
                "json",
                "md",
                "properties",
                "yml",
                ".gitignore",
                "pro"
            ) -> {
                processFile(file, targetFile, config)
            }
            else -> {
                if (!config.skipNewerFiles || !targetFile.exists() || targetFile.lastModified() <= file.lastModified()) {
                    file.copyTo(targetFile, overwrite = true)
                } else {
                    println("Skipping ${targetFile.path} - Target is newer than source")
                }
            }
        }
    }
}

private fun transformFileName(fileName: String, substitutions: Map<Regex, String>): String {
    var newFileName = fileName
    substitutions.forEach { (regex, replacement) ->
        newFileName = newFileName.replace(regex, replacement)
    }
    return newFileName
}

private fun processFile(sourceFile: File, targetFile: File, config: CopyConfig) {

    // Check if target file exists and is newer (only if skipNewerFiles is true)
    if (config.skipNewerFiles && targetFile.exists() && targetFile.lastModified() > sourceFile.lastModified()) {
        println("Skipping ${targetFile.path} - Target is newer than source")
        return
    }

    var content = sourceFile.readText()

    // First, check and report ignore patterns
    config.ignorePatterns.forEach { ignorePattern ->
        val matches = ignorePattern.pattern.findAll(content)
        matches.forEach { match ->
            val message = buildString {
                append("Note: Pattern found in ${sourceFile.path}: '${match.value}'")
                ignorePattern.reason?.let { append(" - Reason: $it") }
            }
            println(message)
        }
    }

    // Then, check for detection patterns
    config.detectionPatterns.forEach { detectionPattern ->
        val matches = detectionPattern.pattern.findAll(content)
        matches.forEach { match ->
            val message = buildString {
                append("Detected potentially sensitive pattern in ${sourceFile.path}: '${match.value}'")
                detectionPattern.reason?.let { append(" - Reason: $it") }
            }
            throw SecurityException(message)
        }
    }

    // Apply substitutions
    config.substitutions.forEach { (regex, replacement) ->
        content = content.replace(regex, replacement)
    }

    targetFile.writeText(content)
}


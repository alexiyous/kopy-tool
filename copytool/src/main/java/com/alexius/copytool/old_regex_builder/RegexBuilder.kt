package com.alexius.copytool.old_regex_builder

import com.alexius.copytool.CopyConfig
import com.alexius.copytool.PatternWithReason

// Regex Template System
object RegexTemplates {


    // Custom template creator
    fun custom(): RegexBuilder {
        return RegexBuilder()

    }

    // Builder class for creating regex patterns
    // There's a difference mechanism between ignorePatterns and ignoreDirectories, while ignorePatterns will only print
    // a message when a pattern is found, ignoreDirectories will skip the directory entirely, preventing any further processing
    // See copy_sensitive_data for an example of ignorePatterns and ignore_directory for an example of ignoreDirectories
    class RegexBuilder {
        private val substitutions = mutableMapOf<Regex, String>()
        private val detectionPatterns = mutableListOf<PatternWithReason>()
        private val ignorePatterns = mutableListOf<PatternWithReason>()
        private val fileNameSubstitutions = mutableMapOf<Regex, String>()
        private val directorySubstitutions = mutableMapOf<Regex, String>()
        private val ignoreDirectories = mutableListOf<PatternWithReason>()
        private val detectDirectories = mutableListOf<PatternWithReason>()
        private var skipNewerFiles: Boolean = false
        private var removeDeleted: Boolean = false

        fun skipNewerFiles(skip: Boolean = true) {
            skipNewerFiles = skip
        }

        fun removeDeleted(remove: Boolean = true) {
            removeDeleted = remove
        }

        fun addDirectoryConversion(fromPattern: String, toPattern: String): RegexBuilder {
            directorySubstitutions[Regex(fromPattern)] = toPattern
            return this
        }

        // (PascalCase)
        fun addComponentConversion(fromPrefix: String, toPrefix: String): RegexBuilder {
            substitutions[Regex("${fromPrefix}([A-Z][a-zA-Z0-9]*)")] = "$toPrefix$1"
            return this
        }

        fun addFileNameConversion(fromPattern: String, toPattern: String): RegexBuilder {
            fileNameSubstitutions[Regex(fromPattern)] = toPattern
            return this
        }

        fun addResourceConversion(fromPrefix: String, toPrefix: String): RegexBuilder {
            substitutions[Regex("R\\.layout\\.${fromPrefix.lowercase()}_[a-z0-9_]+")] =
                "R.layout.${toPrefix.lowercase()}_$1"
            substitutions[Regex("R\\.id\\.${fromPrefix.lowercase()}_[a-z0-9_]+")] =
                "R.id.${toPrefix.lowercase()}_$1"
            return this
        }

        // (camelCase)
        fun addVariableConversion(fromPrefix: String, toPrefix: String): RegexBuilder {
            substitutions[Regex("(?<=\\s|^)${fromPrefix.lowercase()}([A-Z][a-zA-Z0-9]*)")] =
                "${toPrefix.lowercase()}$1"
            return this
        }

        fun addPackageAndImportsConversion(fromPackage: String, toPackage: String): RegexBuilder {
            val escaped = fromPackage.replace(".", "\\.")
            substitutions[Regex("^(\\s*(?:package|import)\\s+)$escaped(\\.[\\w.]+)?", RegexOption.MULTILINE)] = "$1$toPackage$2"
            return this
        }

        fun addDetectionPattern(pattern: String): RegexBuilder {
            detectionPatterns.add(PatternWithReason(Regex(pattern)))
            return this
        }

        fun addDetectionPattern(pattern: String, reason: String): RegexBuilder {
            detectionPatterns.add(PatternWithReason(Regex(pattern), reason))
            return this
        }

        fun addIgnorePattern(pattern: String): RegexBuilder {
            ignorePatterns.add(PatternWithReason(Regex(pattern)))
            return this
        }

        fun addIgnorePattern(pattern: String, reason: String): RegexBuilder {
            ignorePatterns.add(PatternWithReason(Regex(pattern), reason))
            return this
        }

        fun addIgnoreDirectory(pattern: String): RegexBuilder {
            ignoreDirectories.add(PatternWithReason(Regex(pattern)))
            return this
        }

        fun addIgnoreDirectory(pattern: String, reason: String): RegexBuilder {
            ignoreDirectories.add(PatternWithReason(Regex(pattern), reason))
            return this
        }

        fun addDetectDirectory(pattern: String): RegexBuilder {
            detectDirectories.add(PatternWithReason(Regex(pattern)))
            return this
        }

        fun addDetectDirectory(pattern: String, reason: String): RegexBuilder {
            detectDirectories.add(PatternWithReason(Regex(pattern), reason))
            return this
        }

        fun build(
            sourceFolder: String,
            tempFolder: String,
            folderToCompare: String? = null
        ): CopyConfig {
            return CopyConfig(
                sourceFolder = sourceFolder,
                tempFolder = tempFolder,
                folderToCompare = folderToCompare,
                substitutions = substitutions.toMap(),
                detectionPatterns = detectionPatterns.toList(),
                ignorePatterns = ignorePatterns.toList(),
                fileNameSubstitutions = fileNameSubstitutions.toMap(),
                directorySubstitutions = directorySubstitutions.toMap(),
                ignoreDirectories = ignoreDirectories.toList(),
                detectDirectories = detectDirectories.toList(),
                skipNewerFiles = skipNewerFiles,
                removeDeleted = removeDeleted
            )
        }
    }
}
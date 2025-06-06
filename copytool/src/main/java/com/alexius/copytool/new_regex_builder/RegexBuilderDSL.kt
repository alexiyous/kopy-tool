package com.alexius.copytool.new_regex_builder

import com.alexius.copytool.CopyConfig
import com.alexius.copytool.PatternWithReason

object RegexTemplates {
    operator fun invoke(init: RegexBuilder.() -> Unit): RegexBuilder {
        return RegexBuilder().apply(init)
    }
}

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

    fun removeDeleted(remove: Boolean = true) {
        removeDeleted = remove
    }

    fun skipNewerFiles(skip: Boolean = true) {
        skipNewerFiles = skip
    }

    fun conversions(init: Conversions.() -> Unit) {
        Conversions(this).apply(init)
    }

    fun detections(init: Detections.() -> Unit) {
        Detections(this).apply(init)
    }

    class Conversions(private val builder: RegexBuilder) {
        fun component(fromPrefix: String, toPrefix: String) {
            builder.substitutions[Regex("${fromPrefix}([a-zA-Z0-9_]*)")] = "$toPrefix$1"
        }

        fun variable(fromPrefix: String, toPrefix: String) {
            builder.substitutions[Regex("""(?<=\s|^|\( )${fromPrefix}([a-zA-Z0-9_]*)""")] =
                "${toPrefix}$1"
        }

        fun string(fromPrefix: String, toPrefix: String) {
            builder.substitutions[Regex("""(?<=")${fromPrefix}([a-zA-Z0-9_]*)""")] =
                "${toPrefix}$1"
        }

        fun fileName(fromPattern: String, toPattern: String) {
            builder.fileNameSubstitutions[Regex(fromPattern)] = toPattern
        }

        fun packagesAndImports(fromPackage: String, toPackage: String) {
            builder.substitutions[Regex("(?<=^package |^import )${fromPackage.replace(".", "\\.")}")] = toPackage
        }

        fun resource(fromPrefix: String, toPrefix: String) {
            builder.substitutions[Regex("R\\.layout\\.${fromPrefix}_[a-z0-9_]+")] =
                "R.layout.${toPrefix}_$1"
            builder.substitutions[Regex("R\\.id\\.${fromPrefix}_[a-z0-9_]+")] =
                "R.id.${toPrefix}_$1"
        }

        fun directory(fromPattern: String, toPattern: String) {
            builder.directorySubstitutions[Regex(fromPattern)] = toPattern
        }

        fun custom(fromPattern: String, toPattern: String) {
            builder.substitutions[Regex(fromPattern)] = toPattern
        }
    }

    class Detections(private val builder: RegexBuilder) {
        fun variables(prefix: String, init: VariableDetection.() -> Unit) {
            VariableDetection(builder, prefix).apply(init)
        }

        fun directories(init: DirectoryPatterns.() -> Unit) {
            DirectoryPatterns(builder).apply(init)
        }

        fun sensitive(init: SensitivePatterns.() -> Unit) {
            SensitivePatterns(builder).apply(init)
        }
    }

    class VariableDetection(private val builder: RegexBuilder, private val prefix: String) {
        fun ignore(init: IgnorePatterns.() -> Unit) {
            IgnorePatterns(builder, prefix).apply(init)
        }

        fun detect(init: DetectPatterns.() -> Unit) {
            DetectPatterns(builder, prefix).apply(init)
        }
    }

    class DirectoryPatterns(private val builder: RegexBuilder) {
        fun ignore(pattern: String, reason: String? = null) {
            builder.ignoreDirectories.add(PatternWithReason(Regex(pattern), reason))
        }

        fun detect(pattern: String, reason: String? = null) {
            builder.detectDirectories.add(PatternWithReason(Regex(pattern), reason))
        }
    }

    class SensitivePatterns(private val builder: RegexBuilder) {
        fun pattern(pattern: String, reason: String? = null) {
            builder.detectionPatterns.add(PatternWithReason(Regex(pattern), reason))
        }

        fun ignore(pattern: String, reason: String? = null) {
            builder.ignorePatterns.add(PatternWithReason(Regex(pattern), reason))
        }
    }

    class IgnorePatterns(private val builder: RegexBuilder, private val prefix: String) {
        fun component(name: String, reason: String? = null) {
            builder.ignorePatterns.add(
                PatternWithReason(
                    Regex("$prefix$name"),
                    reason
                )
            )
        }

        fun pattern(pattern: String, reason: String? = null) {
            builder.ignorePatterns.add(
                PatternWithReason(
                    Regex(pattern),
                    reason
                )
            )
        }
    }

    class DetectPatterns(private val builder: RegexBuilder, private val prefix: String) {
        fun component(name: String, reason: String? = null) {
            builder.detectionPatterns.add(
                PatternWithReason(
                    Regex("$prefix$name"),
                    reason
                )
            )
        }

        fun pattern(pattern: String, reason: String? = null) {
            builder.detectionPatterns.add(
                PatternWithReason(
                    Regex(pattern),
                    reason
                )
            )
        }
    }

    fun build(sourceFolder: String, tempFolder: String, folderToCompare: String? = null): CopyConfig {
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

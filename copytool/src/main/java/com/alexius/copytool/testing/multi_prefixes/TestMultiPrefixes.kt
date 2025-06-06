package com.alexius.copytool.testing.multi_prefixes

import com.alexius.copytool.old_regex_builder.RegexTemplates
import com.alexius.copytool.copyProject
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testMultiPrefixes() {
    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\multi_prefixes\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\multi_prefixes\\target"

    val config = RegexTemplates.custom()
        .addComponentConversion("Ms", "Cmp")
        .addComponentConversion("My", "Cmp")
        .addComponentConversion("Base", "Cmp")
        .addVariableConversion("ms", "cmp")
        .addVariableConversion("my", "cmp")
        .addVariableConversion("base", "cmp")
        .addFileNameConversion("Ms([A-Z][a-zA-Z0-9]*)\\.kt", "Cmp$1.kt")
        .addFileNameConversion("My([A-Z][a-zA-Z0-9]*)\\.kt", "Cmp$1.kt")
        .addFileNameConversion("Base([A-Z][a-zA-Z0-9]*)\\.kt", "Cmp$1.kt")
        .build(
            "$SOURCE_DIR\\components",
            test.TMP_DIR + "\\components",
            "$TARGET_DIR\\components"
        )

    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testMultiPrefixes()
}
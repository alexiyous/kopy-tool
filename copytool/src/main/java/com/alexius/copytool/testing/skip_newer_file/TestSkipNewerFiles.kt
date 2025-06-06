package com.alexius.copytool.testing.skip_newer_file

import com.alexius.copytool.copyProject
import com.alexius.copytool.new_regex_builder.RegexTemplates
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testSkipNewerFiles() {

    val SOURCE_DIR = File(".").canonicalPath + "\\connector\\src\\main\\java\\code\\apps\\als\\connector\\testing\\skip_newer_file\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\connector\\src\\main\\java\\code\\apps\\als\\connector\\testing\\skip_newer_file\\target"
    val TMP_DIR = File(".").canonicalPath + "\\connector\\src\\main\\java\\code\\apps\\als\\connector\\testing\\skip_newer_file\\test_result\\tempDir"
    // Setup test environment
   /* setupTestFiles(SOURCE_DIR, TARGET_DIR)*/

    // Test with skip enabled
    println("\n=== Testing with skip feature enabled ===")
    val configWithSkip = RegexTemplates {
        skipNewerFiles(true)

        conversions {
            // Convert component names
            component(
                fromPrefix = "Old",
                toPrefix = "New"
            )
            // Convert component names
            component(
                fromPrefix = "Present",
                toPrefix = "New"
            )
            // Convert variable names
            variable(
                fromPrefix = "old",
                toPrefix = "new"
            )
            // Convert file names
            fileName(
                fromPattern = "Old([A-Z][a-zA-Z0-9]*)\\.kt",
                toPattern = "New$1.kt"
            )
        }
    }.build(
        sourceFolder = SOURCE_DIR,
        tempFolder = TMP_DIR,
        folderToCompare = TARGET_DIR
    )

    copyProject(configWithSkip)
}

private fun setupTestFiles(sourceDir: String, targetDir: String) {
    // Create test directories
    File(sourceDir).mkdirs()
    File(targetDir).mkdirs()

    // Create source files
    File(sourceDir, "OldComponent.kt").apply {
        writeText("""
            class OldComponent {
                private val oldVariable = "test"
            }
        """.trimIndent())
        setLastModified(System.currentTimeMillis() - 10000) // Older file
    }

    File(sourceDir, "OldUtils.kt").apply {
        writeText("""
            object OldUtils {
                fun oldFunction() {}
            }
        """.trimIndent())
        setLastModified(System.currentTimeMillis() - 10000) // Older file
    }
}

fun main() {
    testSkipNewerFiles()
}
package com.alexius.copytool.testing.detect_directories


import com.alexius.copytool.copyProject
import com.alexius.copytool.new_regex_builder.RegexTemplates
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testDetectDirectoriesNewRegex() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\detect_directories\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\detect_directories\\target"

    val config = RegexTemplates {
        conversions {
            // Component name conversion (Ms* -> Cmp*)
            component(
                fromPrefix = "Ms",
                toPrefix = "Cmp"
            )

            // Directory path pattern conversion (ms_something -> cmp_something)
            directory(
                fromPattern = "ms_([a-z_]+)",
                toPattern = "cmp_$1"
            )
        }

        detections {
            directories {
                // Detect potentially sensitive directories
                detect(
                    pattern = "secret_.*",
                    reason = "Directory might contain secret information"
                )
                detect(
                    pattern = "private_.*",
                    reason = "Directory might contain private information"
                )
            }
        }
    }.build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)

    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testDetectDirectoriesNewRegex()
}
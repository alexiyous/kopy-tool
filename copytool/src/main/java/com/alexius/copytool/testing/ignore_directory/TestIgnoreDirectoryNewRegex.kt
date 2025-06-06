package com.alexius.copytool.testing.ignore_directory

import com.alexius.copytool.copyProject
import com.alexius.copytool.new_regex_builder.RegexTemplates
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testIgnoreDirectoryNewRegex() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\ignore_directory\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\ignore_directory\\target"

    val config = RegexTemplates {
        conversions {
            // Convert component names
            component(
                fromPrefix = "Ms",
                toPrefix = "Cmp"
            )

            // Convert directory names
            directory(
                fromPattern = "ms_([a-z_]+)",
                toPattern = "cmp_$1"
            )
        }

        detections {
            directories {
                // Define directories to ignore
                ignore(
                    pattern = "deprecated_.*",
                    reason = "it is deprecated, so we ignore it"
                )
                ignore(
                    pattern = "temp_.*",
                    reason = "it is temporary, so we ignore it"
                )
            }
        }
    }.build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)


    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testIgnoreDirectoryNewRegex()
}
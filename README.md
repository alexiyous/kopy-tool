# Kopy Tool

A powerful utility for copying and transforming Android project files with configurable regex patterns.

## Overview

Kopy Tool helps Android developers to easily copy and transform project files, allowing for:
- Package and import path transformations
- File and directory name substitutions
- Pattern detection for sensitive data
- Directory filtering
- And more...

## Installation

Add JitPack repository to your build file:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ....
        maven("https://jitpack.io")
    }
}
```

Add the dependency:

```gradle
dependencies {
    implementation ("com.github.alexiyous:kopy-tool:v1.0.2")
}
```

## Usage

Kopy Tool provides two approaches for configuration:

### 1. Traditional Builder Pattern

```kotlin
val config = RegexTemplates.custom()
    .addPackageAndImportsConversion("com.alexius.tool", "com.something.tool")
    .addComponentConversion("OldPrefix", "NewPrefix")
    .addFileNameConversion("OldName", "NewName")
    .build(
        sourceFolder = "/path/to/source",
        tempFolder = "/path/to/destination"
    )

copyProject(config)
```

### 2. Modern DSL Approach

```kotlin
val config = RegexTemplates {
    conversions {
        packagesAndImports("com.alexius.tool", "com.something.tool")
        component("OldPrefix", "NewPrefix")
        fileName("OldName", "NewName")
    }
    
    detections {
        sensitive {
            pattern("password|secret|credential", "Security risk")
        }
        
        directories {
            ignore("build", "Skip build artifacts")
        }
    }
    
    skipNewerFiles()
}

copyProject(config.build(
    sourceFolder = "/path/to/source",
    tempFolder = "/path/to/destination"
))
```

## Key Features

### Package and Import Path Transformations

Only changes text that appears after "package" or "import" statements:

```kotlin
packagesAndImports("com.alexius.tool", "com.something.tool")
```

### Component and Variable Name Conversions

Transform class names and variables:

```kotlin
// Transform OldPrefixSomething to NewPrefixSomething
component("OldPrefix", "NewPrefix")

// Transform oldPrefixVariable to newPrefixVariable
variable("oldPrefix", "newPrefix")
```

### Sensitive Data Detection

Prevent copying files with sensitive information:

```kotlin
sensitive {
    pattern("password|apiKey", "Security risk")
}
```

### Directory Filtering

Skip or detect specific directories:

```kotlin
directories {
    ignore("build", "Skip build artifacts")
    detect("secret", "Sensitive directory")
}
```

## Examples

Check the `copytool/src/main/java/com/alexius/copytool/testing` directory in the repository for more examples.

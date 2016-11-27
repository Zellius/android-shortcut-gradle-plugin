package ru.solodovnikov.shortcuthelper

import org.gradle.api.GradleException
import org.gradle.api.Project

class ShortcutHelperExtension {
    public static final String EXTENSION_NAME = "shortcutHelper";

    //Path to the shortcut.xml
    File filePath

    private final Project project

    ShortcutHelperExtension(Project project) {
        this.project = project
    }

    void setFilePath(filePath) {
        if (!filePath || filePath.empty) {
            throw new GradleException('File path cannot be null or empty!')
        }

        if (filePath instanceof File) {
            this.filePath = filePath
        } else {
            this.filePath = project.file(filePath)
        }

        if (!this.filePath.file || !this.filePath.exists()) {
            throw new GradleException('File path must contain XML file!')
        }
    }
}

package ru.solodovnikov.shortcuthelper

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.initialization.dsl.ScriptHandler
import org.gradle.api.internal.DefaultDomainObjectSet

class ShortcutHelperPlugin implements Plugin<Project> {
    private static final String ANDROID_TOOLS_PLUGIN = 'com.android.tools.build'

    private ShortcutHelperExtension extension

    @Override
    void apply(Project project) {
        extension = project.extensions.create(ShortcutHelperExtension.EXTENSION_NAME, ShortcutHelperExtension, project)

        project.afterEvaluate {
            getProjectBuildVariants(project).all {
                def applicationId = it.applicationId
                def outputDir = project.file("$project.buildDir/generated/res/resValues/$it.dirName/xml")
                def task = project.tasks.create("prepare${it.name.capitalize()}ShortcutXML", PrepareShortcutTask) {
                    it.outputDir = outputDir
                    it.shortcutFile = extension.filePath
                    it.applicationId = applicationId
                }

                if (getAndroidToolsMajorVersion(project) >= 3) {
                    it.registerGeneratedResFolders(project.files(outputDir).builtBy(task))
                } else {
                    it.registerResGeneratingTask(task, outputDir)
                }
            }
        }
    }

    private DefaultDomainObjectSet<? extends BaseVariant> getProjectBuildVariants(Project project) {
        if (project.plugins.hasPlugin("android")) {
            final AppExtension appExtension = project['android']
            return appExtension.getApplicationVariants()
        } else if (project.plugins.hasPlugin('android-library')) {
            final LibraryExtension libraryExtension = project['android-library']
            return libraryExtension.getLibraryVariants()
        } else {
            throw new GradleException("Set android build types first")
        }
    }

    private int getAndroidToolsMajorVersion(Project project) {
        return project.buildscript
                .configurations[ScriptHandler.CLASSPATH_CONFIGURATION]
                .dependencies
                .find { it.group == ANDROID_TOOLS_PLUGIN }
                .version[0]
                .toInteger()
    }
}

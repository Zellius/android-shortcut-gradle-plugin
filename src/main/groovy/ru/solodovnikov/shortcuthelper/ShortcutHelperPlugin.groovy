package ru.solodovnikov.shortcuthelper

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.DefaultDomainObjectSet

class ShortcutHelperPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def extension = project.extensions.create(ShortcutHelperExtension.EXTENSION_NAME, ShortcutHelperExtension, project)

        project.afterEvaluate {
            getProjectBuildVariants(project).all {
                def applicationId = it.applicationId
                def outputDir = project.file("$project.buildDir/generated/res/resValues/$it.dirName/xml")
                def task = project.tasks.create("prepare${it.name.capitalize()}ShortcutXML", PrepareShortcutTask) {
                    it.outputDir = outputDir
                    it.shortcutFile = extension.filePath
                    it.applicationId = applicationId
                }

                def actualMethodMissing = it.metaClass.methodMissing
                it.metaClass.methodMissing = { String name, args ->
                    if (name == "registerGeneratedResFolders") {
                        it.registerResGeneratingTask(task, outputDir)
                    } else {
                        actualMethodMissing.invokeMethod(name, args)
                    }
                }
                it.registerGeneratedResFolders(project.files(outputDir).builtBy(task))
            }
        }
    }

    private static DefaultDomainObjectSet<? extends BaseVariant> getProjectBuildVariants(Project project) {
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
}

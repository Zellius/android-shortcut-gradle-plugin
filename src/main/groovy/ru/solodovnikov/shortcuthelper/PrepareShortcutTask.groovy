package ru.solodovnikov.shortcuthelper

import groovy.xml.XmlUtil
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction;

class PrepareShortcutTask extends DefaultTask {

    @OutputDirectory
    File outputDir

    @InputFile
    File shortcutFile

    @Input
    String applicationId

    @TaskAction
    void prepare() {
        def shortcuts = new XmlSlurper(false, false).parse(shortcutFile)
        shortcuts.shortcut.each {
            it.intent.each {
                it.attributes().put('android:targetPackage', applicationId)
            }
        }
        outputDir.mkdirs()
        def xmlUtil = new XmlUtil()
        def fw = new FileWriter(project.file("$outputDir/${shortcutFile.name}"))
        xmlUtil.serialize(shortcuts, fw)
        fw.close()
    }
}

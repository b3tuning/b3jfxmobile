/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2018, Gluon Software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.b3tuning.b3jfxmobile.plugin.android.task

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * Check if provided AndroidManifest.xml file exists if it is specified. When
 * it's not specified, create a default one.
 *
 * @author joeri
 */
class ValidateManifest extends DefaultTask {

    @OutputFile
    File output

    @TaskAction
    void validateManifest() {
        if (project.b3jfxmobile.android.manifest != null) {
            File manifestFile = project.file(project.b3jfxmobile.android.manifest)
            if (!manifestFile.exists()) {
                throw new GradleException("Configured manifest file is invalid: ${project.b3jfxmobile.android.manifest}")
            }

            Files.copy(manifestFile.toPath(), getOutput().toPath(), StandardCopyOption.REPLACE_EXISTING)
        } else {
            def projectVersion = project.version == 'unspecified' ? '1.0' : project.version

            // creating default AndroidManifest.xml
            project.b3jfxmobile.android.temporaryDirectory.mkdirs()
            if (!getOutput().exists()) {
                getOutput().withWriter { out ->
                    out.writeLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                    out.writeLine("<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\" package=\"${project.b3jfxmobile.android.applicationPackage}\" android:versionCode=\"1\" android:versionName=\"${projectVersion}\">")
                    out.writeLine("\t<supports-screens android:xlargeScreens=\"true\"/>")
                    out.writeLine("\t<uses-permission android:name=\"android.permission.INTERNET\"/>")
                    out.writeLine("\t<uses-permission android:name=\"android.permission.READ_EXTERNAL_STORAGE\"/>")
                    out.writeLine("\t<uses-permission android:name=\"android.permission.WRITE_EXTERNAL_STORAGE\"/>")
                    out.writeLine("\t<uses-sdk android:minSdkVersion=\"${project.b3jfxmobile.android.minSdkVersion}\" android:targetSdkVersion=\"${project.b3jfxmobile.android.targetSdkVersion}\"/>")
                    out.writeLine("\t<application android:label=\"${project.name}\" android:name=\"android.support.multidex.MultiDexApplication\">")
                    out.writeLine("\t\t<activity android:name=\"javafxports.android.FXActivity\" android:label=\"${project.name}\" android:configChanges=\"orientation|screenSize\">")
                    out.writeLine("\t\t\t<meta-data android:name=\"main.class\" android:value=\"${project.mainClassName}\"/>")
                    if (project.preloaderClassName != null && !project.preloaderClassName.empty) {
                        out.writeLine("\t\t\t<meta-data android:name=\"preloader.class\" android:value=\"${project.preloaderClassName}\"/>")
                    }
                    out.writeLine("\t\t\t<meta-data android:name=\"debug.port\" android:value=\"0\"/>")
                    out.writeLine("\t\t\t<intent-filter>")
                    out.writeLine("\t\t\t\t<action android:name=\"android.intent.action.MAIN\"/>")
                    out.writeLine("\t\t\t\t<category android:name=\"android.intent.category.LAUNCHER\"/>")
                    out.writeLine("\t\t\t</intent-filter>")
                    out.writeLine("\t\t</activity>")
                    out.writeLine("\t</application>")
                    out.writeLine("</manifest>")
                }
            }
        }
    }

}


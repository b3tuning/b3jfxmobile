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
package com.b3tuning.b3jfxmobile.plugin

import com.b3tuning.b3jfxmobile.plugin.ios.IosExtension
import org.gradle.api.Project

/**
 * Simplifies conversion of Gradle project attributes to Java based APIs
 */
class GvmConfig {

    final String rootDirName
    final String appName
    final String mainClassName
    final String launchDir
    final String iosSignIdentity;
    final String iosProvisioningProfile;
    final String minOSVersion

    final String[]     forcelinkClasses
    final String[] runtimeModules;
    final List<String> jarDependecies
    final String[] ignoreNativeLibs;
    
    final String[] frameworks
    final String[] frameworksPaths;

    final boolean smallIio;

    final IosExtension ios

    GvmConfig( Project project ) {

        Map<String, ?> projectProps = project.getProperties()

        jarDependecies = project.configurations
                                .getByName("iosRuntime")
                                .resolve()
                                .collect{it.getAbsolutePath()}
                                .toList()

        rootDirName      = project.getProjectDir().getAbsolutePath()
        appName          = project.getName()
        mainClassName    = (String) projectProps.get("mainClassName")
        forcelinkClasses = project.b3jfxmobile.ios.forceLinkClasses as String[]
        ignoreNativeLibs = project.b3jfxmobile.ios.ignoreNativeLibs as String[]
        runtimeModules   = project.b3jfxmobile.ios.runtimeModules as String[]
        smallIio         = project.b3jfxmobile.ios.smallIio;
        launchDir        = rootDirName + "/build/gvm/" + appName + ".app"
        frameworks       = project.b3jfxmobile.ios.frameworks as String[]
        frameworksPaths  = project.b3jfxmobile.ios.frameworksPaths as String[]
        ios              = project.b3jfxmobile.ios
        iosSignIdentity  = project.b3jfxmobile.ios.iosSignIdentity;
        iosProvisioningProfile = project.b3jfxmobile.ios.iosProvisioningProfile;
        minOSVersion     = project.b3jfxmobile.ios.minOSVersion;
    }




}

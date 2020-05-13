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
package com.b3tuning.b3jfxmobile.plugin.embedded.task

import com.b3tuning.b3jfxmobile.plugin.embedded.RemotePlatformConfiguration
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

class CopyRemoteDir extends DefaultTask {

    @InputDirectory
    File from
    @Input
	RemotePlatformConfiguration remotePlatform

    @TaskAction
    void copyJar() {
        RemotePlatformConfiguration cfg = getRemotePlatform()

        if (cfg.password != null) {
            project.ant.sshexec(host: cfg.host, port: "${cfg.getPort()}", username: cfg.username, password: cfg.password, trust: 'true',
                    command: "mkdir -p ${cfg.workingDir}/${project.name}")
            project.ant.scp(todir: "${cfg.username}@${cfg.host}:${cfg.workingDir}/${project.name}", port: "${cfg.getPort()}", password: cfg.password, trust: 'true') {
                fileset(dir: getFrom())
            }
        } else {
            project.ant.sshexec(host: cfg.host, port: "${cfg.getPort()}", username: cfg.username, keyfile: cfg.keyfile, passphrase: cfg.passphrase, trust: 'true',
                    command: "mkdir -p ${cfg.workingDir}/${project.name}")
            project.ant.scp(todir: "${cfg.username}@${cfg.host}:${cfg.workingDir}/${project.name}", port: "${cfg.getPort()}", keyfile: cfg.keyfile, passphrase: cfg.passphrase, trust: 'true') {
                fileset(dir: getFrom())
            }
        }
    }
}

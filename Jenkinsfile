@NonCPS
def getChangelog() {
    def changeLogSets = currentBuild.changeSets
    def lines = []
    for (int i = 0; i < changeLogSets.size(); i++) {
        def entries = changeLogSets[i].items
        for (int j = 0; j < entries.length; j++) {
            def entry = entries[j]
            lines << "${entry}"
        }
    }
    return lines.join("\n").toString()
}

def s3uploadDefault = { dir, pattern, encoding ->
    withAWS (region:'eu-central-1', credentials:'aws-credentials') {
        if (!pattern.contains(".zip")) {
            s3Upload(bucket: 'apps-builds', workingDir: dir, path: "geogebra/branches/${env.GIT_BRANCH}/${env.BUILD_NUMBER}/",
               includePathPattern: pattern, acl: 'PublicRead', contentEncoding: encoding)
        }
        s3Upload(bucket: 'apps-builds', workingDir: dir, path: "geogebra/branches/${env.GIT_BRANCH}/latest/",
            includePathPattern: pattern, acl: 'PublicRead', contentEncoding: encoding)
    }
}

pipeline {
    agent any
    stages {
        stage('build') {
            steps {
                writeFile file: 'changes.csv', text: getChangelog()
            }
        }
        stage('archive') {
            steps {
                script {
                    withAWS (region:'eu-central-1', credentials:'aws-credentials') {
                       s3Delete(bucket: 'apps-builds', path: "geogebra/branches/${env.GIT_BRANCH}/latest")
                    }
                    s3uploadDefault(".", "changes.csv", "")
                }
            }
        }
    }
    post {
        always { 
           cleanWs() 
        }
        failure {
            slackSend(color: 'danger', tokenCredentialId: 'slack.token', username: 'jenkins',
                message:  "${env.JOB_NAME} [${env.BUILD_NUMBER}]: Build failed. (<${env.BUILD_URL}|Open>)")
        }
        unstable {
            slackSend(color: 'warning', tokenCredentialId: 'slack.token', username: 'jenkins',
                message:  "${env.JOB_NAME} [${env.BUILD_NUMBER}]: Unstable. (<${env.BUILD_URL}|Open>)")
        }
        fixed {
            slackSend(color: 'good', tokenCredentialId: 'slack.token', username: 'jenkins',
                message:  "${env.JOB_NAME} [${env.BUILD_NUMBER}]: Back to normal. (<${env.BUILD_URL}|Open>)")
        }
    }
}

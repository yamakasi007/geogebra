def createChangelog = { fileName ->
    def changeLogSets = currentBuild.changeSets
    def content = []
    for (int i = 0; i < changeLogSets.size(); i++) {
        def entries = changeLogSets[i].items
        for (int j = 0; j < entries.length; j++) {
            def entry = entries[j]
            content << "${entry.commitId},${entry.author.toString()},${new Date(entry.timestamp)},${entry.msg}"
        }
    }
    writeFile file: fileName, text: content.join("\n")
}

def s3uploadDefault = { dir, pattern ->
    withAWS(region:'eu-west-1', credentials:'aws-credentials') {
        s3Upload(bucket: 'apps-builds', workingDir: dir, path: "geogebra/branches/${env.GIT_BRANCH}/${env.BUILD_NUMBER}/$dir", 
            includePathPattern: pattern, acl: 'PublicRead')
    }
}

pipeline {
    agent any
    stages {
        stage('archive') {
            steps {
                script {
                    createChangelog('changes.csv')
                    s3uploadDefault(".", "changes.csv")
                    s3uploadDefault("web/war", "web3d/*")
                    s3uploadDefault("web/war", "webSimple/*")
                    s3uploadDefault("web/war", "*.html")
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

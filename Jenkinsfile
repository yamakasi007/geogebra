@NonCPS
def createChangelog(String fileName) {
    def changeLogSets = currentBuild.changeSets
    def lines = []
    for (int i = 0; i < changeLogSets.size(); i++) {
        def entries = changeLogSets[i].items
        for (int j = 0; j < entries.length; j++) {
            def entry = entries[j]
            lines << "${entry.commitId},${entry.author.toString()},${new Date(entry.timestamp)},${entry.msg}"
        }
    }
    def content = lines.join("\n")
    writeFile file: fileName, text: content
}

def s3uploadDefault = { dir, pattern ->
    withAWS (region:'eu-west-1', credentials:'aws-credentials') {
        s3Upload(bucket: 'apps-builds', workingDir: dir, path: "geogebra/branches/${env.GIT_BRANCH}/${env.BUILD_NUMBER}",
            includePathPattern: pattern, acl: 'PublicRead')
        s3Upload(bucket: 'apps-builds', workingDir: dir, path: "geogebra/branches/${env.GIT_BRANCH}/latest",
            includePathPattern: pattern, acl: 'PublicRead')
    }
}

pipeline {
    agent any
    stages {
        stage('build') {
            steps {
                script {
                    createChangelog('changes.csv')
                }
                sh label: 'clean', script: './gradlew clean'
                sh label: 'build web', script: './gradlew :web:compileGwt :web:symlinkIntoWar :web:createDraftBundleZip :web:mergeDeploy'
                sh label: 'test', script: './gradlew :common-jre:test :desktop:test :common-jre:jacocoTestReport :web:test'
                sh label: 'static analysis', script: './gradlew checkPmd :editor-base:spotbugsMain :web:spotbugsMain :desktop:spotbugsMain :ggbjdk:spotbugsMain :common-jre:spotbugsMain --max-workers=1'
                sh label: 'spotbugs common', script: './gradlew :common:spotbugsMain'
                sh label: 'code style', script: './gradlew :web:cpdCheck checkAllStyles'
            }
        }
        stage('reports') {
            steps {
                junit '**/build/test-results/test/*.xml'
                recordIssues tools: [
                    cpd(pattern: '**/build/reports/cpd/cpdCheck.xml'),
                    checkStyle(pattern: '**/build/reports/checkstyle/*.xml')
                ]
                recordIssues qualityGates: [[threshold: 1, type: 'TOTAL', unstable: true]], tools: [
                    spotBugs(pattern: '**/build/reports/spotbugs/*.xml', useRankAsPriority: true), 
                    pmdParser(pattern: '**/build/reports/pmd/main.xml')
                ]
                publishCoverage adapters: [jacocoAdapter('**/build/reports/jacoco/test/*.xml')],
                    sourceFileResolver: sourceFiles('NEVER_STORE')
            }
        }
        stage('archive') {
            steps {
                script {
                    withAWS (region:'eu-west-1', credentials:'aws-credentials') {
                       s3Delete(bucket: 'apps-builds', workingDir: dir, path: "geogebra/branches/${env.GIT_BRANCH}/latest")
                    }
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

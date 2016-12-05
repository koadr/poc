#!groovy

stage('Checkout') {
    node {
        // some block
        checkout scm
        stash "poc"
    }
}

stage("Build") {
    node {
        unstash "poc"
        withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
            sh "sbt clean compile"
        }
    }
}

stage("Deploy") {
    node {
        unstash "poc"
        withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
            // This step should not normally be used in your script. Consult the inline help for details.
            withDockerRegistry([credentialsId: '248b3cd6-9575-4bf5-aefe-12188ab9d2ba', url: 'https://koadr-on.azurecr.io']) {
                sh "mkdir target/docker"
                sh "sbt docker"
                dir('target/docker') {
                    // some block
                    def poc = docker.build "koadr-on.azurecr.io/poc:${env.BUILD_TAG}"
                    poc.push()
                }
            }
        }
    }

}

stage('Testing') {
    parallel unitTesting: {
        node {
            unstash "poc"
            withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
                sh "sbt test"
            }
        }
    }, integrationTesting: {
        node("linux") {
            unstash "poc"
            withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
                sh "sbt it:test"
            }
        }
    }, stylecheck: {
        node {
            unstash "poc"
            withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
                sh "sbt scalastyle"
                step([$class: 'CheckStylePublisher', canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'target/scalastyle-result.xml', unHealthy: ''])
            }
        }
    }, reports: {
        node {
            unstash "poc"
            withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
                sh "sbt test"
                sh "sbt it:test"
                junit 'target/test-reports/*.xml'
            }
        }
    }
    failFast: true
}



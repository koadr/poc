#!groovy

stage('Checkout') {
    node {
        // some block
        checkout scm
        stash "poc"
    }
}

stage("Build") {
    node("slave") {
        unstash "poc"
        withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
            sh "sbt clean compile"
        }
    }
}

stage("Deploy") {
    node("slave") {
        unstash "poc"
        withEnv(["PATH+SBT=${tool 'sbt'}/bin","PATH+DOCKER=${tool 'docker'}/bin"]) {
            sh "sbt docker"
            // This step should not normally be used in your script. Consult the inline help for details.
            withDockerRegistry([credentialsId: '248b3cd6-9575-4bf5-aefe-12188ab9d2ba', url: 'https://koadr-on.azurecr.io']) {
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
        node("slave") {
            unstash "poc"
            withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
                sh "sbt test"
            }
        }
    }, integrationTesting: {
        node("slave") {
            unstash "poc"
            withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
                sh "sbt it:test"
            }
        }
    }, stylecheck: {
        node("slave") {
            unstash "poc"
            withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
                sh "sbt scalastyle"
                step([$class: 'CheckStylePublisher', canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'target/scalastyle-result.xml', unHealthy: ''])
            }
        }
    }, reports: {
        node("slave") {
            unstash "poc"
            withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
                sh "sbt coverage test"
                sh "sbt coverage it:test"
                junit 'target/test-reports/*.xml'
            }
        }
    }
    failFast: true
}

stage("Performance") {

}



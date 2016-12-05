#!groovy

stage('Checkout') {
    node("linux") {
        // some block
        checkout scm
        stash "poc"
    }
}

stage("Build") {
    node("linux") {
        unstash "poc"
        withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
            sh "sbt clean compile"
        }
    }
}

stage('Testing') {
    parallel unitTesting: {
        node("linux") {
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
        node("linux") {
            unstash "poc"
            withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
                sh "sbt scalastyle"
                step([$class: 'CheckStylePublisher', canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'target/scalastyle-result.xml', unHealthy: ''])
            }
        }
    }, reports: {
        node("linux") {
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

stage("Deploy") {
    node("docker") {
        unstash "poc"
        withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
            sh "sbt docker"
            // This step should not normally be used in your script. Consult the inline help for details.
            withDockerRegistry([credentialsId: '248b3cd6-9575-4bf5-aefe-12188ab9d2ba', url: 'koadr-on.azurecr.io']) {
                dir('target/docker') {
                    // some block
                    def poc = docker.build "koadr-on.azurecr.io/poc:${env.BUILD_TAG}"
                    poc.push()
                }
            }
        }
    }

}



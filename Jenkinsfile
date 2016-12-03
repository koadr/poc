#!groovy

stage('Checkout') {
    node("slave") {
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



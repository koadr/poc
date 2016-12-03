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

stage('Testing') {
    parallel unitTesting: {
        node {
            unstash "poc"
            withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
                sh "sbt test"
            }
        }
    }, integrationTesting: {
        node {
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
                sh "sbt coverage test"
                sh """
                    sbt coverage "it:test" 
                """
                junit 'target/test-reports/*.xml'
            }
        }
    }
    failFast: true
}



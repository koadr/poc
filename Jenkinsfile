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
                step([$class: 'CheckStylePublisher', canComputeNew: false, defaultEncoding: '', healthy: '', pattern: '**/checkstyle-result.xml.', unHealthy: ''])
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
                step([$class: 'CheckStylePublisher', canComputeNew: false, defaultEncoding: '', healthy: '', pattern: '**/scalastyle-result.xml.', unHealthy: ''])
            }
        }
    }
    failFast: true
}



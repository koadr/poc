#!groovy

stage('Checkout') {
    node {
        // some block
        checkout scm
    }
}

stage("Build") {
    node {
        withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
            sh "sbt clean compile"
        }
    }
}

stage('Testing') {
    parallel unitTesting: {
        node {
            withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
                sh "sbt test"
            }
        }
    }, integrationTesting: {
        node {
            withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
                sh 'sbt it:test'

            }
        }
    },
    failFast: true
}


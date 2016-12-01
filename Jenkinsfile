#!groovy

stage('Checkout') {
    node {
        // some block
        checkout scm
    }
}

stage("Build") {
    node {
        checkout scm
        withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
            sh "sbt clean compile"
        }
    }
}

stage('Testing') {
    parallel unitTesting: {
        node {
            checkout scm
            withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
                sh "sbt test"
            }
        }
    }, integrationTesting: {
        node {
            checkout scm
            withEnv(["PATH+SBT=${tool 'sbt'}/bin"]) {
                sh 'sbt it'

            }
        }
    },
    failFast: true
}


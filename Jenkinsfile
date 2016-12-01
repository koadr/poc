#!groovy

stage('Checkout') {
    node {
        // some block
        checkout scm
    }
}

stage("Build") {
    node {
        sh "sbt 'clean compile'"
    }
}

stage('Testing') {
    parallel unitTesting: {
        node {
            sh "sbt test"
        }
    }, integrationTesting: {
        node {
            sh "sbt 'it-test'"
        }
    },
    failFast: true
}


#!groovy

stage('Checkout') {
    // some block
    checkout scm
}

stage("Build") {
    sh "sbt 'clean compile'"
}

stage('Testing') {
    parallel unitTesting: {
        sh "sbt test"
    }, integrationTesting: {
        sh "sbt 'it-test'"
    },
    failFast: true
}


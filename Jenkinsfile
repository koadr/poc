#!groovy

stage('Checkout') {
    node {
        // some block
        checkout scm
    }
}

stage("Build") {
    node {
        def sbtHome = tool 'sbt'
        sh "${sbtHome} 'clean compile'"
    }
}

stage('Testing') {
    parallel unitTesting: {
        node {
            def sbtHome = tool 'sbt'
            sh "${sbtHome} test"
        }
    }, integrationTesting: {
        node {
            def sbtHome = tool 'sbt'
            sh "${sbtHome} 'it-test'"
        }
    },
    failFast: true
}


pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    stages {

        stage('Checkout') {
            steps {
                git url: 'https://your-repo-url.git'
            }
        }

        stage('Run Gatling Tests') {
            steps {
                sh 'mvn clean gatling:test'
            }
        }
    }

    post {
        always {
            publishHTML (target: [
                reportDir: 'target/gatling',
                reportFiles: 'index.html',
                reportName: 'Gatling Report'
            ])
        }
    }
}
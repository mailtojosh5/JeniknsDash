pipeline {
    agent any

    parameters {
        string(name: 'SIMULATION_CLASS', defaultValue: 'com.example.performance.simulations.SmokeSimulation', description: 'Fully qualified Gatling simulation class to run')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn clean test-compile'
                    } else {
                        bat 'mvn clean test-compile'
                    }
                }
            }
        }

        stage('Run Gatling') {
            steps {
                script {
                    if (isUnix()) {
                        sh "mvn gatling:test -Dgatling.simulationClass=${params.SIMULATION_CLASS}"
                    } else {
                        bat "mvn gatling:test -Dgatling.simulationClass=%SIMULATION_CLASS%"
                    }
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/gatling/**/*.*', allowEmptyArchive: true
        }
    }
}

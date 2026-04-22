pipeline {
  agent any

  tools {
    // Update these tool names to match your Jenkins global tool configuration
    jdk 'jdk11'
    maven 'maven'
  }

  parameters {
    string(name: 'GATLING_SIMULATION', defaultValue: '', description: 'Fully qualified Gatling simulation class to run (optional)')
  }

  environment {
    MAVEN_OPTS = '-Xmx2g'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build and Run Gatling') {
      steps {
        script {
          def mvnCommand = 'mvn clean gatling:test'
          if (params.GATLING_SIMULATION?.trim()) {
            mvnCommand += " -Dgatling.simulationClass=${params.GATLING_SIMULATION}"
          }

          if (isUnix()) {
            sh mvnCommand
          } else {
            bat mvnCommand
          }
        }
      }
    }

    stage('Archive Gatling Reports') {
      steps {
        archiveArtifacts artifacts: 'target/gatling/**/*', allowEmptyArchive: true
      }
    }
  }

  post {
    success {
      echo 'Build completed successfully.'
    }
    failure {
      echo 'Build failed. Check the console output and archived artifacts for details.'
    }
  }
}

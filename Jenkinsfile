pipeline {
    agent any

    environment {
        APP_NAME = "jenkins-dashboard"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Install Dependencies') {
            steps {
                bat 'npm install'
            }
        }

        stage('Install PM2') {
            steps {
                bat '''
                where pm2 >nul 2>nul
                if %errorlevel% neq 0 (
                    echo Installing PM2...
                    npm install -g pm2
                ) else (
                    echo PM2 already installed
                )
                '''
            }
        }

        stage('Deploy (Zero Downtime)') {
            steps {
                bat '''
                pm2 describe %APP_NAME% >nul 2>nul

                if %errorlevel%==0 (
                    echo Reloading app (zero downtime)
                    pm2 reload %APP_NAME%
                ) else (
                    echo Starting app
                    pm2 start server.js --name %APP_NAME%
                )
                '''
            }
        }

        stage('Save PM2') {
            steps {
                bat 'pm2 save'
            }
        }
    }

    post {
        success {
            echo "Dashboard deployed successfully 🚀"
        }
        failure {
            echo "Deployment failed ❌"
        }
    }
}
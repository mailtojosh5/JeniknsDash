pipeline {
    agent any

    environment {
        APP_NAME = "jenkins-dashboard"
        PORT = "3000"
    }

    stages {

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Install Dependencies') {
            steps {
                sh 'npm install'
            }
        }

        /*
        ----------------------------------
        INSTALL PM2 LOCALLY (NO ROOT)
        ----------------------------------
        */
        stage('Install PM2 (Local)') {
            steps {
                sh '''
                if ! command -v pm2 &> /dev/null
                then
                    echo "📦 Installing PM2 locally..."
                    npm install -g pm2
                else
                    echo "✅ PM2 already installed"
                fi
                '''
            }
        }

        /*
        ----------------------------------
        START OR RELOAD APP
        ----------------------------------
        */
        stage('Deploy with PM2 (Zero Downtime)') {
            steps {
                sh '''
                export PATH=$PATH:$(npm bin -g)

                if pm2 list | grep -q "$APP_NAME"; then
                    echo "🔄 Reloading existing app (zero downtime)"
                    pm2 reload $APP_NAME
                else
                    echo "🚀 Starting new app"
                    pm2 start server.js --name $APP_NAME --watch
                fi
                '''
            }
        }

        /*
        ----------------------------------
        SAVE PM2 STATE (NO STARTUP REQUIRED)
        ----------------------------------
        */
        stage('Save PM2 Process List') {
            steps {
                sh '''
                pm2 save || echo "⚠️ pm2 save skipped (no startup access)"
                '''
            }
        }
    }

    post {

        success {
            echo "✅ Dashboard deployed successfully"
            echo "🌐 http://<jenkins-server-ip>:3000"
        }

        failure {
            echo "❌ Deployment failed - check logs"
        }
    }
}
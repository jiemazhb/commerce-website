pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // 检出代码库
                git branch: 'main', url: 'https://github.com/jiemazhb/commerce-website.git'
            }
        }

        stage('Build') {
            steps {
                // 编译项目
                bat 'C:\\path\\to\\project\\mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Start Services') {
            steps {
                // 启动服务
                bat 'docker-compose up -d config-server'
                bat 'docker-compose up -d discovery'
                bat 'docker-compose up -d customer identity-service product gateway'
                // 根据需要启动其他服务
            }
        }

        stage('Run Tests') {
            steps {
                // 运行测试
                bat 'C:\\path\\to\\project\\mvnw.cmd test'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Starting local deployment...'
                bat 'docker-compose up -d'  // 启动所有服务
                echo 'Services are up and running on your local machine.'
            }
        }
    }

    post {
        always {
            // 任务结束后，停止并移除容器
            bat 'docker-compose down'
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}

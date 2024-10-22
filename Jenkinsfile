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
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Start Services') {
            steps {
                // 启动服务
                sh 'docker-compose up -d config-server'
                sh 'docker-compose up -d discovery'
                sh 'docker-compose up -d customer identity-service product gateway'
                // 根据需要启动其他服务
            }
        }

        stage('Run Tests') {
            steps {
                // 运行测试
                sh './mvnw test'
            }
        }

        stage('Deploy') {
            steps {
                // 部署步骤，视具体情况而定
            }
        }
    }

    post {
        always {
            // 任务结束后，停止并移除容器
            sh 'docker-compose down'
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}

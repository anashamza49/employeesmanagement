pipeline {
    agent any
    tools {
        maven 'MyMaven'
    }
    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-auth'
        DOCKER_REPO = 'anashamza49/employees-app'
        DOCKER_TAG = 'latest'
        SONARQUBE_ENV_NAME = 'MySonarQubeServer'
        SONARQUBE_PROJECT_KEY = 'my-employees-project'
    }
    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }
        stage('Build Application') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Run Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Run SonarQube Analysis') {
            steps {
                withSonarQubeEnv(SONARQUBE_ENV_NAME) {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=${SONARQUBE_PROJECT_KEY}'
                }
            }
        }
        stage('Package Application') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Verify JAR File Existence') {
            steps {
                script {
                    if (!fileExists('target/employeesmanagement-0.0.1-SNAPSHOT.jar')) {
                        error 'JAR file not found. Build failed.'
                    } else {
                        echo 'JAR file found successfully.'
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    appImage = docker.build("${DOCKER_REPO}:${DOCKER_TAG}", ".")
                }
            }
        }
        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('', DOCKER_CREDENTIALS_ID) {
                        appImage.push()
                    }
                }
            }
        }
        stage('Deploy to Remote Server') {
            steps {
                sshagent([REMOTE_SERVER_SSH]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no $REMOTE_SERVER "
                        docker stop \$(docker ps -q --filter ancestor=${DOCKER_REPO}:${DOCKER_TAG}) || true &&
                        docker rm \$(docker ps -q --filter ancestor=${DOCKER_REPO}:${DOCKER_TAG}) || true &&
                        docker pull ${DOCKER_REPO}:${DOCKER_TAG} &&
                        docker run -d -p 8080:8080 ${DOCKER_REPO}:${DOCKER_TAG}
                    "
                    """
                }
            }
        }
    }
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check the logs for details.'
        }
    }
}

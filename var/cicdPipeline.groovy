def call(Map config) {

    pipeline {
        agent any

        stages {

            stage('Checkout') {
                steps {
                    echo 'git checkout stage'
                    git branch: config.branch, url: config.repo
                }
            }

            stage('Build') {
                steps {
                    echo 'Building with Maven'
                    sh 'mvn clean install'
                }
            }

            stage('Deploy') {
                steps {
                    echo 'Deploying to Tomcat'
                    deploy adapters: [
                        tomcat9(
                            credentialsId: config.credentialsId,
                            url: config.url
                        )
                    ],
                    contextPath: config.contextPath,
                    war: config.warFile
                }
            }
        }
    }
}

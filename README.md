
#  Jenkins Shared Library - CI/CD Pipeline (Maven + Tomcat)

This project demonstrates how to implement a **Jenkins Shared Library** to standardize and reuse CI/CD pipelines across multiple projects.

---

##  Overview

Instead of writing the same Jenkins pipeline code in every project, we create a **shared library** that contains reusable pipeline logic for:

- ✅ Git Checkout  
- ✅ Maven Build  
- ✅ Tomcat Deployment  

---

## Project Structure

```

jenkins-shared-library/
│
├── vars/
│   └── cicdPipeline.groovy
│
└── src/

````

---

##  Shared Library Code

###  `vars/cicdPipeline.groovy`

```groovy
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
````

---

## 🔧 Jenkins Configuration

1. Go to:

   ```
   Manage Jenkins → Configure System
   ```

2. Scroll to:

   ```
   Global Pipeline Libraries
   ```

3. Add a new library:

   * **Name:** `my-shared-lib`
   * **Default version:** `main`
   * **SCM:** Git
   * **Repository URL:** `https://github.com/jadalaramani/tomcat_shared_library.git`

---

##  Jenkinsfile (Usage)

```groovy
@Library('my-shared-lib') _

cicdPipeline(
    branch: 'main',
    repo: 'https://github.com/jadalaramani/tomcat_shared_library.git',
    credentialsId: 'tomcat',
    url: 'http://3.217.10.33:8081/',
    contextPath: 'shared-lib',
    warFile: '**/*.war'
)
```

---

##  Parameters Explained

| Parameter       | Description                    |
| --------------- | ------------------------------ |
| `branch`        | Git branch to checkout         |
| `repo`          | Git repository URL             |
| `credentialsId` | Jenkins credentials for Tomcat |
| `url`           | Tomcat server URL              |
| `contextPath`   | Application context path       |
| `warFile`       | WAR file location              |

---

##  Benefits of Shared Library

*  Reusability across multiple projects
*  Standardization of CI/CD pipelines
*  Cleaner Jenkinsfiles
*  Easy maintenance (change once, apply everywhere)
*  Centralized credential management

---

##  Common Mistakes

* ❌ Missing `call()` method in Groovy file
* ❌ Incorrect folder structure (`vars/` required)
* ❌ Hardcoding values instead of using parameters
* ❌ Wrong library name in `@Library`


createDockerJob("docker-test-container", 'sudo /usr/bin/docker run hello-world', "")

def createDockerJob(def jobName, def shellCommand, def gitRepository) {

    println "############################################################################################################"
    println "Creating Docker Job ${jobName} for gitRepository=${gitRepository}"
    println "############################################################################################################"

    job(jobName) {
        logRotator {
            numToKeep(10)
        }
        if ("${gitRepository}".size() <= 0) {
            println "############################################################################################################"
            println "No repository field provided for ${jobName}"
            println "############################################################################################################"
        } else {
            scm {
                git {
                    remote {
                        url(gitRepository)
                    }
                    extensions {
                        cleanAfterCheckout()
                    }
                }
            }
        }

        steps {
            steps {
                shell(shellCommand)
            }
        }
        publishers {
            chucknorris()
        }
    }
}

listView('docker') {
    description('')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex(/docker-.*/)
    }
    columns {
        status()
        buildButton()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
    }
}

![area](https://img.shields.io/badge/SmallSocialNetwork-backend-blue)
# Content-Service
This ktor api serves as the heart of the network.
This is where most of the social network data is managed.
The ktor logic is of course in Kotlin, but the data management is in JAVA.

# How to run
## run it via Docker
This repository contains a Dockerfile which enables the user-service to run in docker.

Since the user-service uses a properties file to store crucial information such as database credentials,
it is necessary to bind mount a folder with said config to the docker container.
This can be archived by using the [docker command](https://docs.docker.com/storage/bind-mounts/)
or by configuring it in [IntelliJ](https://www.jetbrains.com/help/idea/docker.html#volume_bindings) or in your Docker-environment.
The config folder should be bind like this: ```"D:\SmallSocialNetwork Config:/run/config"```.


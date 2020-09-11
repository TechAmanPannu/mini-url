# Mini Url

How to Start Locally:

Run Following Commands

1. Build project to jar
2. eval $(minikube docker-env) : Switch to minikube docker environment.
3. docker build -t <docker-user-name>/<image-name>:<tag> . 
4. kubectl apply -f k8s to run all containers or k8s/server-deployment.yaml to run specific configuration. 
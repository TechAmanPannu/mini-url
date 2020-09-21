# Mini Url

Basic setup Minikube
1. Install Minikube
3. Run minikube start --vm=true --memory=6GB

How to Start Locally:

Run Following Commands

1. Build project  jar
2. eval $(minikube docker-env) : Switch to minikube docker environment.
3. docker build -t <docker-user-name>/<image-name>:<tag> . 
4. kubectl apply -f k8s to run all containers or k8s/server-deployment.yaml to run specific configuration. 

Delete k8s pods
1. kubectl delete all --all : Temporary
2. kubectl delete all --all --grace-period=0 --force : Permanently

## Helm Makes things Easy
use Helm to setup Redis in local as well as production

1. Add redis for local: helm install mini_url_redis bitnami/redis

Install Redis using helm  with Args:

````text 
helm install mini-url-redis bitnami/redis \--set cluster.slaveCount=3 \
  --set usePassword=false \--set volumePermissions.enabled=true  \--set master.persistence.size=1Gi \ 
  --set slave.persistence.size=1Gi
  
2. Delete Redis: helm delete mini-url-redis

3 . Upgrade Redis : helm upgrade <arguements>

#Cassndra Query Limit
https://www.datastax.com/blog/2015/06/deep-look-cql-where-clause


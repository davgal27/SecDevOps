Task 1a - CloudNativePG PostgreSQL Cluster

This task replaces the original single PostgreSQL instance with a split Read-write (rw)/Read-only (ro) PostgreSQL cluste

Files:
- yamls/cnpg_cluster.yaml
  Creates app-secret and bootstraps the fnd-db PostgreSQL cluster with ordersdb owned by clusteruser.


Reproduction steps:
1. Start minikube with the Docker driver:
   minikube start --driver=docker

2. Add and update the CloudNativePG Helm repository:
   helm repo add cnpg https://cloudnative-pg.github.io/charts
   helm repo update

3. Install the CloudNativePG operator:
   helm upgrade --install cnpg cnpg/cloudnative-pg \
     --namespace cnpg-system \
     --create-namespace \
     -f cnpg_vals.yaml

4. Verify the operator:
   helm list -n cnpg-system
   kubectl get pods -n cnpg-system

5. Apply the PostgreSQL cluster manifest:
   kubectl apply -f cnpg_cluster.yaml

6. Verify the cluster:
   helm list -n cnpg-system
   kubectl get pods -n cnpg-system
   kubectl get cluster
   kubectl get pods
   kubectl get svc
   kubectl get pvc
   kubectl describe cluster fnd-db
   kubectl get events --sort-by=.metadata.creationTimestamp 

Task 1a - CloudNativePG PostgreSQL Cluster

Purpose:
This task replaces the original single PostgreSQL instance with a CloudNativePG-managed PostgreSQL cluster exposing separate read-write and read-only service endpoints.

Files:
- namespace.yaml
  Creates the foodndeliv Kubernetes namespace.

- cnpg_vals.yaml
  Helm values file used to install the CloudNativePG operator.

- cnpg_cluster.yaml
  Creates the fnd-db PostgreSQL cluster, the ordersdb database, and the myuser application database user.

- evidence/kubectl-session-task1a-raw.txt
  Complete terminal session dump showing the Helm installation, Kubernetes resource creation, PostgreSQL cluster creation, service verification, database connectivity test, events, and logs.

Reproduction steps:
1. Start minikube with the Docker driver:
   minikube start --driver=docker

2. Move into the Task 1a folder:
   cd /home/dave/Documents/Security/task1/task1a

3. Apply the namespace:
   kubectl apply -f namespace.yaml

4. Add and update the CloudNativePG Helm repository:
   helm repo add cnpg https://cloudnative-pg.github.io/charts
   helm repo update

5. Install the CloudNativePG operator:
   helm upgrade --install cnpg cnpg/cloudnative-pg \
     --namespace cnpg-system \
     --create-namespace \
     -f cnpg_vals.yaml

6. Verify the operator:
   helm list -n cnpg-system
   kubectl get pods -n cnpg-system -o wide
   kubectl get crd | grep cnpg

7. Apply the PostgreSQL cluster manifest:
   kubectl apply -f cnpg_cluster.yaml

8. Verify the cluster:
   kubectl get cluster -n foodndeliv
   kubectl get pods -n foodndeliv -o wide
   kubectl get svc -n foodndeliv -o wide
   kubectl get pvc -n foodndeliv
   kubectl get secret -n foodndeliv

Expected result:
- The cnpg Helm release is deployed in namespace cnpg-system.
- The CNPG operator pod is Running.
- The fnd-db PostgreSQL cluster exists in namespace foodndeliv.
- The cluster has 3 ready instances.
- The cluster status is healthy.
- fnd-db-1 is the primary instance.
- fnd-db-rw exists and selects the primary instance.
- fnd-db-ro exists and selects replica instances.
- fnd-db-r exists and selects all PostgreSQL instances.
- The ordersdb database is reachable using user myuser through fnd-db-rw.
- The database is empty at this stage, as expected for Task 1a.

Task 1b usage:
- The original read-write API service should use fnd-db-rw as its PostgreSQL host.
- The read-only API service should use fnd-db-ro as its PostgreSQL host.
EOF
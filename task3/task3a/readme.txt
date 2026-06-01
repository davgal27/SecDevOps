Task 3a: Deploy a Secured API Gateway using KrakenD and Keycloak

- fnd-task3: FoodNDeliv API backend connected to the CNPG database.

Files:
- ../codebase/
    Maven project for the FoodNDeliv API

- keycloak/
    - postgresql/ values.yaml
      Helm values for the Keycloak PostgreSQL database.

    - certificate.pem &  key.pem
      TLS certificate and key for Keycloak.
    
    - fnd-realm-export.json & master-realm-export.json
      Exported realm configurations.

- krakend/
    - Dockerfile               
      Docker image definition for KrakenD.

    - krakend.json             
      KrakenD gateway configuration defining all endpoints, RBAC, and ABAC rules.

- postman/
    Contains the Postman test screenshots for testing of the reproduced solution

- yamls/
    - fnd-task3-deployment.yaml    
      Deploys the FoodNDeliv API backend.

    - fnd-task3-svc.yaml           
      Exposes the FoodNDeliv API backend as a service.

    - kcsecrets.yaml               
      Keycloak client secret for the app.

    - keycloak-admin-secret.yaml   
      Keycloak admin credentials secret.

    - keycloak-service.yaml        
      Exposes Keycloak via LoadBalancer on ports 30080 (http) and 30443 (https).

    - keycloak.yaml                
      Keycloak CR deployment.

    - krakend-deployment.yaml      
      Deploys KrakenD with 3 replicas.

    - krakend-service.yaml         
      xposes KrakenD as a LoadBalancer service on port 31028.
==============================================================

Steps for reproduction of task:

1. Install Keycloak Operator CRDs:
kubectl create namespace keycloak
kubectl apply -f https://raw.githubusercontent.com/keycloak/keycloak-k8s-resources/26.4.5/kubernetes/keycloaks.k8s.keycloak.org-v1.yml
kubectl apply -f https://raw.githubusercontent.com/keycloak/keycloak-k8s-resources/26.4.5/kubernetes/keycloakrealmimports.k8s.keycloak.org-v1.yml
kubectl -n keycloak apply -f https://raw.githubusercontent.com/keycloak/keycloak-k8s-resources/26.4.5/kubernetes/kubernetes.yml

2. Deploy Keycloak PostgreSQL:
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm install kc-postgresql bitnami/postgresql -n keycloak -f keycloak/postgresql/values.yaml --version 18.0.15

3. Create Keycloak Secrets:
# TLS certificate
openssl req -subj '/CN=test.keycloak.org/O=Test Keycloak./C=MT' \
  -addext "subjectAltName=DNS:test.keycloak.org" \
  -newkey rsa:2048 -nodes -keyout keycloak/key.pem -x509 -days 365 -out keycloak/certificate.pem

kubectl create secret tls keycloak-tls-secret -n keycloak \
  --cert keycloak/certificate.pem --key keycloak/key.pem

# Database credentials
kubectl create secret generic keycloak-db-secret -n keycloak \
  --from-literal=username=keycloak

# Keycloak admin and client secrets
kubectl apply -f yamls/keycloak-admin-secret.yaml -n keycloak
kubectl apply -f yamls/kcsecrets.yaml

4. Deploy Keycloak:
kubectl apply -f yamls/keycloak.yaml -n keycloak
kubectl apply -f yamls/keycloak-service.yaml -n keycloak

5. Configure /etc/hosts:
Add the following line to /etc/hosts so that test.keycloak.org resolves to the minikube IP:
192.168.49.2   test.keycloak.org

6. Start minikube tunnel:
minikube tunnel
Keycloak will be accessible at http://192.168.49.2:30080.

7. Configure Keycloak:
In the Keycloak admin console at http://192.168.49.2:30080 by doing the following: 

    1) Create the fnd realm
    2) Create Realm Roles customer and admin
    3) Create hte custid User Profile Attribute 
    4) Create the fnd client
    5) configure the custid attribute mapper 
    6) Create test users for admin and customer 

    Specifics for the above steps are found in 03_AccessControl_Robustness.pdf

8. Build and push the app:
cd codebase (it is in the same directory as task3a/ as it shares codebase with later task3b)
mvn clean compile test package
cd docker
docker build -t davegalea/foodndeliv:task3a .
docker push davegalea/foodndeliv:task3a
kubectl rollout restart deployment/fnd-task3

Note: for the first build, in task3/codebase/src/main/resources/application.properties,
set spring.jpa.hibernate.ddl-auto=update, so that the database schema can be created.
Afterwards, repeat this process with auto=none. 

9. Build and push KrakenD:
cd task3a/krakend
docker build -t davegalea/foodndeliv-krakend:task3a .
docker push davegalea/foodndeliv-krakend:task3a
kubectl rollout restart deployment/krakend-deployment

10. Verify deployment:
kubectl get pods
kubectl logs deployment/fnd-task3
kubectl logs deployment/krakend-deployment

11. Test with Postman using the tests attached in postman/
All requests go through KrakenD at http://192.168.49.2:31028, except the openid configuration which goes directly to Keycloak at http://192.168.49.2:30080.
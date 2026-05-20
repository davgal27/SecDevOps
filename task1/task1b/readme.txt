Task 1b: Deploy two FoodNDeliv API versions:
- fnd-rw: original read-write API connected to the CNPG read-write database service.
- fnd-ro: read-only API accepting only GET requests and connected to the CNPG read-only database service.

Files:
- app-rw/
  Maven project for the read-write API version.

- app-ro/
  Maven project for the read-only API version.

- yamls/db-secret.yaml
  Creates database connection credentials for both API deployments.

- yamls/rw-deployment.yaml
  Deploys the read-write API using image davegalea/foodndeliv-rw:task1b and DB_HOST=fnd-db-rw.

- yamls/ro-deployment.yaml
  Deploys the read-only API using image davegalea/foodndeliv-ro:task1b and DB_HOST=fnd-db-ro.

- yamls/rw-service.yaml
  Exposes the read-write API as service fnd-rw.

- yamls/ro-service.yaml
  Exposes the read-only API as service fnd-ro.

- postman/ 
  Contains the test screenshots and text files for test reproduction

1. Build both Maven projects:
    mvn clean compile test package
   
    Note: for the first build of the Read-Write version, in task1/task1b/app-rw/src/main/resources/application.properties, set spring.jpa.hibernate.ddl-auto=update, so that the database schema can be created.

2. Build and push Docker images:
    docker build -f docker/Dockerfile -t davegalea/foodndeliv-rw:task1b ./app-rw
    docker push davegalea/foodndeliv-rw:task1b

    docker build -f docker/Dockerfile -t davegalea/foodndeliv-ro:task1b ./app-ro
    docker push davegalea/foodndeliv-ro:task1b

3. Deployment:
    kubectl apply -f yamls/db-secret.yaml
    kubectl apply -f yamls/rw-deployment.yaml
    kubectl apply -f yamls/ro-deployment.yaml
    kubectl apply -f yamls/rw-service.yaml
    kubectl apply -f yamls/ro-service.yaml

4. Rebuild Read-Write app:
    mvn clean compile test package

    Note: this time, set in task1/task1b/app-rw/src/main/resources/application.properties, set spring.jpa.hibernate.ddl-auto=none, so that the existing schema remain.

    The same steps apply regarding building and pushing the docker image for read-write.

5. Re-Deploy Read-Write:
    kubectl rollout restart deployment/fnd-rw

6. Start port-forwarding:
    Read only: kubectl port-forward svc/fnd-ro 8080:8080
    Read Write: kubectl port-forward svc/fnd-rw 8081:8080

7. Test with Postman, using the tests described in postman/tests.txt:
- Test read-only API using http://localhost:8080
- Test read-write API using http://localhost:8081

Verification:
    kubectl get pods
    kubectl describe deployment fnd-rw
    kubectl describe deployment fnd-ro
    kubectl logs deployment/fnd-rw --tail=100
    kubectl logs deployment/fnd-ro --tail=100
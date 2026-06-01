Task 3b:
 
This task updates the FoodNDeliv API (fnd-task3) with the following:
 
  i)   Java Bean Validation-based structural invariants for entity fields
  ii)  Server-side order total calculation
  iii) Domain invariants preventing closure of restaurants / deactivation
       of customers with pending orders
  iv)  RBAC: Only admins can manage restaurants
  v)   ABAC: Only account owners can place orders / deactivate their accounts
 
The KrakenD gateway (krakend.json) is also updated to enforce RBAC/ABAC
at the gateway level 
 
Files:
- ../codebase/
    Updated API version of the Maven project for the FoodNDeliv API backend.
 
- ../task3a/krakend/krakend.json
    Updated KrakenD configuration adding the following endpoints:
    - PUT /api/ctrl/restaurants/{id}     
    - POST /api/ctrl/orders             
    - PUT /api/ctrl/customers/{id}       
 
- postman/
    Contains the Postman test screenshots for testing of the reproduced solution.
 
 - terminal-dump.txt
  Contains the full raw terminal dump outputted while performing this task 
 


==========================================================================
Steps

 
1. Open the codebase files with the changes described above.
 
2. Set ddl-auto=update for first deploy (to add total_price column):
   In task3/codebase/src/main/resources/application.properties:
       spring.jpa.hibernate.ddl-auto=update
 
3. Build the app:
   cd task3/codebase
   mvn clean compile test package
 
4. Build and push Docker image:
   cd docker
   docker build --no-cache -t davegalea/foodndeliv:<tag> -f Dockerfile ..
   docker push davegalea/foodndeliv:<tag>
 
5. Deploy the updated app:
   kubectl set image deployment/fnd-task3 foodndeliv=davegalea/foodndeliv:<tag>
   kubectl rollout restart deployment/fnd-task3
 
6. Set ddl-auto=none and redeploy:
   In task3/codebase/src/main/resources/application.properties:
       spring.jpa.hibernate.ddl-auto=none
   Repeat steps 3-5
 
7. Update and redeploy KrakenD with the new endpoints:
   cd task3a/krakend
   docker build --no-cache -t davegalea/foodndeliv-krakend:task3b .
   docker push davegalea/foodndeliv-krakend:task3b
   kubectl set image deployment/krakend-deployment krakend=davegalea/foodndeliv-krakend:task3b
   kubectl rollout restart deployment/krakend-deployment
 
8. Verify deployment:
   kubectl get pods
   kubectl get events
   kubectl logs deployment/fnd-task3
   kubectl logs deployment/krakend-deployment

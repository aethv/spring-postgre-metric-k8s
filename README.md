Spring Boot + PostgreSQL

### Steps:
* Start services
```bash
docker run -d --name postgres \
  -p 5432:5432 \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=moviesdb \
  postgres:16.1
  
docker run -d --name redis -p 6379:6379 redis:7.2.0
 
```

* Start App
```bash
./mvnw clean spring-boot:run 
```

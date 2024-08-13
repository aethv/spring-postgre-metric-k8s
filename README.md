Spring Boot + PostgreSQL

### Steps:
* Start PostgreSQL
```bash
docker run -d --name postgres \
  -p 5432:5432 \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=moviesdb \
  postgres:16.1 
```

* Start App
```bash
./mvnw clean spring-boot:run 
```

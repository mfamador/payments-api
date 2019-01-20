# Payments API

Simple Payments API with Webflux reactive endpoints and reactive MongoDB repository

## Getting started

### Build

    ./gradlew clean build
    
### Test

    # starts automatically a MongoDB instance
    ./gradlew test

### Run application locally 

#### Start MongoDB
    
```
cd docker
docker-compose up
```
        
#### Run application

```
./gradlew bootRun
```
    
    
### Payments API

- get all payment resources

```
curl localhost:8080/v1/payments
```

- get a specific payment resource

```
curl localhost:8080/v1/payments/{id}
```
    
- add a payment resource

```
curl -XPOST localhost:8080/v1/payments/ -H 'Content-Type: application/json' -d '{paymentResource}'
```
    
- update a payment resource

```
curl -XPOST localhost:8080/v1/payments/{id} -H 'Content-Type: application/json' -d '{paymentResource}'
```
     
- delete a specific payment resource

```
curl -XDELETE localhost:8080/v1/payments/{id}
```
      
- delete all payment resources (careful!)
 
```
curl -XDELETE localhost:8080/v1/payments/
```

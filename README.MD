# Spring-boot RabbitMQ Example

## Overview

This is a simple example of how to use RabbitMQ with Spring Boot. The example demonstrates how to send and receive
messages using RabbitMQ in a Spring Boot application. If you are new to RabbitMQ, you can read more about it [here](RabbitMQ.md)

<p align="center" style="padding:10px;">
  <reportTable style="width: 80%;">
    <tr>
    <td><img src="assets/spring-icon.png" style="margin-bottom:10px;"  width="150px" alt="s1"></td>
    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td><img src="assets/rabbitmq-icon.png" alt="s2" width="300px"></td>
    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td><img src="assets/postgresql-icon-.png" alt="s2" width="150px"></td>
    </tr>
  </reportTable>
</p>

## Use case

Simple e-shop that has two microservices one for the Inventory service and the other for the order service. The
inventory service is managing all the products in the e-shop while the order service to create an order when a product
is purchased and the inventory service should be notified to decrease/increase the product quantity depends on the order
status.
While creating an order the order service should call the inventory service to decrease the product quantity but the
order service doesn't need to wait for the inventory service to finish the operation, so we need to use the message
broker to send the message to the inventory service to decrease/increase the product quantity depends on the order
status.

### Solution

The solution is to use a message broker **RabbitMQ** to send messages between the order service and the inventory
service. The order service will send a message to the inventory service to decrease/increase the product quantity
depends on the order status.

## Prerequisites

Everyone will need:

- Basic knowledge of Java, and Spring boot (Basics).

- [JDK 17 or higher](https://openjdk.java.net/install/index.html) installed. **Ensure you have a JDK installed and not
  just a JRE**
- [docker](https://docs.docker.com/install/) installed.
- [maven](https://maven.apache.org/install.html) installed.

## Step 1: Set Up the Development Environment

- **Install Java:** Ensure you have Java 17 or later installed on your system.
- **Install Maven:** Install Apache Maven as your build tool. Download it from
  the [Apache Maven website](https://maven.apache.org/download.cgi).
- **Install Docker Desktop:** Install Docker desktop as your container platform. Download it from
  the [Apache Maven website](https://www.docker.com/products/docker-desktop/).

## Step 2: Run the application

1. **Start docker-compose.yml:**

   ```bash
    > docker-compose up      
   ```
   ```yml
      services:
      ## rabbitMQ
       rabbitmq:
         container_name: rabbitmq-eshop
         image: rabbitmq:3.9.11-management-alpine
         ports:
           - "5672:5672"
           - "15672:15672"
         networks:
           - rabbitmq
       ## postgres
       postgres:
         container_name: products
         image: postgres
         environment:
           POSTGRES_USER: Celfocus
           POSTGRES_PASSWORD: Celfocus
           PGDATA: /data/postgres
         volumes:
           - postgres:/data/postgres
         ports:
           - "5432:5432"
         networks:
           - postgres
         restart: unless-stopped
      
      networks:
       rabbitmq:
        driver: bridge
       postgres:
        driver: bridge

      volumes:
       postgres: 
   ```
    Don't forget to configure your database username and password and should be matched with the ones in application.yml files in the services.
2. **Run Inventory service:**

   ```bash
    > cd inventory
    > mvn spring-boot:run      
   ```
   
    After starting the inventory service, some products will be created in the database. To check them can do the following:
    
   ```bash
    > docker exec -it products bash ## open the bash shell in the container
    > psql -U Celfocus ## ope postgres shell 
    > \c Celfocus ## connect to the database
    > select * from products; ## list all the products
   ``` 
3. **Run Order service:**

   ```bash
    > cd order
    > mvn spring-boot:run      
   ```

## Step 3: Test the application

Use tools like postman or curl to make POST requests to create an order and observe the logs in the console.

### Steps 

#### Step 1: Do a POST request to create an order
    
This will create a new order with order items and the order service will send a message to the inventory service to identify that the order is **CREATED** then the inventory will decrease the product quantity.

   ```cURL
    curl --location 'http://localhost:8090/api/v1/orders' \
    --header 'Content-Type: application/json' \
    --data '{
        "total": "3075$",
        "address": "123 Dublin",
        "orderItems": [
            {
                "name":"Iphone 15 pro max",
                "quantity": 5,
                "price": "1050$",
                "productId": 1
            },
            {
                "name":"Raspberry pi 5",
                "quantity": 5,
                "price": "450$",
                "productId": 2
            }
        ]
    }'
   ```
#### Step 2: Check if the products quantity is decreased
From docker shell [Step of running the inventory service], run the sql command to check the products' quantity.
    
   ```bash
    > select * from products; ## list all the products
   ```
The query should return the products with the decreased quantity.

#### Step 3: Cancel the order
Do a PATCH request to cancel the order, this will send a message to the inventory service with **CANCELLED** status to increase the product quantity.

   ```cURL
    curl --location --request PATCH 'http://localhost:8090/api/v1/orders/1' \
    --header 'Content-Type: application/json' \
    --data '{
        "id": 1,
        "total": "3075$",
        "address": "123 Dublin",
        "status": "CANCELLED",
        "orderItems": [
            {
                "id": 1,
                "name": "Iphone 15 pro max",
                "quantity": 5,
                "price": "1050$",
                "productId": 1
            },
            {
                "id": 2,
                "name": "Raspberry pi 5",
                "quantity": 5,
                "price": "450$",
                "productId": 2
            }
        ]
    }'
   ```  
After running the same query again, you will find that the products' quantity increased again.

### Postman Collection [here](./rabbitMQ-example.postman_collection.json)

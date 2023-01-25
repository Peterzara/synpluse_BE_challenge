# Synpluse Code Challenge

## Introduction
This project aims to design and implement a reusable REST API for an e-Banking Portal. The API is responsible for returning a paginated list of money account transactions for a given customer who is logged-on to the portal. The transactions are consumed from a Kafka topic and are filtered by the calendar month specified by the customer. Additionally, the API also returns the total credit and debit values at the current exchange rate, obtained from a third-party provider. The API has been developed using Spring Boot and packaged as a Docker image for ease of deployment to Kubernetes environments.

![](image/E-R diagram.png "ER Diagram")

## Getting Started
1. First, make sure you have JDK 17, the latest Kafka and Docker Desktop installed on your machine, with the Kubernetes feature enabled.

2. Next, clone the project repository to your local machine.

3. Go to your kafka root directory, launch zookeeper, Kafka server, and Kafka listener using the following commands in separate terminals:
```jshelllanguage
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties
bin/kafka-console-consumer.sh --topic transactions --from-beginning --bootstrap-server localhost:9092
```
4. Open the project in Intellij using maven to download needed dependencies 

5. Go to src/main/resources/application.properties, update your external exchange-rate API password, you can register a free account in: https://apilayer.com/marketplace/exchangerates_data-api

6. Run _TechChallengeApplication_ class

## Endpoints
The e-Banking API offers two main endpoints:
### /auth/register
This endpoint is a POST method that is used for registering a customer with the system. It requires a request body containing the customer's ID and a default password:

```JSON
{
    "customerId": "CUST31098",
    "password": "password"
}
```
(you can copy customerId from the terminal that runs Kafka listener)

The endpoint returns a JSON Web Token (JWT) that can be used for authentication in subsequent requests.

### /customers/{customerId}/transactions?month=&page=
This endpoint is a GET method that is used for retrieving a paginated list of transactions for a specific customer. It requires a customer ID to be passed in the path and accepts query parameters such as month and pageNumber. The endpoint returns a JSON body containing a list of transactions, total credit, and total debit.

In order to use this endpoint, you will need to provide the JWT token obtained from the /auth/register in the headers of each request like this:
```jshelllanguage
Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDVVNUMzEwOTgiLCJpYXQiOjE2NzQ1NjEyOTQsImV4cCI6MTY3NDU2MjczNH0.wiPiR6seaJeO1qbcE7QzqJ93FNOB7XzNqycsyrOAXhM
```
Please note that for the GET endpoint, the month query parameter is mandatory, and pageNumber is optional, by default is 1.

## Build and Deployment
> While the app can run locally, it fails to host on k8s

Building and deploying the e-Banking API can be done using Docker and Kubernetes (K8s).

To build the API, a Dockerfile has been provided in the project. This file contains instructions on how to package the Spring Boot application as a Docker image. To build the image, run the following command:

```
 docker build -t springio/gs-spring-boot-docker .
```
To deploy the API to a K8s cluster, a `spring-boot-app.yml` and `kafka-zkp.yml` have been provided. The spring-boot-app.yml file contains instructions on how to create a K8s deployment and service for the API. The kafka-zkp.yml file contains instructions on how to create a Kafka cluster and zookeeper cluster.

To deploy the Kafka and zookeeper, run the following commands:

```jshelllanguage
kubectl create -f namespace.yml
kubectl create -f zookeeper.yml
kubectl create -f broker.yml
```

To deploy the API, run the following command:
```jshelllanguage
kubectl create -f spring-boot-app.yml
```

Once the deployment is complete, you can check the status of the pods and services.

## Testing
This project contains 2 unit testing:
- KafKaTransactionGeneratorUnitTest
- CustomerControllerUnitTest

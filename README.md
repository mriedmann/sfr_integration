# Assignment 2: Integration

## 1 Usecase

You are working for a bank and more and more Customer-Facing Services need
access to the data you generate and process in your core-banking system. The
idea is, to introduce an asynchronous EventStream that works in a non-blocking
manner. The decision was made to create multiple prototypes to compare the
open source products on the market. You were selected to create the prototype
based on Kafka.
The scenario is pretty simple. A core-banking service publishes all the topics
that are relevant for your use-case. Two services consume those topics and
provide some data to a customer (UI is not relevant for this exercise). In addition
a money-laundering service processes a topic and adds additional events.

## 2 Tasks

Fulfill the following goals for the given task at hand:

* Setup Kafka and the core-banking publisher service to generate topics
* Create the consumer services that subscribe to the topics
* Create the money-laundering service that processes one topic

## 3 Solution

To solve the given tasks 4 services were implemented. Java 11 with SpringBoot 
and "Spring Cloud Stream With Kafka" is used to realize the project. Every service
is runtime-coupled only to the kafka-broker. There is also only one compile-time dependency, 
sharing the message-objects to avoid possible errors during serialization and deserialization.
This dependency is not needed and could be resolved at any time by replacing the classes
with similar implementations. To avoid the need for an own persistence layer, kafka was directly 
used to store all information. All stateful services (core, balance, customer) are reading all
needed topics from start on boot-up. Topic-Listeners are then used to build the 
necassery data-structures. These structures are kept up-to-date using the exact same 
mechanism. This reduces code complexity and makes it easy to scale-out to multiple instances
for each service. The only downside is that the boot-up-time and memory consumption grows relative 
to the number of messages in the topics. This could be solved by using different partitions 
or/and a compacting/cleanup strategy but this is assumed "out of scope" for assignment.        

All services are preconfigured to use a local kafka instance. To start kafka the official
quick-start guide can be used (https://kafka.apache.org/quickstart). Each service listens to
its own port, therefor following tcp ports should be available to start the services: 
`8080`,`8081`,`8082`,`8083`.

To simulate actual workload, please find an automated integration-test-suite implemented in python
under ./test.py. To run it start kafka, start each service and run the test-suite with 
`python ./tests.py`. This generated 5 test customers, submits 5 to 25 transactions with one out of
3 different currencies and an amount between -100,000 and +100,000. It also validates all 
other endpoints and verifies that the example money-laundering rule (`amount > 80000 or amount < -80000` is 
assumed to be money-laundering) freezes the account of the issuing customer within less then 1 second.

#### Data

This sub-project is used to contain essential shared objects like `Customer`, `Transaction` and `MoneyLaunderingAlert`. 

#### CoreBankingApplication

The core-banking-service is the primary message-producer of this system and exposes 2 rest-api-endpoints:
`:8080 POST /customers` and `:8080 POST /transactions`. Both are used to submit events to the system. 

The customers-endpoint accepts unique customers with 3 attributes (`firstName`,`lastName`,`address`).
If an duplicate is submitted the service returns `400 Bad Request`. On success a extended customer-object
in json is returned. This contains the current frozen-state (bool) and the customer-id (uuid).

The transactions-endpoint accept transaction from customers and uses 3 attributes 
(`customerId`,`currency`,`amount`) where customer-id is the id provided in the subsequent 
customer-create-response. Currency has to be a 3 letter currency code (ISO 4217) and amount is defined
as decimal-number that follows the BigDecimal specification of Java (https://docs.oracle.com/javase/8/docs/api/java/math/BigDecimal.html).
If the customer-id does not refer to an existing customer `400 Bad Request` is returned. Also if
the customer-id belongs to an frozen account, the request is rejected and `403 Forbidden` is
returned. 

#### Balance

The balance-service implements simple balance reports for a given customer-id. To make use of the
different currencies, one sum per currency is generated. The endpoint can be reached under `:8081 GET /balances/<customer-id>`.
Another endpoint is available at `:8081 GET /balances` and lists all available balances. No pagination is implemented
so expect very long requests for big test-data-sets.

#### Customer

The customer-service can be used to access detailed customer information. The core-service is only responsible for the
validation and routing of requests. There are no sensitive user-data saved to preserve a "single point of responsibility".
This helps fulfilling possible data-protection regulations amd minimizes possible data-coupling.

To use this service call `:8082 GET /customers/<customer-id>` with a known customer-id. There is also a special id to
get the latest user (`:8082 GET /customers/latest`) in case there is no valid id available.

#### Money Laundering

To simulate a stateless, processing-only service an "money laundering protection" service is implemented. These uses 
a simple Rule Design Pattern (https://www.michael-whelan.net/rules-design-pattern/) to make adding new rules as easy
as possible. The current rule is very simple but provides a good showcase for the given task.

Because of the special nature of this service, the topic-listener is implemented as simple pipeline where transactions 
are fed as arguments and possible alerts are returned from the function and therefor submitted to the `moneylaundering` 
topic. Also the listener does not read from the beginning on each start, rather then using consumer-offsets to 
save the progress and not recompute already processed transactions. This makes this service very efficient and 
easy to use and scale. There are no special http-endpoints, only the default spring-boot-web implementation to make
monitoring and health-checking of this service easier. 

## Useful reading

* https://kafka.apache.org/quickstart
* https://dzone.com/articles/spring-cloud-stream-with-kafka
* https://www.baeldung.com/java-maps-streams
* https://www.baeldung.com/spring-boot-change-port
* https://start.spring.io/
* https://docs.gradle.org/current/userguide/multi_project_builds.html
  
### Purpose (craft-demo : Driver Onboarding Module)  ###
- This system is built as as a demoable module for a riding management 'Uber' like application. The focus here is on the onboarding module. 

### Modules & Inter-depdendencies ###

- model : Holds POJOs related to request/response and so forth.
- core : Holds core non-api module functionalities e.g. connection pool, rest client utils, repositories, events, etc.
- api : (Depends on core & model) Contains service classes with the implementation and logic.
- web : (Depends on api) Exposes web controllers and holds rest web related stuff.

### Rest-API Documentaion ###
- Details available under swagger doc as follows.
- Uri : http://{host}:{port}/{context-path}/swagger-ui/
- Sample : http://localhost:8045/driver-onboarding/swagger-ui/

### Tech-Stack + Patterns ###
- Database : MySQL + Blob-Store(Document verification)
- Async Messaging : Pub/Sub pattern via Apache Kafka
- Swagger : API Documentation
- Change-Listeners : Observer Handler pattern using Kafka-consumers (+ Should have database change listeners for state change : Not implemented)
- Principles/Patterns : SOLID + Choreography SAGA + Observer - Listeners + Handlers

### Components (Only which can be demoable in 3 hours dev time-period) ###
- Services : IUserService, IDriverOnboarding, IDriverDocService
- Message-Handlers : BgVerificationHandler, TrackingDeviceShipmentHandler, etc.
- Entity : User, DriverInfo {mysql-relational DB}
- Models : UserProfileInfo, UserLoginRequest, DriverState (enum for plug n play state change) 
- Events : Kafka
- Controller : RestAccountsAPI, RestDriverAPI
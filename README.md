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
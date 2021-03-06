# Trovimap Proof of Concept

The repository outlines some of the best practices for building applications in scala. It shows examples of how to test components, outlines the onion architecture, and outlines some of the tools you may wish to adapt in order to write scala software that encourage readilbility and robustness.

The code is structured as follows
  - **Domain**- the core business model
  - **API**- The business controller of the domain
  - **Infrastructure**- The implementation details and external dependencies for the application. For ex. Elasticsearch, Postgres, or Akka-http
 
In order to run the app, you will need a running instance of both Postgres and Elasticsearch. The configuration details are both stored in ```resources/enviroment.conf```. You will need to change accordingly to point to your respective instances.

### Bootstrapping the ElasticSearch index
After adjusting the configuration as outlined above, run the following command to add properties in the elasticsearch index.
```bash
sbt "run-main com.trovimap.infrastructure.elasticsearch.Bootstrap"
```

### Running Postgres Migrations
Before running the application, you will need to run all the requisite migrations. I am using [Flyway](https://flywaydb.org)
```bash
# Pass in the correct values for the POSTGRES_DATABASE_NAME, flyway.url, flyway.user, flyway.password first
sbt -DPOSTGRES_DATABASE_NAME=postgres -Dflyway.url="jdbc:postgresql://localhost:5432/postgres" -Dflyway.user=postgres -Dflyway.password=postgres flywayMigrate
```

### Running the Trovimap POC Application
```bash
sbt "run-main com.trovimap.infrastructure.http.Trovimap"
```

### Running the Trovimap POC Application in a Dev/Prod Setting
```bash
sbt assembly
# The above command will produce a jar in the target/scala-2.11/ folder (For example trovimap-poc-assembly-1.0.jar)
# Run the java executable and you are good to go
java -cp trovimap-poc-assembly-1.0.jar com.trovimap.infrastructure.http.Trovimap
```

### Rest API
```sh
# Get Property By Id
curl -X GET http://localhost:9000/properties

# Create Property
curl -X POST http://localhost:9000/properties -d '{
	"brokerId": "1",
	"id": "2",
	"location": {
		"latitude": 23232,
		"longitude": 5445
	},
	"numberOfBathrooms": 3,
	"numberOfBedrooms": 6,
	"price": {
		"cents": 23232,
		"currency": "USD"
	},
	"title": "test property"
}'

# Search Properties
# Url needs to be encoded
curl -X GET http://localhost:9000/properties?filter[numberOfBathrooms]=3
```

### Testing

Tests are broken down into 2 sections: 1) Integration tests and 2) Unit tests.
```sh
# Unit tests
sbt test

# Integration tests
sbt "it-test"
```
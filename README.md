# Simple REST IMDB java backend

## Requirements

In order to run this example you will need the following:

1. Java 8 (or newer), you can use any implementation:
    * If you have installed Java, you can check the version by typing the following in a command line:
        
        ```
        java -version
        ```

2. Maven 3.2.1 (or newer):
    * If you have installed Maven, you can check the version by typing the following in a command line:
        
        ```
        mvn -version
        ```

## Usage

The example uses maven to build and run the microservices.

1. Build the sample using maven:

    ```bash
    $ mvn clean package
    ```

2. Run the sample:
* Uber-jar:

    ```bash
    $ java -jar target/${project.build.finalName}.jar
    ```
    
    in Windows environemnt use the command
    ```batch
    java -jar target/${project.build.finalName}.jar
    ```
    
The application/service can be accessed on the following URL:
* JAX-RS REST resource page 
- http://localhost:8080/v1/movies
- http://localhost:8080/v1/movies/search?searchText=movie
- http://localhost:8080/v1/movies?limit=&{limit}&offset=&{offset}
- http://localhost:8080/v1/movies/&{imdbId}
- http://localhost:8080/v1/actors
- http://localhost:8080/v1/actors?limit=&{limit}&offset=&{offset}
- http://localhost:8080/v1/actors/&{actorId}

Sample Movie object:
```json
{
        "imdb_id": 1,
        "title": "movie X",
        "description": "Great movie",
        "year": 2012,
        "movieActors": [
            2,
            3,
            4
        ]
    }
```

Sample Actor object:

```json
{
        "id": 1,
        "firstName": "John",
        "lastName": "Novak",
        "born": "1991-10-10",
        "actorMovies": [
            9,
            8,
            10
        ]
    }
```
							

To shut down the example simply stop the processes in the foreground.

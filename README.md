# Quote demo backend

This repository contains a demo application for storing and calculation of average price.
Technologies used: Spring webflux


### Running the Reactor Netty server
 - Build using 'maven package'
 - Run the `java -jar target/quotedemo-backend-1.0-SNAPSHOT.jar'
 
### Running the Redis server (for storing quote)
-- Run 'redis-server' 

### Sample curl commands

Instead of running the client, here are some sample `curl` commands that  call the services
by this example:

```sh
curl -v 'http://localhost:8080/quote'
curl -v 'http://localhost:8080/quote/average/2'
curl -d '{"symbol":"0001.HK","price":94.45,"timestamp":80905000000005}' -H 'Content-Type: application/json' -v 'http://localhost:8080/quote'
```



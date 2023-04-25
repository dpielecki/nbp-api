## Simple service using NBP API

### Running the server

In order to run the application either build docker image locally by running
`docker build -t IMAGE_NAME .`
followed by
`docker run -p 8080:8080 IMAGE_NAME`
or pull the image directly from [Dockerhub](https://hub.docker.com/) by running 
`docker run -p 8080:8080 dpielecki/nbp-api`.
Doing so will start a container exposing the application at localhost:8080.

### Accessing the application

The easiest way to access the API is to go to [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) and use the frontend provided by [Swagger](https://swagger.io/). From there simply pick one of the endpoints, press on the **Try it out** button, input the parameters and execute the request.

### Available operations

1. Given a date (formatted YYYY-MM-DD) and a currency code (list: https://nbp.pl/en/statistic-and-financial-reporting/rates/table-a/), provide its average exchange rate.
   - endpoint: `/exchangerates/average/{currency}/{date}`
   - example input:
     - currency: **EUR**
     - date: **2020-10-12**
   - expected output: **4.48**
2. Given a currency code and the number of last quotations N (N <= 255), provide the max and min average value (every day has a different average).
   - endpoint: `/exchangerates/extremes/{currency}/{quotations}`   
   - example input:
     - currency: **EUR**
     - quotations: **100**
   - expected output: 
```
{
  "minAverageValue": 4.6,
  "maxAverageValue": 4.79
}
```
3. Given a currency code and the number of last quotations N (N <= 255), provide the major difference between the buy and ask rate (every day has different rates).
   - endpoint: `/exchangerates/majordifference/{currency}/{quotations}`
   - example input:
     - currency: **EUR**
     - quotations: **100**
   - expected output: **0.10**
## The system flows

The system is performing 2 flows:

1. It receives messages from the Websocket, convert them to entities and post them to the database.
2. A GET REST endpoint is exposed to generate the price history as Candlesticks.

2 functionalities are performed in a single application for simplicity. In a real life system, I would split the system
into two separate microservices.

## Starting the application

In order to successfully start the application, the websockets and database must be available. The simulated websockets and database can be started with the following terminal command, which is called from the repository root directory.

`docker compose up -d
`

They can be shut down using the command:

`docker compose down
`

Once the dependencies are up and running, the application can be started with:

```mvn spring-boot:run```

## Flow 1. Incoming data handling

The data received through websockets messages is saved into a Mongo Database.

### DB Entities

#### DB Entities

Type | Description
--- | ---
Instrument | Entity used to persist Instrument details received through the /instruments Websocket.
Quote | Entity used to persist Instrument details received through the /quotes Websocket. If in the database there is no Instrument with the ISIN received in the message, the Quote is not saved to the database.

#### Incoming Instrument messages attributes

Messages related to Instruments may contain one of 2 flag attributes:

* ADD - the instrument with the given ISIN will be saved to the database.
* DELETE - the instrument with the given ISIN will be removed from the database, as well as all Quotes with the same
  ISIN.

## Flow 2. REST endpoint

Once the server is running you can check the results at
```
http://localhost:9000/candlesticks?isin={ISIN}
```
in this case a candlestick for the precedent 30 minutes will be generated.

The endpoint supports also a few more nullable query parameters:

Parameter | Description
--- | ---
length| the time length of the candlesticks expected in the output
from| starting time point for generating the candlesticks
to| ending time limit for generating the candlesticks

The parameters `from` and `to` accept values in the following format, before being URL encoded:

```yyyy-MM-dd-THH:mm:ss.zzz```

for example: 

```2017-01-13T17:09:42.411```

#### Valid input:
* the `from` value must be before the value passed as `to`
* the `length` value must be positive and bigger than the difference between `to` and `from`.
* the `isin` must be non null an d non blank

For example the following request will generate:
* a list of candles(with the 'size' equals to value under length parameter)
* the candles will be generated only for the time interval between the values `from` ... `to`

#### Request example

```localhost:9000/candlesticks?isin=PM2853Q71886&length=30&from=2021-10-12T12%3A55%3A49&to=2021-10-18T12%3A55%3A49```

#### Output example
```[
    {
        "openTimestamp": "2021-10-17T12:25:49",
        "closeTimestamp": "2021-10-17T12:55:49",
        "openPrice": 1411.7708,
        "highPrice": 1434.6562,
        "lowPrice": 1276.9688,
        "closingPrice": 1300.6771
    },
    {
        "openTimestamp": "2021-10-17T12:55:49",
        "closeTimestamp": "2021-10-17T13:25:49",
        "openPrice": 1300.6771,
        "highPrice": 1300.6771,
        "lowPrice": 990.25,
        "closingPrice": 990.25
    },
    
    ....
    
    {
        "openTimestamp": "2021-10-18T11:25:49",
        "closeTimestamp": "2021-10-18T11:55:49",
        "openPrice": 1099.6277,
        "highPrice": 1099.6277,
        "lowPrice": 1099.6277,
        "closingPrice": 1099.6277
    },
    {
        "openTimestamp": "2021-10-18T11:55:49",
        "closeTimestamp": "2021-10-18T12:25:49",
        "openPrice": 1099.6277,
        "highPrice": 1099.6277,
        "lowPrice": 1099.6277,
        "closingPrice": 1099.6277
    },
    {
        "openTimestamp": "2021-10-18T12:25:49",
        "closeTimestamp": "2021-10-18T12:55:49",
        "openPrice": 1099.6277,
        "highPrice": 1454.5938,
        "lowPrice": 1099.6277,
        "closingPrice": 1454.5938
    }
]

   ```

## Running the application
Once the application is running it will start listening to messages, and post data to the mongo database.
For a better user experience / and manual testing Mongo DB Compass can be used: it will require the following connection string to connect to the Mongo database running on  Docker:

```mongodb://root:rootpassword@localhost:27017/?authSource=admin&readPreference=primary&appname=MongoDB%20Compass&directConnection=true&ssl=false```


## System requirements

* Java 11+
* Docker


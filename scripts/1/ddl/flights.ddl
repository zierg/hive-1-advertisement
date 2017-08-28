drop table if exists flights_temp;
drop table if exists flights;

CREATE TABLE IF NOT EXISTS flights_temp
(
    Year int,
    Month int,
    DayofMonth int,
    DayOfWeek int,
    DepTime int,
    CRSDepTime int,
    ArrTime int,
    CRSArrTime int,
    UniqueCarrier string,
    FlightNum int,
    TailNum string,
    ActualElapsedTime int,
    CRSElapsedTime int,
    AirTime int,
    ArrDelay int,
    DepDelay int,
    Origin string,
    Dest string,
    Distance int,
    TaxiIn int,
    TaxiOut int,
    Cancelled int,
    CancellationCode string,
    Diverted int,
    CarrierDelay int,
    WeatherDelay int,
    NASDelay int,
    SecurityDelay int,
    LateAircraftDelay int
)
row format delimited fields terminated by ","
STORED AS TEXTFILE
tblproperties("skip.header.line.count"="1");
load data local inpath '/root/homework/hive/2007.csv' OVERWRITE INTO TABLE flights_temp;
create table flights stored as parquet as select * from flights_temp;

drop table flights_temp;
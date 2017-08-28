drop table if exists airports_temp;
drop table if exists airports;

CREATE TABLE IF NOT EXISTS airports_temp
(
    iata string,
    airport string,
    city string,
    state string,
    country string,
    lat double,
    long double
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
WITH SERDEPROPERTIES (
   "separatorChar" = ",",
   "quoteChar"     = "\"",
   "escapeChar"    = "\\"
)
STORED AS TEXTFILE
tblproperties("skip.header.line.count"="1");
load data local inpath '/root/homework/hive/airports.csv' OVERWRITE INTO TABLE airports_temp;
create table airports stored as parquet as select * from airports_temp;

drop table airports_temp;
drop table if exists carriers_temp;
drop table if exists carriers;

CREATE TABLE IF NOT EXISTS carriers_temp
(
    code string,
    description string
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
WITH SERDEPROPERTIES (
   "separatorChar" = ",",
   "quoteChar"     = "\"",
   "escapeChar"    = "\\"
)
STORED AS TEXTFILE
tblproperties("skip.header.line.count"="1");
load data local inpath '/root/homework/hive/carriers.csv' OVERWRITE INTO TABLE carriers_temp;
create table carriers stored as parquet as select * from carriers_temp;

drop table carriers_temp;
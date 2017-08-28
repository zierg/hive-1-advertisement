drop table if exists ads_temp;
drop table if exists ads;

CREATE TABLE ads_temp
(
    Bid_ID string,
    Timestamp bigint,
    Log_Type string,
    i_Pin_You_ID string,
    User_Agent string,
    IP string,
    Region_ID int,
    City_ID int,
    Ad_Exchange 2,
    Domain string,
    URL string,
    Anonymous_URL string,
    Ad_Slot_ID string,
    Ad_Slot_Width int,
    Ad_Slot_Height int,
    Ad_Slot_Visibility string,
    Ad_Slot_Format string,
    Ad_Slot_Floor_Price int,
    Creative_ID string,
    Bidding_Price int,
    Paying_Price int,
    Landing_Page_URL string, 
    Advertiser_ID int,
    User_Profile_IDs string
)

row format delimited fields terminated by "\t"
STORED AS TEXTFILE;
load data local inpath '/root/homework/hive/advertisement/records' OVERWRITE INTO TABLE ads_temp;
create table ads as orc as select * from ads_temp;
drop table ads_temp;


drop table if exists cities_temp;
drop table if exists cities;

CREATE TABLE cities_temp
(
    City_ID int,
    City_name string
)

row format delimited fields terminated by "\t"
STORED AS TEXTFILE;
load data local inpath '/root/homework/hive/advertisement/cities.en.txt' OVERWRITE INTO TABLE cities_temp;
create table cities as orc as select * from cities_temp;
drop table cities_temp;
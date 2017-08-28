1)  
-- CSV. MR, No Indexes: 37.74
-- CSV. MR, Indexes: 36.949
-- CSV. Tez: 12.147
-- ORC. Tez: 9.025
-- Parquet. Tez: 13.714
select UniqueCarrier, count(1) from flights group by UniqueCarrier;
    
2) 
-- CSV. MR, No Indexes: 20.88
-- CSV. MR, Indexes: 19.6
-- CSV. Tez: 14.869
-- ORC. Tez: 8.451
-- Parquet. Tez: 11.542
with ny_airports as (
    select
        iata
    from
        airports
    where
        state = 'NY'
)    
select
    count(1) as flights_amount
from
    (
        select
            1
        from
            flights f
        join
            ny_airports ny_o
        on
            f.origin = ny_o.iata
        union all
        select
            1 as flights_amount
        from
            flights f
        join
            ny_airports ny_o
        on
            f.dest = ny_o.iata
    ) t;
-- 707941    

3) 
-- CSV. MR, No Indexes: 121.326
-- CSV. MR, Indexes: 123.612
-- CSV. Tez: 16.031
-- ORC. Tez: 9.23
-- Parquet. Tez: 11.548
with summer_flights as (
    select
        origin
        , dest
    from
        flights
    where
        month >= 6
        and month <= 8
)
select
    airport
    , flights_amount
from
    (
        select
            a.airport, count(1) as flights_amount
        from
            airports a
        join
            summer_flights f
        on
            a.iata = f.origin
        group by a.airport
        union
        select
            a.airport, count(1) as flights_amount
        from
            airports a
        join
            summer_flights f
        on
            a.iata = f.dest
        group by a.airport
    ) t
order by flights_amount desc
limit 5;

4)
-- CSV. MR, No Indexes: 53.399
-- CSV. MR, Indexes: 53.634
-- CSV. Tez: 9.932
-- ORC. Tez: 7.441
-- Parquet. Tez: 
select
    UniqueCarrier
from
    (
    select
        f.UniqueCarrier, count(1) as flights_amount
    from
        flights f
    group by
        f.UniqueCarrier
    order by flights_amount desc
    limit 1
    ) t;
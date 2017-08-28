with cancelled as (
    select
        f.UniqueCarrier as carrier
        , a.city
    from
        flights f
    join
        airports a
    on
        a.iata = f.origin
        and f.cancelled = 1
)
select 
    carrier
    , concat_ws(', ', collect_set(city)) as cities
from
    cancelled
group by carrier;
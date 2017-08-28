with 
    parsed as
        (SELECT
            c.city_name as city
            , get_user_agent(a.user_agent) as user_agent
        from
            ads a
        join
            cities c
        on
            a.city_id = c.city_id)
    , counted as 
        (select distinct
            city
            , user_agent.device as device
            , user_agent.browser as browser
            , user_agent.os as os
            , count(1) over (partition by city, user_agent.device) as device_amount
            , count(1) over (partition by city, user_agent.browser) as browser_amount
            , count(1) over (partition by city, user_agent.os) as os_amount
        from
            parsed)
    , maximized as
        (select
            city
            , device
            , device_amount
            , max(device_amount) over (partition by city) as max_device_amount
            , browser
            , browser_amount
            , max(browser_amount) over (partition by city) as max_browser_amount
            , os
            , os_amount
            , max(os_amount) over (partition by city) as max_os_amount
        from
            counted)
    , devices as (select distinct city, device, device_amount from maximized where device_amount = max_device_amount)
    , browsers as (select distinct city, browser, browser_amount from maximized where browser_amount = max_browser_amount)
    , o_systems as (select distinct city, os, os_amount from maximized where os_amount = max_os_amount)
select
    d.city
    , d.device
    , d.device_amount
    , b.browser
    , b.browser_amount
    , o.os
    , o.os_amount
from
    devices d
join
    browsers b
on
    d.city = b.city
join
    o_systems o
on
    b.city = o.city
order by city;
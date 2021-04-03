-----------------------------------mazanie doterajsich dat, ak take existuju

TRUNCATE TABLE Accounts, Client,Tickets, Events,Bet_options,Betting_odds,Ticket_del_bet_open,odds_del,Operations, Recharge, Deposit RESTART IDENTITY CASCADE;

-----------------------------------generacia dat--------------nastavovanie objemu je v spodnej casti tejto sekcie
--ucty pre klientov
insert into Accounts (money)
select  floor(random() * 200) + 100 as money
from generate_series(1, 1001) as seq(i);

--first names  /  copy Alexander Simko project
drop table if exists first_names cascade;
create table first_names
(
    first_name varchar
);
insert into first_names (first_name)
values	('James'), ('Willie'), ('Chad'), ('Zachary'), ('Mathew'),
	('John'), ('Ralph'), ('Jacob'), ('Corey'), ('Tyrone'),
	('David'), ('Benjamin'), ('Kyle'), ('Roberto'), ('Cody');

-- last names / copy Alexander Simko project

drop table if exists last_names cascade;
create table last_names
(
    last_name varchar
);

insert into last_names (last_name)
values	('Smith'), ('Jones'), ('Taylor'), ('Williams'), ('Brown'),
	('Davies'), ('Evans'), ('Wilson'), ('Thomas'), ('Roberts'),
	('Adams'), ('Campbell'), ('Anderson'), ('Allen'), ('Cook');


create or replace function random_first_name() returns varchar language sql as
$$
select first_name from first_names order by random() limit 1
$$;
create or replace function random_last_name() returns varchar language sql as
$$
select last_name from last_names order by random() limit 1
$$;
insert into Client (first_name, last_name,birth_number,email,login,password,account_id)
select	random_first_name(),
        random_last_name(),
        floor(random() * 200) as birth_number,
        'email'||i ||'@gmail.com' as email,
        'login'||i as login,
        'heslo'||i as password,
        i as accoun_id
from generate_series(2, 1001) as seq(i);

-- vytvaranie eventov

drop table if exists type_s cascade;
create table type_s
(
    type varchar
);
insert into type_s (type)
values	('football'), ('hockey'), ('futsal'), ('basketball'), ('volleyball'), ('handball');

create or replace function random_type() returns varchar language sql as
$$
select type from type_s order by random() limit 1
$$;

insert into Events (type, name,date,time,event_status,max_win,min_bet)   --
select random_type(),
       'team vs team'||i as name,
        date(now() + trunc(random()  * 20 +1) * '2 day'::interval)as date,
       '00:00:00'::time + i * '1 minute'::interval as time,
       'open' as event_status,
       10000 as max_win,
       5 as min_bet
from generate_series(1, 100) as seq(i);

insert into Events (type, name,date,time,event_status,max_win,min_bet)   --
select random_type(),
       'team vs team'||i as name,
       date(now() - trunc(random()  * 20) * '2 day'::interval)as date,
       '00:00:00'::time + i * '1 minute'::interval as time,
       'close' as event_status,
       10000 as max_win,
       5 as min_bet
from generate_series(1, 1000) as seq(i);

--pridavanie nahodnych moznosti na tipovanie

drop table if exists name_bet cascade;
create table name_bet
(
    name_bet varchar
);
insert into name_bet (name_bet)
values	('1'), ('2'), ('X');
create or replace function random_name_bet() returns varchar language sql as
$$
select name_bet from name_bet order by random() limit 1
$$;
insert into bet_options (name, bet_odds,status,event_id)   --
select  random_name_bet(),
       round( CAST(float8 '1.1415927'+(random() * 2.9) as numeric), 2),
       case when random() < 0.5 then 'win' else 'lost' end as status,
       i as event_id
from generate_series(1, 1000) as seq(i);

drop table if exists name_bet cascade;
create table name_bet
(
    name_bet varchar
);
insert into name_bet (name_bet)
values	('1X'), ('X2'), ('+2g'), ('-2g');

insert into bet_options (name, bet_odds,status,event_id)   --
select  random_name_bet(),
       round( CAST(float8 '1.1415927'+(random() * 2.9) as numeric), 2),
       'N'as status,
       i as event_id
from generate_series(1, 1000) as seq(i);

insert into Tickets (bet_money, date, client_id)    --
select  --floor(random() * 10)+1max_win_events,
       --'bet' as ticket_status,
       floor(random() * 200)+5 as bet_money,
       date(now() - trunc(random()  * 5) * '1 day'::interval)as date,
       floor(random() * 999)+1 as cleint_id
from generate_series(1, 100000) as seq(i);

insert into betting_odds (bet_option_id, ticket_id)    --
select  floor(random() * 1999)+1 as bet_option_id,
        floor(random() *99999)+1 as ticket_id
from generate_series(1, 5000) as seq(i)
ON CONFLICT DO NOTHING ;

UPDATE tickets    --
set max_win_events = floor(random() *(Select count(id) from betting_odds where ticket_id=i))+1,
    ticket_status =        CASE
                               WHEN (Select count(status) from Betting_odds left join Bet_options
                                   On Betting_odds.bet_option_id=Bet_options.id
                                   where ticket_id=i and status = 'lost') > 0 THEN 'lost'
                               WHEN (Select count(status) from Betting_odds left join Bet_options
                                    On Betting_odds.bet_option_id=Bet_options.id
                                    where ticket_id=i and status = 'win') = (Select count(status) from Betting_odds left join Bet_options
                                    On Betting_odds.bet_option_id=Bet_options.id
                                    where ticket_id=i) THEN 'win'
                               ELSE 'bet'
        END
from generate_series(1, 100000) as seq(i)
where id = i;

INSERT into Operations(date, time,type)
select        date(now() - trunc(random()  * 20) * '2 day'::interval)as date,
              '00:00:00'::time + i * '1 minute'::interval as time,
               case when i<50 then 'd'
                     when i<100 then 'r'
                     when i<150 then 'o'
                     else 't' end as type
from generate_series(1, 200) as seq(i);

INSERT into Deposit(amount, operation_id, account_id)
select  floor(random() * 100)+1 as amount,
        i as operation_id,
        floor(random() * 999)+1 as account_id
from generate_series(1, 50) as seq(i);

INSERT into Recharge(amount, operation_id, account_id)
select  floor(random() * 100)+1 as amount,
        i as operation_id,
        floor(random() * 999)+1 as account_id
from generate_series(50, 100) as seq(i);

INSERT into odds_del( operation_id)
select   i as operation_id
from generate_series(100, 150) as seq(i);

INSERT into ticket_del_bet_open( status,operation_id)
select   case when random() < 0.33 then 'del'
              when random() < 0.66 then 'bet'
              else 'open' end as status,
        i as operation_id
from generate_series(150, 200) as seq(i);

UPDATE accounts SET money=10000000 where id = 1;



drop table first_names, last_names,type_s,name_bet cascade;

drop function random_first_name();
drop function random_last_name();
drop function random_type();
drop function random_name_bet();
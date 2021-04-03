drop table if exists Accounts cascade;
create table Accounts
                    (
                        id serial primary key,
                        money numeric
                    );
                    drop table if exists Client cascade;
                    create table Client
                    (
                        id serial primary key,
                        first_name varchar,
                        last_name varchar,
                        birth_number integer,
                        email varchar CONSTRAINT proper_email CHECK (email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'),
                        login varchar,
                        password varchar,
                        account_id integer references Accounts
                    );

                    drop table if exists Admin cascade;
                    create table Admin
                    (
                        id serial primary key,
                        login varchar,
                        password varchar,
                        account_id integer references Accounts
                    );

                    drop table if exists Events cascade;
                    create table Events
                    (
                        id serial primary key,
                        type varchar,
                        name varchar,
                        date date,
                        time time,
                        event_status varchar,
                        max_win  numeric CHECK (max_win>0),
                        min_bet numeric CHECK (min_bet>0)
                    );
                    drop table if exists Tickets cascade;
                    create table Tickets
                    (
                        id serial primary key,
                        max_win_events integer,
                        ticket_status varchar,
                        bet_money numeric,
                        date date,
                        client_id integer references Client
                    );

                    drop table if exists Bet_options cascade;
                    create table Bet_options
                    (
                        id serial primary key,
                        name varchar,
                        bet_odds numeric CHECK (bet_odds>0),
                        status varchar,
                        event_id integer references Events ON DELETE CASCADE,
                        Unique (name,event_id)
                    );

                    drop table if exists Operations cascade;
                    create table Operations
                    (
                        id serial primary key,
                        date date,
                        time time,
                        type varchar
                    );

                    drop table if exists Betting_odds cascade;
                    create table Betting_odds
                    (
                        id serial primary key,
                        bet_option_id integer references Bet_options ON DELETE CASCADE,
                        ticket_id integer references Tickets ON DELETE CASCADE,
                        Unique (bet_option_id,ticket_id)
                    );

                    drop table if exists Ticket_del_bet_open cascade;
                    create table Ticket_del_bet_open
                    (
                        id serial primary key,
                        status varchar,
                        operation_id integer references Operations
                      --  ticket_id integer references Tickets
                    );

                    drop table if exists Recharge cascade;
                    create table Recharge
                    (
                        id serial primary key,
                        amount numeric,
                        operation_id integer references Operations,
                        account_id integer references Accounts
                    );

                    drop table if exists Deposit cascade;
                    create table Deposit
                    (
                        id serial primary key,
                        amount numeric,
                        operation_id integer references Operations,
                        account_id integer references Accounts
                    );

                    Drop table if exists first_names cascade;
                    create table first_names(first_name varchar);

                    drop table if exists Odds_del cascade;
                    create table Odds_del
                    (
                        id serial primary key,
                        operation_id integer references Operations
                     --   bet_option_id integer references Bet_options
                    );
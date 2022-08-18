create database GuessANumberGame;
use GuessANumberGame;
create table player (
	username varchar(255),
    password varchar(255) not null,
    name varchar(255) not null,
    primary key (username)
);
create table game_session (
	startId int unsigned,
    id varchar(255) unique not null,
    targetNumber int unsigned not null,
    startTime timestamp not null,
    endTime timestamp not null,
    completed bit(1) not null default 0,
    active bit(1) not null default 0,
    username varchar(255),
    primary key (id),
    foreign key (username) references player (username)
);
create table guess (
	gameSessionId varchar(255),
    guessNumber int unsigned not null,
    result varchar(255) not null,
    time timestamp not null,
    foreign key (gameSessionId) references game_session (id)
);

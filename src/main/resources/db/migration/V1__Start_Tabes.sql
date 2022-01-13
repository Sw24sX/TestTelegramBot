create sequence hibernate_sequence start 1 increment 1;

create table "users" (
    "id" int8 not null primary key,
    "chat_id" varchar not null unique,
    "name" varchar not null,
    "score" int4 not null default 0,
    "high_score" int4 not null default 0,
    "bot_state" varchar not null
);

create table "java_quiz" (
    "id" int8 not null primary key,
    "question" varchar not null,
    "option1" varchar not null,
    "option2" varchar not null,
    "option3" varchar not null,
    "answer_correct" varchar not null
);
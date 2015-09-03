create table users (
  id bigint not null primary key,
  username varchar_ignorecase(50) not null unique,
  password varchar_ignorecase(50) not null,
  enabled boolean not null);

create table authorities (
  username varchar_ignorecase(50) not null,
  authority varchar_ignorecase(50) not null,
  constraint fk_authorities_users foreign key(username) references users(username));

create unique index ix_auth_username on authorities (user_id, authority);

create table groups (
  id bigint not null primary key,
  name varchar(50) not null unique);

create table user_group (
  user_id bigint not null,
  group_id bigint not null,
  constraint pk_user_group primary key (user_id, group_id),
  constraint fk_user_group_users foreign key(user_id) references users(id),
  constraint fk_user_group_groups foreign key(group_id) references groups(id));

create table collections (
  id bigint not null primary key,
  name varchar(50) not null unique,
  group_id bigint not null,
  constraint fk_collections_groups foreign key(group_id) references groups(id));

insert into users values (1, 'siska', 'siska', true);
insert into users values (2, 'chris', 'chris', true);
insert into users values (3, 'bert', 'bert', true);
insert into users values (4, 'adriaan', 'adriaan', true);

insert into authorities values ('siska', 'ROLE_USER');
insert into authorities values ('chris', 'ROLE_USER');
insert into authorities values ('bert', 'ROLE_USER');
insert into authorities values ('adriaan', 'ROLE_ADMIN');

insert into groups values (1, 'Studio Brussel');
insert into groups values (2, 'Canvas');

insert into user_group values (1, 1);
insert into user_group values (2, 2);
insert into user_group values (3, 1);
insert into user_group values (3, 2);

insert into collections values (1, 'Music For Life', 1);
insert into collections values (2, 'Cobra''s Classic Battle', 2);

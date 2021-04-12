drop table if exists pessoa CASCADE;
drop sequence if exists seq_pessoa;
create sequence seq_pessoa start with 1 increment by 1;
create table pessoa (id bigint not null, cpf varchar(255) not null, data_criacao date not null, data_nascimento date not null, email varchar(255), nacionalidade varchar(255), naturalidade varchar(255), nome varchar(255) not null, sexo integer, versao timestamp, primary key (id));
alter table pessoa add constraint UK_nlwiu48rutiltbnjle59krljo unique (cpf);


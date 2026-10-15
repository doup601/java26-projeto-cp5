alter table instrucoes add column cancelada tinyint default 0;
alter table instrucoes add column motivo_cancelamento varchar(50);
alter table instrucoes add column data_cancelamento datetime;

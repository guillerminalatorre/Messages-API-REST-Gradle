insert into employees(id_number,name, last_name, mail_username, id_city) values ('1234567', 'jose', 'perez', 'pepe', null);
insert into employees(id_number,name, last_name, mail_username, id_city) values ('1234568', 'Ana', 'Perez', 'ana-perez', null);
insert into employees(id_number,name, last_name, mail_username, id_city) values ('1234569', 'Pedro', 'Rodriguez', 'tete', null);


insert into users(username, password, is_admin, id_employee, is_enabled) values ('jose perez','1234', false, 1,true);
insert into users(username, password, is_admin, id_employee, is_enabled) values ('Pepe Rodriguez','1234', true, 2,true);


insert into recipient_types (name, acronym) values ('Primary Receptor', 'To');

insert into labels(name, id_user) values ('Work',1);

insert into messages(subject, body, datee, id_user_from) values ('Body Message', 'Subject Message','2021-04-23 11:05:07', 1);

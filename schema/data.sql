insert into countries(name) values ('Argentina');

insert into states(name, id_country) values ('Buenos Aires', 1);

insert into cities(zip_code, name, id_state) values (7600, 'Mar del Plata', 1);

insert into employees(id_number,name, last_name, mail_username, id_city) values ('1234567', 'Pedro', 'Rodriguez', 'pepe', 7600);
insert into employees(id_number,name, last_name, mail_username, id_city) values ('1234568', 'Jorge', 'Perez', 'jorge-perez', 7600);
insert into employees(id_number,name, last_name, mail_username, id_city) values ('1234568', 'Ana', 'Perez', 'ana-perez', 7600);

/*password 1234*/
insert into users(username, password, is_admin, id_employee) values ('Pepe Rodriguez','81dc9bdb52d04dc20036dbd8313ed055', true, 1);
insert into users(username, password, is_admin, id_employee) values ('Jorge Perez','81dc9bdb52d04dc20036dbd8313ed055', false, 2);


insert into recipient_types (name, acronym) values ('Primary Receptor', 'To'), ('Carbon Copy', 'CC'),('Blind Carbon Copy', 'BCC');

/*Default*/
insert into labels(name, id_user) values ('Important',null), ('Spam',null);

/*UserLabels*/
insert into labels(name, id_user) values ('Family',1), ('Bank',1), ('Work',2);
insert into labels(name,id_user) values ('Label',2);

insert into messages(subject, body, datee, id_user_from) values ('First Message', 'Hi! How are you?','2021-04-23 11:05:07', 1);
insert into messages(subject, body, datee, id_user_from) values ('Second Message', 'Bye!', '2021-04-23 11:05:07', 1);

insert into recipients (id_message, id_recipient_type, id_user) values (1,1,2);
insert into recipients (id_message, id_recipient_type, id_user) values (1,3,2);


insert into labels_x_messages (id_label, id_message,id_user) values (1,1,1);
insert into labels_x_messages (id_label, id_message,id_user) values (3,1,1);
insert into labels_x_messages (id_label, id_message,id_user) values (5,1,2);
insert into labels_x_messages (id_label, id_message,id_user) values (1,1,2);

select * from countries;
select * from users e ;
select * from employees e;
select * from recipient_types rt;
select * from messages;
select * from recipients r;
select * from labels;
select * from labels_x_messages lxm ;
select * from labels_users lu ;
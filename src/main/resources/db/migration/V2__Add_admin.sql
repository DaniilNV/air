insert into users (id, username, password, active,email)
values (1,'admin', '$2a$10$1XQot4IJPzSaDab/w.LvAOiTMddIMMm54D5zvSkYAgmcqCBJkwV7.', true, 'shontar002@mail.ru');

insert into user_role (user_id, roles)
values (1,'ADMIN'), (1,'USER');

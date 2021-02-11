insert into users (id, username, password, active,email)
values (100,'admin', '$2a$10$1XQot4IJPzSaDab/w.LvAOiTMddIMMm54D5zvSkYAgmcqCBJkwV7.', true, ' ');

insert into user_role (user_id, roles)
values (100,'ADMIN'), (100,'USER');

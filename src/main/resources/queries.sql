INSERT INTO usr(`user_id`, `username`, `password`, `active`)
VALUES
(1, 'root', '$2a$08$ef8vHmCwNhvPz2FVr8CjVev.6bElRL5hN2c2uYpsSNiGWaY6B2PJ6', true);

INSERT INTO `user_role`(`user_id`, `roles`)
VALUES
(1,'USER'),
(1,'PROGRAMMER'),
(1,'ADMINISTRATOR');


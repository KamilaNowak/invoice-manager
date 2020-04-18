INSERT INTO users(id,username, passwd, email, date_of_birth)
    VALUES (1,'test','$2a$10$HHNwc6T2ZTPHqp/DZX0WcOdt34VeIqoFSFL.3J3zs87AJ31Syap72','test@email.com',TO_TIMESTAMP('2000-01-01 00:00:00.000000000', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_TIMESTAMP('2020-04-18 00:00:00.000000000', 'YYYY-MM-DD HH24:MI:SS.FF'));

INSERT INTO users(id,username, passwd, email, date_of_birth)
    VALUES (2,'johndoe','$2a$10$vFevsUj4CaxjDbVPp6qnW.TI9uclfhTi8X3CUHxsxpp5fC7mtKw/G','john@gmail.com',TO_TIMESTAMP('1999-04-02 00:00:00.000000000', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_TIMESTAMP('2020-04-18 00:00:00.000000000', 'YYYY-MM-DD HH24:MI:SS.FF'));

INSERT INTO users(id,username, passwd, email, date_of_birth)
    VALUES (3,'aliciasmith','$2a$10$3IossZnbrPNgPas.aHqJzeq9I2Ewnu5NDJy27PmwGQO3n1U52E8DW','asmith@yahoo.com',TO_TIMESTAMP('1990-04-023 00:00:00.000000000', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_TIMESTAMP('2020-04-18 00:00:00.000000000', 'YYYY-MM-DD HH24:MI:SS.FF'));
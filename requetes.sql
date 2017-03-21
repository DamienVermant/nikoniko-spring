USE nikoniko_red;

INSERT INTO user(id, user_login, user_password, user_firstname, user_lastname, user_registration,
                 user_sex)
VALUES  (1,"login1", "password1", "prenom1", "nom", "XLE1522",'F'),
    (2,"login2", "password2", "prenom2", "nom", "XLE1522",'F'),
    (3,"login3", "password3", "prenom3", "nom", "XLE1522",'F'),
    (4,"login4", "password4", "prenom4", "nom", "XLE1522",'F'),
    (5,"login5", "password5", "prenom5", "nom", "XLE1522",'F'),
    (6,"login6", "password6", "prenom6", "nom", "XLE1522",'F'),
    (7,"login7", "password7", "prenom7", "nom", "XLE1522",'F'),
    (8,"login8", "password8", "prenom8", "nom", "XLE1522",'F'),
    (9,"login9", "password9", "prenom9", "nom", "XLE1522",'F'),
    (10,"login10", "password10", "prenom10", "nom", "XLE1522",'F'),
    (11,"login11", "password11", "prenom11", "nom", "XLE1522",'F'),
    (12,"login12", "password12", "prenom12", "nom", "XLE1522",'F');

INSERT INTO nikoniko (id, nikoniko_comment,nikoniko_entry_date,nikoniko_mood, user_id)
VALUES  (1,"coucou","2017-03-20 12:59:59",2,1),
		(2,"coucou","2017-03-13 23:59:59",3,1),
		(3,"coucou","2017-03-15 23:59:59",1,1),
		(4,"coucou","2017-03-18 23:59:59",1,1),
		(5,"coucou","2017-03-19 23:59:59",3,1),
		(6,"coucou","2017-03-12 23:59:59",2,2),
		(7,"coucou","2017-03-13 23:59:59",2,2),
		(8,"coucou","2017-03-15 23:59:59",3,2),
		(9,"coucou","2017-03-19 23:59:59",2,2),
		(10,"coucou","2017-03-20 23:59:59",1,2),
		(11,"coucou","2017-03-12 23:59:59",2,3),
		(12,"coucou","2017-03-13 23:59:59",3,3);





	
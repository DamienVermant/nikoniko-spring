USE nikoniko_red;

INSERT INTO verticale(id, agency, name)
VALUES	(1, "Rennes", "Dev"),
		(2, "Rennes", "RH"),
        (3, "Rennes", "Chef"),
        (4, "Rennes", "Stagiaire");


INSERT INTO user(id, login, password, firstname, lastname, registration_number, sex, verticale_id, enable)
VALUES  (1,"login1", "password1", "prenom1", "nom", "XLE1522",'F',1, 1),
    (2,"login2", "password2", "prenom2", "nom", "XLE1523",'F',1,1),
    (3,"login3", "password3", "prenom3", "nom", "XLE1524",'F',2, 1),
    (4,"login4", "password4", "prenom4", "nom", "XLE1525",'F',2, 1),
    (5,"login5", "password5", "prenom5", "nom", "XLE1526",'F',3, 1),
    (6,"login6", "password6", "prenom6", "nom", "XLE1527",'F',3, 1),
    (7,"login7", "password7", "prenom7", "nom", "XLE1528",'F',4, 1),
    (8,"login8", "password8", "prenom8", "nom", "XLE1529",'F',4, 1),
    (9,"login9", "password9", "prenom9", "nom", "XLE1530",'F',4, 1),
    (10,"login10", "password10", "prenom10", "nom", "XLE1531",'F',4, 1),
    (11,"login11", "password11", "prenom11", "nom", "XLE1532",'F',4, 1),
    (12,"login12", "password12", "prenom12", "nom", "XLE1533",'F',4, 1);

INSERT INTO nikoniko (id, nikoniko_comment, entry_date, mood, user_id)
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


INSERT INTO team (id, name, start_date, privacy, visibility, 
					stickers_colors, stickers_number, serial, verticale_id)
VALUES	(1, "Genius", "2017-03-20 12:59:59", 0, 25, "red", "5", "CGI3542", 1),
		(2, "Normal", "2017-03-20 12:59:59", 0, 5, "red", "6", "CGI5419", 1),
        (3, "Mouton", "2017-03-20 12:59:59", 0, 1, "red", "7", "CGI218", 2),
        (4, "Team42", "2017-03-20 12:59:59", 0, 2, "red", "8", "CGI4897", 1);


	
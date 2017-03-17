USE nikonikospring;
INSERT INTO nikoniko (id, change_date, nikoniko_comment,isAnonymous,log_date,satisfaction,project_id, user_id)
VALUES  (1,"2009-02-12 23:59:59","coucou",false,"2008-01-12 23:59:59",2,null,null),
		(2,"2309-01-12 23:59:59","coucou",false,"2008-01-12 23:59:59",3,null,null),
		(3,"2009-04-12 23:59:59","coucou",false,"2008-01-12 23:59:59",1,null,null),
		(4,"2049-01-10 23:59:59","coucou",false,"2008-01-12 23:59:59",1,null,null),
		(5,"2009-01-12 23:59:59","coucou",false,"2008-01-12 23:59:59",3,null,null),
		(6,"2209-04-12 23:59:59","coucou",false,"2008-01-12 23:59:59",2,null,null),
		(7,"2009-05-02 23:59:59","coucou",false,"2008-01-12 23:59:59",2,null,null),
		(8,"2009-01-12 23:59:59","coucou",false,"2008-01-12 23:59:59",3,null,null),
		(9,"2029-03-12 23:59:59","coucou",false,"2008-01-12 23:59:59",2,null,null),
		(10,"2009-01-12 23:59:59","coucou",false,"2008-01-12 23:59:59",1,null,null),
		(11,"2209-01-12 23:59:59","coucou",false,"2008-01-12 23:59:59",2,null,null),
		(12,"2009-02-10 23:59:59","coucou",false,"2008-01-12 23:59:59",3,null,null);


INSERT INTO project(id, end_date, name, start_date)
VALUES  (1,"2009-02-12 23:59:59", "robot", "2008-01-12 23:59:59"),
	(2,"2029-02-12 23:59:59", "crinos", "2008-01-12 23:59:59"),
	(3,"2209-02-12 23:59:59", "vampire", "2008-01-12 23:59:59"),
	(4,"2209-02-12 23:59:59", "mage", "2008-01-12 23:59:59"),
	(5,"2009-02-12 23:59:59", "projet", "2008-01-12 23:59:59"),
	(6,"2029-02-12 23:59:59", "pastille", "2008-01-12 23:59:59"),
	(7,"2029-02-12 23:59:59", "pates", "2008-01-12 23:59:59"),
	(8,"2109-02-12 23:59:59", "sonde", "2008-01-12 23:59:59"),
	(9,"2059-02-12 23:59:59", "voiture", "2008-01-12 23:59:59"),
	(10,"2059-02-12 23:59:59", "classe", "2008-01-12 23:59:59"),
	(11,"2509-02-12 23:59:59", "vide", "2008-01-12 23:59:59"),
	(12,"2009-02-12 23:59:59", "GLADOS", "2008-01-12 23:59:59");


INSERT INTO user(id, login, password, firstname, lastname, registration_cgi, sex)
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
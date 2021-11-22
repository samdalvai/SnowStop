
BEGIN;

/* put the two transactions together in the same transaction, otherwise circular reference does not work */

INSERT INTO BuildingSite VALUES

('Town Hall Egna','39040','ALDINO',1001,10,15,10,'concrete-tile'),
('Ice Stadium Hockey Bolzano','39100','BOLZANO',1002,20,100,20,'concrete-tile'),
('Mr. Mario Rossi House','40121','BOLOGNA',1003,10,15,30,'concrete-tile'),
('Mr. Max Mustermann House','39040','BARBIANO',1004,10,15,45,'concrete-tile'),
('Train Station Florence','50100','FIRENZE',1005,20,50,10,'concrete-tile'),
('UniBZ Roof','39100','BOLZANO',1006,20,100,30,'standing-seam-sheet'),
('Cantina vini Montepulciano','53045','MONTEPULCIANO',1007,20,100,50,'standing-seam-sheet');

INSERT INTO Computation VALUES

(1001,'2021-08-28',0,0),
(1002,'2021-08-28',0,0),
(1003,'2021-08-28',0,0),
(1004,'2021-08-28',0,0),
(1005,'2021-08-28',0,0),
(1006,'2021-08-28',0,0),
(1007,'2021-08-28',0,0);


COMMIT;


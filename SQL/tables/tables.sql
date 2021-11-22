/* ===================================================================
-- Tables Creation 
-- ================================================================ */

BEGIN;

CREATE TABLE Product ( /* Named SnowstopProduct in the relational schema */
	code NUMERIC PRIMARY KEY,
	name VARCHAR(40) NOT NULL,
	material VARCHAR(20) NOT NULL,
	color VARCHAR(10),
	price NUMERIC NOT NULL,
	CONSTRAINT codeCheck CHECK (code >= 100000 AND code <= 999999),
	CONSTRAINT materialCheck CHECK (material IN ('Zink Steel','Stainless Steel','Painted Steel','Aluminium','Copper')),
	CONSTRAINT colorCheck CHECK (color IN ('Red', 'Brown', 'Black', 'Grey')),
	CONSTRAINT coloredMaterialCheck CHECK ((color IS NULL and material != 'Painted Steel') or (color IS NOT NULL and material = 'Painted Steel')),
	CONSTRAINT priceCheck CHECK (price > 0)
);

CREATE TABLE Retainer ( /* Named SnowRetainer in the relational schema */
	code NUMERIC PRIMARY KEY,
	LinearResistance NUMERIC NOT NULL,
	retainerType VARCHAR(10) NOT NULL,
	measure NUMERIC NOT NULL,
	profile VARCHAR(20),
	CONSTRAINT resistanceCheck CHECK (LinearResistance > 0),
	CONSTRAINT retainerTypeCheck CHECK (retainerType IN ('Tube','Grid')),
	CONSTRAINT profileCheck CHECK ((retainerType = 'Tube' and profile IS NULL) or (retainerType = 'Grid') AND profile IS NOT NULL),
	CONSTRAINT retainerInProduct FOREIGN KEY (code)
		REFERENCES Product (code)
		ON UPDATE CASCADE
		ON DELETE CASCADE
		DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Holder ( /* Named RetainerHolder in the relational schema */
	code NUMERIC PRIMARY KEY,
	resistance NUMERIC NOT NULL,
	roofType VARCHAR(20) NOT NULL,
	retainerType VARCHAR(10) NOT NULL,
	CONSTRAINT resistanceCheck CHECK (resistance > 0),
	CONSTRAINT roofTypeCheck CHECK (roofType IN ('concrete-tile', 'ondulated-plate', 'trapezoidal-sheet', 'standing-seam-sheet', 'flat-tile')),
	CONSTRAINT retainerTypeCheck CHECK (retainerType IN ('Tube','Grid')),
	CONSTRAINT holderInProduct FOREIGN KEY (code)
		REFERENCES Product (code)
		ON UPDATE CASCADE
		ON DELETE CASCADE
		DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Accessory ( /* Named RetainerAccessory in the relational schema */
	code NUMERIC PRIMARY KEY,
	measure VARCHAR(30) NOT NULL,
	retainerType VARCHAR(10) NOT NULL,
	type VARCHAR(20) NOT NULL,
	CONSTRAINT typeCheck CHECK (type IN ('Connection','Ice-Retainer')),
	CONSTRAINT retainerTypeCheck CHECK (retainerType IN ('Tube','Grid')),
	CONSTRAINT accessoryInProduct FOREIGN KEY (code)
		REFERENCES Product (code)
		ON UPDATE CASCADE
		ON DELETE CASCADE
		DEFERRABLE INITIALLY DEFERRED
);


CREATE TABLE Province (
	shorthand VARCHAR(2) PRIMARY KEY,
	name VARCHAR(40) NOT NULL UNIQUE,
	zone VARCHAR(3) NOT NULL,
	baseLoad NUMERIC NOT NULL,
	CONSTRAINT zoneCheck CHECK (zone IN ('I-A', 'I-M', 'II', 'III')),
	CONSTRAINT loadCheck CHECK (baseLoad > 0)
);

CREATE TABLE City (
	zip VARCHAR(5) NOT NULL,
	name VARCHAR(40) NOT NULL,
	province VARCHAR(2) NOT NULL,
	altitude NUMERIC NOT NULL,
	CONSTRAINT citiPK PRIMARY KEY (zip,name),
	CONSTRAINT cityInProvince FOREIGN KEY (province)
		REFERENCES Province (shorthand)
		ON UPDATE CASCADE
		ON DELETE RESTRICT
		DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Customer (
	code NUMERIC PRIMARY KEY,
	name varchar(50) NOT NULL,
	zip VARCHAR(5) NOT NULL,
	city VARCHAR(40) NOT NULL,
	discount NUMERIC,
	CONSTRAINT codeCheck CHECK (code >= 40000000 AND code <= 50000000),
	CONSTRAINT discountCheck CHECK (discount >= 0 OR discount <= 30),
	CONSTRAINT customerInCity FOREIGN KEY (zip,city)
		REFERENCES City (zip,name)
		ON UPDATE CASCADE
		ON DELETE RESTRICT
		DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Phone (
	number VARCHAR(10) PRIMARY KEY,
	customerCode NUMERIC NOT NULL,
	CONSTRAINT customerCodeInCustomer FOREIGN KEY (customerCode)
		REFERENCES Customer (code)
		ON UPDATE CASCADE
		ON DELETE RESTRICT
		DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE BuildingSite (
	name VARCHAR(30) NOT NULL,
	zip VARCHAR(5) NOT NULL,
	city VARCHAR(40) NOT NULL,
	computationCode NUMERIC NOT NULL UNIQUE,
	length NUMERIC NOT NULL,
	width NUMERIC NOT NULL,
	steepness NUMERIC NOT NULL,
	covering VARCHAR(20) NOT NULL,
	CONSTRAINT computationCodeCheck CHECK (computationCode > 0 AND computationCode <= 100000),
	CONSTRAINT buildingSitePK PRIMARY KEY (name,zip,city),
	CONSTRAINT measuresCheck CHECK (length > 0 AND width > 0 AND steepness > 0 AND steepness < 180),
	CONSTRAINT roofTypeCheck CHECK (covering IN ('concrete-tile', 'ondulated-plate', 'trapezoidal-sheet', 'standing-seam-sheet', 'flat-tile')),
	CONSTRAINT buildingSiteInCity FOREIGN KEY (zip,city)
		REFERENCES City (zip,name)
		ON UPDATE CASCADE
		ON DELETE CASCADE
		DEFERRABLE INITIALLY DEFERRED
);


CREATE TABLE Computation ( /* Named SnowloadComputation in the relational schema */
	code NUMERIC NOT NULL PRIMARY KEY,
	date DATE NOT NULL,
	groundLoad NUMERIC NOT NULL,
	roofLoad NUMERIC NOT NULL,
	CONSTRAINT codeCheck CHECK (code > 0 AND code <= 100000),
	CONSTRAINT groundLoadCheck CHECK (groundLoad >= 0),
	CONSTRAINT roofLoadCheck CHECK (roofLoad >= 0),
	CONSTRAINT computationForBuildingSite FOREIGN KEY (code)
		REFERENCES BuildingSite (computationCode)
		ON UPDATE CASCADE
		ON DELETE CASCADE
		DEFERRABLE INITIALLY DEFERRED
);

ALTER TABLE BuildingSite
ADD CONSTRAINT buildingSiteWithComputation FOREIGN KEY (computationCode)
		REFERENCES Computation (code)
		ON UPDATE CASCADE
		ON DELETE CASCADE
		DEFERRABLE INITIALLY DEFERRED;


CREATE TABLE SupplyOffer (
	code NUMERIC PRIMARY KEY,
	computationCode NUMERIC NOT NULL,
	date DATE NOT NULL,
	totalPrice NUMERIC NOT NULL,
	totalResistance NUMERIC NOT NULL,
	rows NUMERIC NOT NULL,
	distance NUMERIC NOT NULL,
	CONSTRAINT codeCheck CHECK (code > 0 AND code <= 100000),
	CONSTRAINT computationCodeCheck CHECK (computationCode > 0 AND computationCode <= 100000),
	CONSTRAINT totalPriceCheck CHECK (totalPrice >= 0),
	CONSTRAINT rowCheck CHECK (rows >= 1),
	CONSTRAINT distanceChekc CHECK (distance >= 100 AND distance <= 1000),
	CONSTRAINT computationCodeInComputation FOREIGN KEY (computationCode)
		REFERENCES Computation (code)
		ON UPDATE CASCADE
		ON DELETE CASCADE
		DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE offerForCustomer ( /* Named For-Customer in the relational schema */
	offerCode NUMERIC NOT NULL,
	customerCode NUMERIC NOT NULL,
	CONSTRAINT offerCodeCheck CHECK (offerCode > 0 AND offerCode <= 100000),
	CONSTRAINT offerForCustomerPK PRIMARY KEY (offerCode, customerCode),
	CONSTRAINT offerCodeInSupplyOffer FOREIGN KEY (offerCode)
		REFERENCES SupplyOffer (code)
		ON UPDATE CASCADE
		ON DELETE CASCADE
		DEFERRABLE INITIALLY DEFERRED,
	CONSTRAINT customerCodeInCustomer FOREIGN KEY (customerCode)
		REFERENCES Customer (code)
		ON UPDATE CASCADE
		ON DELETE CASCADE
		DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE offerForProduct ( /* Named Offer-Prod in the relational schema */
	offerCode NUMERIC NOT NULL,
	productCode NUMERIC NOT NULL,
	quantity NUMERIC NOT NULL,
	CONSTRAINT offerCodeCheck CHECK (offerCode > 0 AND offerCode <= 100000),
	CONSTRAINT offerForProductPK PRIMARY KEY (offerCode,productCode),
	CONSTRAINT quantityCheck CHECK (quantity > 0),
	CONSTRAINT offerCodeInSupplyOffer FOREIGN KEY (offerCode)
		REFERENCES SupplyOffer (code)
		ON UPDATE CASCADE
		ON DELETE CASCADE
		DEFERRABLE INITIALLY DEFERRED,
	CONSTRAINT productCodeInProduct FOREIGN KEY (productCode)
		REFERENCES Product (code)
		ON UPDATE CASCADE
		ON DELETE CASCADE
		DEFERRABLE INITIALLY DEFERRED
);

COMMIT;


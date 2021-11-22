/* ===================================================================
-- Tables Destruction 
-- ================================================================ */

BEGIN;

ALTER TABLE IF EXISTS Retainer DROP CONSTRAINT IF EXISTS retainerInProduct;
ALTER TABLE IF EXISTS Holder DROP CONSTRAINT IF EXISTS holderInProduct;
ALTER TABLE IF EXISTS Accessory DROP CONSTRAINT IF EXISTS accessoryInProduct;

ALTER TABLE IF EXISTS Customer DROP CONSTRAINT IF EXISTS customerInCity;
ALTER TABLE IF EXISTS City DROP CONSTRAINT IF EXISTS cityInProvince;
ALTER TABLE IF EXISTS Phone DROP CONSTRAINT IF EXISTS customerCodeInCustomer;

ALTER TABLE IF EXISTS SupplyOffer DROP CONSTRAINT IF EXISTS computationCodeInComputation;

ALTER TABLE IF EXISTS offerForCustomer DROP CONSTRAINT IF EXISTS offerCodeInSupplyOffer;
ALTER TABLE IF EXISTS offerForCustomer DROP CONSTRAINT IF EXISTS customerCodeInCustomer;

ALTER TABLE IF EXISTS offerForProduct DROP CONSTRAINT IF EXISTS offerCodeInSupplyOffer;
ALTER TABLE IF EXISTS offerForProduct DROP CONSTRAINT IF EXISTS productCodeInProduct;

ALTER TABLE IF EXISTS BuildingSite DROP CONSTRAINT IF EXISTS buildingSiteInCity;
ALTER TABLE IF EXISTS BuildingSite DROP CONSTRAINT IF EXISTS buildingSiteWithComputation;

ALTER TABLE IF EXISTS Computation DROP CONSTRAINT IF EXISTS computationForBuildingSite;

DROP VIEW IF EXISTS allProductsWithType;

DROP VIEW IF EXISTS offerWithProductsAndPrice;

DROP VIEW IF EXISTS computationWithBSData;

DROP VIEW IF EXISTS offerWithComputation;

DROP VIEW IF EXISTS customerWithOfferAndPrice;

DROP TABLE IF EXISTS Product;

DROP TABLE IF EXISTS Retainer;

DROP TABLE IF EXISTS Holder;

DROP TABLE IF EXISTS Accessory;

DROP TABLE IF EXISTS Province;

DROP TABLE IF EXISTS City;

DROP TABLE IF EXISTS Customer;

DROP TABLE IF EXISTS Phone;

DROP TABLE IF EXISTS SupplyOffer;

DROP TABLE IF EXISTS BuildingSite;

DROP TABLE IF EXISTS Computation;

DROP TABLE IF EXISTS offerForCustomer;

DROP TABLE IF EXISTS offerForProduct;


COMMIT;
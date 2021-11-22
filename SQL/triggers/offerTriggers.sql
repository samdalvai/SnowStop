/* ===================================================================
-- Triggers Creation 
-- ================================================================ */

/* Trigger for the update of TotalPrice in SupplyOffer 
   on insert of offerForProduct add to TotalPrice 
   EXTERNAL CONSTRAINT 5 IN THE RELATIONAL SCHEMA */
CREATE OR REPLACE FUNCTION updateTotalPriceOnInsert()
  RETURNS TRIGGER AS 
$$
    BEGIN

        UPDATE supplyoffer
        SET totalPrice = (SELECT SUM(subTotal)
                          FROM offerWithProductsAndPrice
                          WHERE offerWithProductsAndPrice.offercode = NEW.offerCode)
        WHERE code = NEW.offerCode;

        RETURN NEW;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER updateTotalPriceOnInsert
  AFTER INSERT
  ON offerForProduct
  FOR EACH ROW
  EXECUTE PROCEDURE updateTotalPriceOnInsert();

/* Trigger for the update of TotalPrice in SupplyOffer 
   on update of Product price */
CREATE OR REPLACE FUNCTION updateTotalPriceOnUpdateOfProduct()
  RETURNS TRIGGER AS 
$$
    BEGIN

        UPDATE supplyoffer
        SET totalPrice = (SELECT SUM(subTotal)
                          FROM offerWithProductsAndPrice)
        WHERE code IN (SELECT DISTINCT(offercode)
                      FROM offerWithProductsAndPrice
                      WHERE offerWithProductsAndPrice.offercode IN (SELECT distinct(offercode)
												FROM offerforproduct
												WHERE productcode = NEW.code));

        RETURN NEW;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER updateTotalPriceOnUpdateOfProduct
  AFTER UPDATE
  ON product
  FOR EACH ROW
  EXECUTE PROCEDURE updateTotalPriceOnUpdateOfProduct();

/* Trigger for the update of TotalPrice in SupplyOffer 
   on update of offerForProduct compare to old quantity
   and add the difference to TotalPrice (difference might
   be negative, price is also updated if the Retainer is changed
   EXTERNAL CONSTRAINT 5 IN THE RELATIONAL SCHEMA */
CREATE OR REPLACE FUNCTION updateTotalPriceOnUpdate()
  RETURNS TRIGGER AS 
$$
    BEGIN

        UPDATE supplyoffer
        SET totalPrice = (SELECT SUM(subTotal)
                          FROM offerWithProductsAndPrice
                          WHERE offerWithProductsAndPrice.offercode = NEW.offerCode)
        WHERE code = NEW.offerCode;

        RETURN NEW;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER updateTotalPriceOnUpdate
  AFTER UPDATE
  ON offerForProduct
  FOR EACH ROW
  EXECUTE PROCEDURE updateTotalPriceOnUpdate();


/* Trigger to update total price of SupplyOffer on deletion of a product */
CREATE OR REPLACE FUNCTION updateTotalPriceOnDelete()
  RETURNS TRIGGER AS 
$$  
    BEGIN

        UPDATE supplyoffer
        SET totalPrice = (SELECT SUM(subTotal)
                          FROM offerWithProductsAndPrice
                          WHERE offerWithProductsAndPrice.offercode = OLD.offerCode)
        WHERE code = OLD.offerCode;

        RETURN OLD;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER updateTotalPriceOnDelete
  AFTER DELETE
  ON offerForProduct
  FOR EACH ROW
  EXECUTE PROCEDURE updateTotalPriceOnDelete();


/* Update value of TotalResistance in SupplyOffer after inserting or updating
   a Holder in the offerForProduct relation */
CREATE OR REPLACE FUNCTION updateTotalResistanceOnInsertOfRetainer()
  RETURNS TRIGGER AS 
$$
    DECLARE resistance NUMERIC;
    DECLARE rows NUMERIC;
    DECLARE distance NUMERIC;
    DECLARE newResistance NUMERIC;
    BEGIN
        IF NEW.productcode IN (SELECT code FROM holder) THEN /* product is a holder */
            
            resistance = (SELECT holder.resistance
                      FROM holder
                      WHERE code = NEW.productCode);

            rows = (SELECT supplyoffer.rows
                      FROM supplyoffer
                      WHERE supplyoffer.code = NEW.offercode);

            distance = (SELECT supplyoffer.distance
                      FROM supplyoffer
                      WHERE supplyoffer.code = NEW.offercode);

            newResistance = ROUND (rows * (1000 / distance) * resistance, 2);

        UPDATE supplyoffer
        SET totalResistance = newResistance
        WHERE code = NEW.offerCode;

        END IF;

        RETURN NEW;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER updateTotalResistanceOnInsertOfRetainer
  AFTER INSERT OR UPDATE
  ON offerForProduct
  FOR EACH ROW
  EXECUTE PROCEDURE updateTotalResistanceOnInsertOfRetainer();


/* Update value of TotalResistance in SupplyOffer after updating
   a SupplyOffer, for example with a new number of rows or distance
   EXTERNAL CONSTRAINT 1 IN THE RELATIONAL SCHEMA*/
CREATE OR REPLACE FUNCTION updateTotalResistanceOnUpdateOfOffer()
  RETURNS TRIGGER AS 
$$
    DECLARE resistance NUMERIC;
    DECLARE holderCode NUMERIC;
    DECLARE newResistance NUMERIC;
    BEGIN
        IF NEW.rows <> OLD.rows OR NEW.distance <> OLD.distance THEN

            holderCode = (SELECT productCode
                FROM supplyoffer JOIN offerForProduct ON supplyoffer.code = offerForProduct.offerCode
                WHERE code = NEW.code AND productCode IN (SELECT code FROM holder));
            
            resistance = (SELECT holder.resistance
                      FROM holder
                      WHERE code = holderCode);

            newResistance = ROUND (NEW.rows * (1000 / NEW.distance) * resistance, 2);
          
          NEW.totalResistance = newResistance;

        END IF;

        RETURN NEW;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER updateTotalResistanceOnUpdateOfOffer
  BEFORE UPDATE
  ON supplyOffer
  FOR EACH ROW
  EXECUTE PROCEDURE updateTotalResistanceOnUpdateOfOffer();


/* Trigger to check if the TotalResistance of a SupplyOffer is higher than
   the RoofLoad in the associated SnowloadComputation, the check is made after
   a Supply Offer is Updated the totalResistance has been initialized with 
   the previous triggers 
   EXTERNAL CONSTRAINT 1 IN THE RELATIONAL SCHEMA */
CREATE OR REPLACE FUNCTION checkTotalResistanceHigherThanRoofLoad()
  RETURNS TRIGGER AS 
$$
    DECLARE snowLoad NUMERIC;
    BEGIN

        IF (SELECT COUNT(*)
              FROM supplyoffer JOIN offerForProduct ON supplyoffer.code = offerForProduct.offerCode
              WHERE code = NEW.code AND productCode IN (SELECT code FROM holder)) > 0
              AND NEW.totalResistance > 0 THEN

              snowLoad = (SELECT roofLoad
                              FROM computation
                              WHERE computation.code = (SELECT computationcode
                                          FROM supplyoffer
                                          WHERE supplyoffer.code = NEW.code));
              IF NEW.totalResistance < snowLoad THEN
                  RAISE EXCEPTION 'Total resistance lower than roofLoad on offer n. %.
                  Total resistance: % Roofload: %. Please increase number of rows, decrease distance or change Retainer', NEW.code, NEW.totalResistance, snowLoad;
                  RETURN NULL;
              END IF;
        END IF;
        
        RETURN NEW;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER checkTotalResistanceHigherThanRoofLoad
  AFTER UPDATE
  ON supplyOffer
  FOR EACH ROW
  EXECUTE PROCEDURE checkTotalResistanceHigherThanRoofLoad();


/* Trigger that checks if all the products in a supply offer are compatible (for the same retainerType) 
   EXTERNAL CONSTRAINT 2a AND 2b IN THE RELATIONAL SCHEMA */
CREATE OR REPLACE FUNCTION checkProductCompatibilityInOffer()
  RETURNS TRIGGER AS 
$$
    BEGIN
        IF (SELECT COUNT(DISTINCT(retainerType))
              FROM offerWithProductsAndPrice JOIN allProductsWithType ON offerWithProductsAndPrice.productCode = allProductsWithType.code
              WHERE offerWithProductsAndPrice.offerCode = NEW.code) > 1 THEN
              RAISE EXCEPTION 'RetainerType clash on offer: % all the products must have the same value for the retainerType attribute', NEW.code;
        RETURN NULL;
        END IF;
        
        RETURN NEW;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER checkProductCompatibilityInOffer
  BEFORE UPDATE OR INSERT
  ON supplyOffer
  FOR EACH ROW
  EXECUTE PROCEDURE checkProductCompatibilityInOffer();


/* Trigger that checks if a holder associated with a supply offer is 
   compatible with the building site of the computation
   EXTERNAL CONSTRAINT 3 IN THE RELATIONAL SCHEMA */
CREATE OR REPLACE FUNCTION checkHolderCompatibilityWithBS()
  RETURNS TRIGGER AS 
$$  
    DECLARE holderRoofType VARCHAR(20);
    DECLARE buildingSiteRoofType VARCHAR(20);
    BEGIN
        IF NEW.productCode IN (SELECT code FROM holder) THEN
          
            holderRoofType = (SELECT holder.roofType
                                FROM offerWithProductsAndPrice JOIN holder ON offerWithProductsAndPrice.productCode = holder.code
                                WHERE offerWithProductsAndPrice.offerCode = NEW.offerCode);

            buildingSiteRoofType = (SELECT DISTINCT(buildingSite.covering) AS roofType
                                FROM offerWithProductsAndPrice JOIN buildingSite ON offerWithProductsAndPrice.computationCode = buildingSite.computationCode
                                WHERE offerWithProductsAndPrice.offerCode = NEW.offerCode);

            IF holderRoofType <> buildingSiteRoofType THEN
              RAISE EXCEPTION 'Error in offer n. %, roofType attribute in Holder must coincide with covering in Computation. roofType: % covering: %', NEW.offerCode, holderRoofType, buildingSiteRoofType;
            RETURN NULL;
            END IF;

        END IF;
        
        RETURN NEW;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER checkHolderCompatibilityWithBS
  AFTER UPDATE OR INSERT
  ON offerForProduct
  FOR EACH ROW
  EXECUTE PROCEDURE checkHolderCompatibilityWithBS();


/* check that a SupplyOffer has exactly one retainer and one holder
   and between 0 and 2 different types of accessory (Ice-Retainer and Connection,
   not two of the same type)
   EXTERNAL CONSTRAINT 4 IN THE RELATIONAL SCHEMA */
CREATE OR REPLACE FUNCTION checkProductCategoriesInOfferOnInsert()
  RETURNS TRIGGER AS 
$$  
    DECLARE accessoryCount NUMERIC;
    BEGIN

        /* Count holder */
        IF (SELECT count(*)
            FROM offerwithproductsandprice
            WHERE offerwithproductsandprice.offercode = NEW.offerCode AND offerwithproductsandprice.productCode IN
            (SELECT holder.code FROM holder)) <> 1 THEN
            RAISE EXCEPTION 'Error in offer n. %, an Offer must contain exactly one Holder, please add or remove until there is only one Holder', NEW.offerCode;
            RETURN NULL;
        END IF;

        /* Count retainer */
        IF (SELECT count(*)
            FROM offerwithproductsandprice
            WHERE offerwithproductsandprice.offercode = NEW.offerCode AND offerwithproductsandprice.productCode IN
            (SELECT retainer.code FROM retainer)) <> 1 THEN
            RAISE EXCEPTION 'Error in offer n. %, an Offer must contain exactly one Retainer, please add or remove until there is only one Retainer', NEW.offerCode;
            RETURN NULL;
        END IF;

        /* Count accessory */
        accessoryCount = (SELECT count(*)
            FROM offerwithproductsandprice
            WHERE offerwithproductsandprice.offercode = NEW.offerCode AND offerwithproductsandprice.productCode IN
            (SELECT accessory.code FROM accessory));
            IF accessoryCount > 2 THEN
                RAISE EXCEPTION 'Error in offer n. %, an Offer must have between 0 and 2 Accessory, please add or remove until the number of Accessory is correct', NEW.offerCode;
                RETURN NULL;
            END IF;
       
        IF accessoryCount = 2 THEN
            IF (SELECT COUNT(DISTINCT(accessory.type))
                FROM offerwithproductsandprice JOIN accessory ON offerwithproductsandprice.productcode = accessory.code
                WHERE offerwithproductsandprice.offercode = NEW.offerCode) <> 2 THEN
                RAISE EXCEPTION 'Error in offer n. %, two Accessory in the same supply offer must be of different type Ice-Retainer and Connection', NEW.offerCode;
                RETURN NULL;
            END IF;
        END IF;

        RETURN NEW;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER checkProductCategoriesInOfferOnInsert
  BEFORE INSERT OR UPDATE
  ON offerForCustomer
  FOR EACH ROW
  EXECUTE PROCEDURE checkProductCategoriesInOfferOnInsert();


/* Same as above but for update on offerForProduct or delete*/
CREATE OR REPLACE FUNCTION checkProductCategoriesInOfferOnChangeOfProduct()
  RETURNS TRIGGER AS 
$$  
    DECLARE accessoryCount NUMERIC;
    BEGIN

        /* If we are deleting the associated computation or building site, we avoid
           to check, since evertything will be deleted in cascade */
        IF (SELECT COUNT(DISTINCT(computation.code))
            from offerForProduct
            JOIN supplyoffer ON offerForProduct.offercode = supplyoffer.code
            JOIN computation ON supplyoffer.computationcode = computation.code
            WHERE offerForProduct.offercode = OLD.offerCode) > 0 THEN

            /* Count holder */
            IF (SELECT count(*)
                FROM offerwithproductsandprice
                WHERE offerwithproductsandprice.offercode = OLD.offerCode AND offerwithproductsandprice.productCode IN
                (SELECT holder.code FROM holder)) <> 1 THEN
                RAISE EXCEPTION 'Error in offer n. %, an Offer must contain exactly one Holder, please add or remove until there is only one Holder', OLD.offerCode;
                RETURN NULL;
            END IF;

            /* Count retainer */
            IF (SELECT count(*)
                FROM offerwithproductsandprice
                WHERE offerwithproductsandprice.offercode = OLD.offerCode AND offerwithproductsandprice.productCode IN
                (SELECT retainer.code FROM retainer)) <> 1 THEN
                RAISE EXCEPTION 'Error in offer n. %, an Offer must contain exactly one Retainer, please add or remove until there is only one Retainer', OLD.offerCode;
                RETURN NULL;
            END IF;

            /* Count accessory */
            accessoryCount = (SELECT count(*)
                FROM offerwithproductsandprice
                WHERE offerwithproductsandprice.offercode = OLD.offerCode AND offerwithproductsandprice.productCode IN
                (SELECT accessory.code FROM accessory));
                IF accessoryCount > 2 THEN
                    RAISE EXCEPTION 'Error in offer n. %, an Offer must have between 0 and 2 Accessory, please add or remove until the number of Accessory is correct', OLD.offerCode;
                    RETURN NULL;
                END IF;
          
            IF accessoryCount = 2 THEN
                IF (SELECT COUNT(DISTINCT(accessory.type))
                    FROM offerwithproductsandprice JOIN accessory ON offerwithproductsandprice.productcode = accessory.code
                    WHERE offerwithproductsandprice.offercode = OLD.offerCode) <> 2 THEN
                    RAISE EXCEPTION 'Error in offer n. %, two Accessory in the same supply offer must be of different type Ice-Retainer and Connection', OLD.offerCode;
                    RETURN NULL;
                END IF;
            END IF;

        END IF;

        RETURN NEW;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER checkProductCategoriesInOfferOnChangeOfProduct
  AFTER DELETE OR UPDATE
  ON offerForProduct
  FOR EACH ROW
  EXECUTE PROCEDURE checkProductCategoriesInOfferOnChangeOfProduct();
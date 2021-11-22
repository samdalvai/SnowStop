/* ===================================================================
-- Triggers Creation 
-- ================================================================ */

/* Trigger for generalization constraints on Retainer, Holder and Accessory,
   makes sure that the three tables are disjoint */
CREATE OR REPLACE FUNCTION productGeneralizationCheck()
  RETURNS TRIGGER AS 
$$
    BEGIN
        IF NEW.code IN (SELECT code FROM Holder) THEN
            RAISE EXCEPTION 'Generalization error on %, code is already in Holder', NEW;
            RETURN NULL;
        END IF;
        IF NEW.code IN (SELECT code FROM Accessory) THEN
            RAISE EXCEPTION 'Generalization error on %, code is already in Accessory', NEW;
            RETURN NULL;
        END IF;
        IF NEW.code IN (SELECT code FROM Retainer) THEN
            RAISE EXCEPTION 'Generalization error on %, code is already in Retainer', NEW;
            RETURN NULL;
        END IF;
        RETURN NEW;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER productGeneralizationCheck
  BEFORE INSERT
  ON retainer
  FOR EACH ROW
  EXECUTE PROCEDURE productGeneralizationCheck();

CREATE TRIGGER productGeneralizationCheck
  BEFORE INSERT
  ON holder
  FOR EACH ROW
  EXECUTE PROCEDURE productGeneralizationCheck();

CREATE TRIGGER productGeneralizationCheck
  BEFORE INSERT
  ON accessory
  FOR EACH ROW
  EXECUTE PROCEDURE productGeneralizationCheck();


/* Trigger that ensures that a Product is not removed from
   Holder, Retainer or Accessory before it is deleted the
   parent entity */
CREATE OR REPLACE FUNCTION productInclusionCheck()
  RETURNS TRIGGER AS 
$$
    BEGIN
        IF (SELECT count(*)
            FROM product
            where product.code NOT IN (
            SELECT code FROM retainer
            UNION
            SELECT code FROM holder
            UNION
            SELECT code FROM accessory)) > 0 THEN
            RAISE EXCEPTION 'Generalization error on %, must remove from Product first', OLD;
            RETURN NULL;
        END IF;

        RETURN OLD;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER productInclusionCheck
  AFTER DELETE
  ON retainer
  FOR EACH ROW
  EXECUTE PROCEDURE productInclusionCheck();

CREATE TRIGGER productInclusionCheck
  AFTER DELETE
  ON holder
  FOR EACH ROW
  EXECUTE PROCEDURE productInclusionCheck();

CREATE TRIGGER productInclusionCheck
  AFTER DELETE
  ON accessory
  FOR EACH ROW
  EXECUTE PROCEDURE productInclusionCheck();
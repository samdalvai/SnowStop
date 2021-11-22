/* ===================================================================
-- Triggers Creation 
-- ================================================================ */

/* Trigger for inclusion constraint on SupplyOffer present at least one
   time in offerForCustomer relation */
CREATE OR REPLACE FUNCTION offerForCustomerInclusionCheck()
  RETURNS TRIGGER AS 
$$
    BEGIN
        IF (SELECT COUNT(*)
            FROM supplyoffer
            WHERE supplyoffer.code NOT IN (SELECT offerForCustomer.offerCode FROM offerForCustomer)) > 0 THEN
            RAISE EXCEPTION 'Inclusion error on %, a SupplyOffer must be associated with at least one Customer', OLD;
            RETURN NULL;
        END IF;

        RETURN OLD;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER offerForCustomerInclusionCheck
  AFTER DELETE OR UPDATE
  ON offerForCustomer
  FOR EACH ROW
  EXECUTE PROCEDURE offerForCustomerInclusionCheck();


/* Trigger for inclusion constraint on SupplyOffer present at least one
   time in offerForCustomer relation */
CREATE OR REPLACE FUNCTION customerPhoneInclusionCheck()
  RETURNS TRIGGER AS 
$$
    BEGIN
        IF (select COUNT(*)
            FROM customer
            WHERE code NOT IN (SELECT customerCode FROM phone)) > 0 THEN
            RAISE EXCEPTION 'Inclusion error on %, a Customer must have at least one phone number', OLD;
            RETURN NULL;
        END IF;

        RETURN OLD;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER customerPhoneInclusionCheck
  AFTER DELETE OR UPDATE
  ON Phone
  FOR EACH ROW
  EXECUTE PROCEDURE customerPhoneInclusionCheck();


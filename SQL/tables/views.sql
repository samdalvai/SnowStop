/* ===================================================================
-- USEFUL VIEWS FOR THE DATABASE
-- ================================================================ */

BEGIN;


/* All the producs with the compatiple type */
CREATE VIEW allProductsWithType AS (
    SELECT code, retainerType
    FROM retainer
    UNION
    SELECT code, retainerType
    FROM holder
    UNION
    SELECT code, retainerType
    FROM accessory
);


/* All the products with the associated SupplyOffer */
CREATE VIEW offerWithProductsAndPrice AS (
    SELECT supplyoffer.code AS offerCode, supplyoffer.computationCode, offerForProduct.productCode, offerForProduct.quantity, product.price, (offerForProduct.quantity * product.price) as subTotal
    FROM supplyoffer JOIN offerForProduct ON supplyoffer.code = offerForProduct.offerCode JOIN product ON product.code = offerForProduct.productCode
    ORDER BY supplyoffer.code
);

/* All computations with useful data */
CREATE VIEW computationWithBSData AS (
    SELECT computation.code,
    buildingsite.length, buildingsite.width, buildingsite.steepness,buildingsite.covering,
    city.altitude,
    province.baseload, province.zone,
    computation.groundLoad, computation.roofLoad
    FROM computation 
    JOIN buildingsite ON computation.code = buildingsite.computationcode 
    JOIN city ON buildingsite.zip = city.zip AND buildingsite.city = city.name
    JOIN province ON city.province = province.shorthand
);


CREATE VIEW offerWithComputation AS (
    SELECT supplyoffer.code AS offer, computation.code AS computation, supplyoffer.totalprice, supplyoffer.totalresistance, supplyoffer.rows, supplyoffer.distance,
    computation.groundload, computation.roofload
    FROM supplyoffer JOIN computation ON supplyoffer.computationcode = computation.code
);

CREATE VIEW customerWithOfferAndPrice AS (
    SELECT customer.code AS customerCode, customer.name,
    offerforcustomer.offercode, supplyoffer.totalPrice, customer.discount,
    ROUND((supplyoffer.totalPrice - (supplyoffer.totalPrice / 100 * customer.discount)),2) AS netPrice
    FROM customer 
    JOIN offerforcustomer ON customer.code = offerforcustomer.customercode
    JOIN supplyoffer ON offerforcustomer.offercode = supplyoffer.code
    ORDER BY customer.name
);

COMMIT;
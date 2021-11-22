/* ===================================================================
-- Triggers Creation 
-- ================================================================ */

/* Calculate groundLoad attribute for computation. 
   The calculation is made by following the Italian norm
   on snowload calculation */
CREATE OR REPLACE FUNCTION calculateGroundLoadForComputation()
  RETURNS TRIGGER AS 
$$
    DECLARE altitudeBS NUMERIC;
    DECLARE zoneBS VARCHAR;
    DECLARE groundLoadBS NUMERIC;
    BEGIN

        altitudeBS = (SELECT altitude
                      FROM computationWithBSData
                      WHERE computationWithBSData.code = NEW.code);

        zoneBS = (SELECT zone
                  FROM computationWithBSData
                  WHERE computationWithBSData.code = NEW.code);

        /* Mathematical computation of ground load based on Italian norm */
        IF altitudeBS > 200 THEN 
            IF zoneBS = 'I-A' THEN
                groundLoadBS = ROUND((1.39 * (1 + POWER((altitudeBS / 728),2))),2);
            ELSEIF zoneBS = 'I-M' THEN
                groundLoadBS = ROUND((1.35 * (1 + POWER((altitudeBS / 602),2))),2);
            ELSEIF zoneBS = 'II' THEN
                groundLoadBS = ROUND((0.85 * (1 + POWER((altitudeBS / 481),2))),2);
            ELSEIF zoneBS = 'III' THEN
                groundLoadBS = ROUND((0.51 * (1 + POWER((altitudeBS / 481),2))),2);
            ELSE
                RAISE EXCEPTION 'Zone name % not recognized!!', zoneBS;
            END IF;
        ELSE
            groundLoadBS = (SELECT baseLoad
                            FROM computationWithBSData
                            WHERE computationWithBSData.code = NEW.code);
        END IF;

        UPDATE computation
        SET groundLoad = groundLoadBS
        WHERE NEW.code = computation.code;

        RETURN NEW;
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER calculateGroundLoadForComputation
  AFTER INSERT
  ON computation
  FOR EACH ROW
  EXECUTE PROCEDURE calculateGroundLoadForComputation();


/* Compute roofload based on the previously computed groundLoad
   the roofload depends on the steepness and length of the roof and
   on the grounload of where it is located */
CREATE OR REPLACE FUNCTION calculateRoofLoadForComputation()
  RETURNS TRIGGER AS 
$$  
    DECLARE steepnessBS NUMERIC;
    DECLARE roofLoadOnRoofSurfaceBS NUMERIC;
    DECLARE lengthBS NUMERIC;
    DECLARE horizontalLengthBS NUMERIC;
    DECLARE roofLoadBS NUMERIC;
    BEGIN
        steepnessBS = (SELECT steepness
                       FROM computationWithBSData
                       WHERE NEW.code = computationWithBSData.code);

        roofLoadOnRoofSurfaceBS = NEW.groundLoad * 0.8;

        lengthBS = (SELECT length
                       FROM computationWithBSData
                       WHERE NEW.code = computationWithBSData.code);

        horizontalLengthBS = lengthBS * COS(RADIANS(steepnessBS));

        roofLoadBS = roofLoadOnRoofSurfaceBS * horizontalLengthBS * SIN(RADIANS(steepnessBS));

        RETURN (NEW.code, NEW.date,NEW.groundLoad,ROUND(roofLoadBS,2));
    END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER calculateRoofLoadForComputation
  BEFORE UPDATE
  ON computation
  FOR EACH ROW
  EXECUTE PROCEDURE calculateRoofLoadForComputation();




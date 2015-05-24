DROP PROCEDURE CISDB.SPCISSELUSER;

GRANT EXECUTE ON PROCEDURE CISDB.SPCISSELUSER TO CIS_PORTAL_USER;

CREATE PROCEDURE CISDB.SPCISSELUSER(IN username VARCHAR(50))
BEGIN
DECLARE cur CURSOR FOR
SELECT
  FIIDUSER 'id',
  fcusername username,
  fcauthority authority,
  fcenabled enabled,
  fcname 'name',
  fclastname lastname,
  fcsex sex,
  fdbirth birth,
  fdregisterdate registerdate,
  fdlastupdate lastupdate
FROM
  cisdb.tacisuser
WHERE
fcusername = username;
END
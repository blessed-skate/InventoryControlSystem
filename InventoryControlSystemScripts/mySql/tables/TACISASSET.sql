DROP TABLE  CISDB.TACISASSET;

CREATE TABLE CISDB.TACISASSET 
( 
  FIIDASSET INT(10) NOT NULL AUTO_INCREMENT
, FIIDLEDGER INT(10) NOT NULL 
, FIIDASSETTYPE INT(3) NOT NULL 
, FCDESCRIPTION VARCHAR(100) NOT NULL 
, FCBRAND VARCHAR(50) NULL DEFAULT 'Sin marca' 
, FCMODEL VARCHAR(50) NULL DEFAULT 'Sin modelo' 
, FCSERIALNUMBER VARCHAR(50) NULL DEFAULT 'Sin numero de serie' 
, FIIDMATERIAL INT(3) NOT NULL 
, FIIDCOLOR INT(3) NOT NULL 
, FCSUPPLIER VARCHAR(50) NULL DEFAULT 'Proveedor no identificado' 
, FCGENERALMANAGER VARCHAR(100) NULL 
, FCDIRECTLYRESPONSIBLE VARCHAR(100) NULL 
, FITAG BIGINT(19) NULL 
, FCBILL VARCHAR(30) NULL DEFAULT 'Sin factura' 
, FDBILLINGDATE DATE NULL DEFAULT '2015-06-01' 
, FCLOCATION VARCHAR(50) NULL 
, FDUSEDATE DATE NULL DEFAULT '2015-06-01' 
, FNPRICE DECIMAL(10, 2) NOT NULL DEFAULT 0.00 
, FCGENERALLOCATION VARCHAR(50) NULL 
, FCSECURE VARCHAR(50) NULL 
, FDREGISTERDATE TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP 
, FDLASTUPDATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
, CONSTRAINT PRIMARY KEY ( FIIDASSET ) );

ALTER TABLE CISDB.TACISASSET ADD CONSTRAINT TACISASSET_FK1 FOREIGN KEY ( FIIDASSETTYPE )
 REFERENCES CISDB.CTCISASSETTYPE ( FIIDASSETTYPE )
 ON DELETE CASCADE;

ALTER TABLE CISDB.TACISASSET ADD CONSTRAINT TACISASSET_FK2 FOREIGN KEY ( FIIDCOLOR )
 REFERENCES CISDB.CTCISCOLOR ( FIIDCOLOR )
 ON DELETE CASCADE;

ALTER TABLE CISDB.TACISASSET ADD CONSTRAINT TACISASSET_FK3 FOREIGN KEY ( FIIDMATERIAL )
 REFERENCES CISDB.CTCISMATERIAL ( FIIDMATERIAL )
 ON DELETE CASCADE;

CREATE INDEX TACISASSET_INDEX1 ON CISDB.TACISASSET ( FIIDASSET ASC);
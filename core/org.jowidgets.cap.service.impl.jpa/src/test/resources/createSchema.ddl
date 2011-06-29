CREATE TABLE PERSON (ID BIGINT NOT NULL, BIRTHDAY DATE, NAME VARCHAR, POINTS INTEGER, TRISTATE BOOLEAN, VERSION BIGINT, PRIMARY KEY (ID))
CREATE TABLE JOB (ID BIGINT NOT NULL, SALARY INTEGER, TITLE VARCHAR, OWNER_ID BIGINT, VERSION BIGINT, PRIMARY KEY (ID))
ALTER TABLE JOB ADD CONSTRAINT FK_JOB_OWNER_ID FOREIGN KEY (OWNER_ID) REFERENCES PERSON (ID)
CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50) NOT NULL, SEQ_COUNT NUMERIC(38), PRIMARY KEY (SEQ_NAME))
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0)
CREATE VIEW V_PERSON AS SELECT * FROM PERSON
CREATE TRIGGER T_V_PERSON INSTEAD OF DELETE ON V_PERSON FOR EACH ROW CALL "org.jowidgets.cap.service.impl.jpa.h2.IgnoreTrigger"
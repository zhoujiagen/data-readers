# README

### t1

``` sql
CREATE TABLE `t1` (
  `col1` BIGINT NOT NULL AUTO_INCREMENT COMMENT '列1',
  `col2` VARCHAR(7000) NULL COMMENT '列2',
  PRIMARY KEY (`col1`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_bin
COMMENT = '表1';

INSERT INTO t1 SELECT NULL, REPEAT('a', 7000);

DELIMITER //
CREATE PROCEDURE get_t1()
BEGIN
    SELECT * FROM t1;
END //    
DELIMITER ;

CALL get_t1();


DELIMITER //
CREATE PROCEDURE load_t1(count INT UNSIGNED)
BEGIN
    DECLARE s INT UNSIGNED DEFAULT 1;
    DECLARE c VARCHAR(7000) DEFAULT REPEAT('a', 7000);
    WHILE s <= count DO
		INSERT INTO t1 SELECT NULL, c;
        SET s = s + 1;
    END WHILE;
END //
    
DELIMITER ;

CALL load_t1(60);
CALL load_t1(1);


```
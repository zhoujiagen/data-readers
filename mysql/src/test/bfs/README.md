# README

### t1.frm

``` sql
CREATE TABLE `t1` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `c1` varchar(45) COLLATE utf8mb4_bin NOT NULL COMMENT 'col 1',
  `c2` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'col 2',
  PRIMARY KEY (`id`),
  KEY `idx_c1` (`c1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='table test';
```
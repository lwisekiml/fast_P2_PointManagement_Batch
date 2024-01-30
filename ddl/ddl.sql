mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
4 rows in set (0.01 sec)

mysql> create database point;
Query OK, 1 row affected (0.01 sec)

mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| point              |
| sys                |
+--------------------+
5 rows in set (0.01 sec)

mysql> use point;
Database changed
mysql> CREATE TABLE `point_wallet` (
    ->  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    ->  `amount` bigint NOT NULL COMMENT ''
        -> ,
    ->  `user_id` varchar(20) NOT NULL COMMENT ' ID',
    ->  PRIMARY KEY (`id`)
    -> ) COMMENT '';
Query OK, 0 rows affected (0.03 sec)

mysql> CREATE TABLE `point_reservation` (
    ->  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    ->  `amount` bigint NOT NULL COMMENT ''
        -> ,
    ->  `available_days` int NOT NULL COMMENT ''
        -> ,
    ->  `earned_date` date NOT NULL COMMENT ''
        -> ,
    ->  `is_executed` tinyint NOT NULL COMMENT ''
        -> ,
    ->  `point_wallet_id` bigint NOT NULL COMMENT '  ID',
    ->  PRIMARY KEY (`id`)
    -> ) COMMENT ' ';
Query OK, 0 rows affected (0.03 sec)

mysql> CREATE TABLE `point`(
    ->  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    ->  `amount` bigint NOT NULL COMMENT ''
        -> ,
    ->  `earned_date` date NOT NULL COMMENT ''
        -> ,
    ->  `expire_date` date NOT NULL COMMENT ''
        -> ,
    ->  `is_used` tinyint NOT NULL COMMENT ''
        -> ,
    ->  `is_expired` tinyint NOT NULL COMMENT ''
        -> ,
    ->  `point_wallet_id` bigint NOT NULL COMMENT '  ID',
    ->  PRIMARY KEY (`id`)
    -> ) COMMENT '';
Query OK, 0 rows affected (0.03 sec)

mysql> CREATE TABLE `message` (
    ->  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    ->  `user_id` varchar(20) NOT NULL COMMENT ' ID',
    ->  `title` varchar(200) NOT NULL COMMENT ''
    -> ,
    ->  `content` text NOT NULL COMMENT ''
    -> ,
    ->  PRIMARY KEY (`id`)
    -> ) COMMENT '';
Query OK, 0 rows affected (0.03 sec)

mysql> show tables;
+-------------------+
| Tables_in_point   |
+-------------------+
| message           |
| point             |
| point_reservation |
| point_wallet      |
+-------------------+
4 rows in set (0.01 sec)

mysql> select * from point;
Empty set (0.01 sec)


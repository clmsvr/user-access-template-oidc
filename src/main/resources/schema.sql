-- https://docs.spring.io/spring-boot/how-to/data-initialization.html

set foreign_key_checks = 0;

drop table IF EXISTS User_has_role;
drop table IF EXISTS role_has_Permission;
drop table IF EXISTS User;
drop table IF EXISTS role;
drop table IF EXISTS Permission;

set foreign_key_checks = 1;

-- -----------------------------------------------------
-- Table User
-- -----------------------------------------------------
CREATE TABLE User (
  id BIGINT NOT NULL AUTO_INCREMENT,
  oidc_id VARCHAR(200) NOT NULL,
  provider_name VARCHAR(200) NOT NULL,
  email VARCHAR(100) NOT NULL,
  name VARCHAR(100) NOT NULL,
  city VARCHAR(100) NULL,
  state CHAR(2) NULL,
  num_blocks_subtitled INT NULL DEFAULT 0,
  num_blocks_translated INT NULL DEFAULT 0,
  comment TEXT NULL COMMENT 'descricao do proprio usuario.',
  creation_date DATETIME not null,
  update_date DATETIME not null,
  PRIMARY KEY (id),
  UNIQUE INDEX oidc_id_UNIQUE (oidc_id ASC) )
ENGINE = InnoDB default character set = utf8mb4;


-- -----------------------------------------------------
-- Table role
-- -----------------------------------------------------
CREATE TABLE role (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  description varchar(255),
  PRIMARY KEY (id))
ENGINE = InnoDB default character set = utf8mb4;

-- -----------------------------------------------------
-- Table User_has_role
-- -----------------------------------------------------
CREATE TABLE User_has_role (
  User_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (User_id, role_id),
  INDEX fk_User_has_role_role1_idx (role_id ASC) ,
  INDEX fk_User_has_role_User1_idx (User_id ASC) ,
  CONSTRAINT fk_User_has_role_User1
    FOREIGN KEY (User_id)
    REFERENCES User (id)
  ,
  CONSTRAINT fk_User_has_role_role1
    FOREIGN KEY (role_id)
    REFERENCES role (id)
  )
ENGINE = InnoDB default character set = utf8mb4;


-- -----------------------------------------------------
-- Table Permission
-- -----------------------------------------------------
CREATE TABLE Permission (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  description varchar(255),
  PRIMARY KEY (id))
ENGINE = InnoDB default character set = utf8mb4;


-- -----------------------------------------------------
-- Table role_has_Permission
-- -----------------------------------------------------
CREATE TABLE role_has_Permission (
  role_id BIGINT NOT NULL,
  Permission_id BIGINT NOT NULL,
  PRIMARY KEY (role_id, Permission_id),
  INDEX fk_role_has_Permission_Permission1_idx (Permission_id ASC) ,
  INDEX fk_role_has_Permission_role1_idx (role_id ASC) ,
  CONSTRAINT fk_role_has_Permission_role1
    FOREIGN KEY (role_id)
    REFERENCES role (id)
  ,
  CONSTRAINT fk_role_has_Permission_Permission1
    FOREIGN KEY (Permission_id)
    REFERENCES Permission (id)
  )
ENGINE = InnoDB default character set = utf8mb4;


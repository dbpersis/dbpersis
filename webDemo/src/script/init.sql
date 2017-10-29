-- -----------------------------------------------------
-- Table `User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `User` (
  `id` VARCHAR(30) NOT NULL,
  `username` VARCHAR(64) NULL,
  `pwd` VARCHAR(64) NULL,
  `sex` VARCHAR(64) NOT NULL,
  `age` INT NULL,
  `createdate` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `Students`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Students` (
  `id` VARCHAR(30) NOT NULL,
  `name` VARCHAR(64) NULL,
  `classid` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `Schoolclass`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Schoolclass` (
  `id` VARCHAR(30) NOT NULL,
  `name` VARCHAR(64) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


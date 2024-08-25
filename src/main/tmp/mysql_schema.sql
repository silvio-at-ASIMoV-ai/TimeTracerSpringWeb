CREATE TABLE IF NOT EXISTS Employees (
	ID INT auto_increment PRIMARY KEY,
    Name varchar(100) NOT NULL,
    Surname varchar(100) NOT NULL,
    BirthDate DATE NOT NULL,
    BirthPlace varchar(100) NOT NULL,
    SocialSecurityNum varchar(100) NOT NULL,
    Residence varchar(256) NOT NULL
);
CREATE TABLE IF NOT EXISTS Projects (
	ID BIGINT auto_increment PRIMARY KEY,
	ProjectName varchar(100) NOT NULL
);
CREATE TABLE IF NOT EXISTS Times (
    ID BIGINT auto_increment PRIMARY KEY,
    EmployeeID Integer NOT NULL REFERENCES Employees(id),
    ProjectID Integer NOT NULL REFERENCES Projects(id),
    PunchedTime TIMESTAMP NOT NULL,
    `in` BOOLEAN NOT NULL,
    InsertUser varchar(255) NOT NULL,
    InsertTimestamp TIMESTAMP NOT NULL,
    ModifyUser varchar(255) NOT NULL,
    ModifyTimestamp TIMESTAMP NOT NULL
);
CREATE TABLE IF NOT EXISTS Roles (
	ID BIGINT auto_increment PRIMARY KEY,
	RoleDesc varchar(100),
	IsAdmin BOOLEAN NOT NULL,
	ProjectId INTEGER
);
CREATE TABLE IF NOT EXISTS Users (
    UserName varchar(255) NOT NULL,
    Password varchar(128),
    ResetTime TIMESTAMP,
    RoleId Integer NOT NULL REFERENCES Roles(id),
    EmployeeId Integer REFERENCES Employees(id),
    PRIMARY KEY (UserName)
);
CREATE TABLE IF NOT EXISTS Logs (
	ID BIGINT auto_increment PRIMARY KEY,
    Operation varchar(100) NOT NULL,
    `Table` varchar(100) NOT NULL,
	NewValues text NOT NULL,
	OldValues text NOT NULL,
	undoId int DEFAULT NULL,
	`Timestamp` TIMESTAMP NOT NULL
);

INSERT INTO Roles (RoleDesc, IsAdmin) VALUES ('Admin', true);
INSERT INTO Roles (RoleDesc, IsAdmin) VALUES ('Employee', false);

INSERT INTO Employees (Name, Surname, BirthDate, BirthPlace, SocialSecurityNum, Residence)
VALUES ('Rocco', 'Matera', '1988-04-12', 'Borgo Cristina', 'MTRRCC88D20M260A', 'Incrocio Gregori 3, Sesto Italo');
INSERT INTO Users (UserName, ResetTime, RoleId, EmployeeId) VALUES ('Matera_Ro', CURRENT_TIMESTAMP(), 2, 1);
INSERT INTO Users (UserName, ResetTime, RoleId) VALUES ('Admin', CURRENT_TIMESTAMP(), 1);
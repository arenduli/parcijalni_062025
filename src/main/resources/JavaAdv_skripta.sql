CREATE DATABASE JavaAdv;
GO

USE JavaAdv;
GO

CREATE TABLE Polaznik (
    PolaznikID INT IDENTITY(1,1) PRIMARY KEY,
    Ime NVARCHAR(100) NOT NULL,
    Prezime NVARCHAR(100) NOT NULL
);

CREATE TABLE ProgramObrazovanja (
    ProgramObrazovanjaId INT IDENTITY(1,1) PRIMARY KEY,
    Naziv NVARCHAR(100) NOT NULL,
    CSVET INT NOT NULL
);

CREATE TABLE Upis (
    UpisId INT IDENTITY(1,1) PRIMARY KEY,
    IDProgramObrazovanja INT NOT NULL,
    IDPolaznik INT NOT NULL,
    FOREIGN KEY (IDProgramObrazovanja) REFERENCES ProgramObrazovanja(ProgramObrazovanjaId),
    FOREIGN KEY (IDPolaznik) REFERENCES Polaznik(PolaznikID)
);
GO

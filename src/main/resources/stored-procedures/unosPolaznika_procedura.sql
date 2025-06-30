CREATE PROCEDURE AddStudent
    @FirstName NVARCHAR(50),
    @LastName NVARCHAR(50)
AS
BEGIN
    INSERT INTO Polaznik (Ime, Prezime)
    VALUES (@FirstName, @LastName);
END;

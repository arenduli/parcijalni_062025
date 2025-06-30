CREATE PROCEDURE GetStudentsInProgram
    @ProgramID INT
AS
BEGIN
    SELECT p.Ime, p.Prezime, po.Naziv, po.CSVET
    FROM Upis u
    JOIN Polaznik p ON u.IDPolaznik = p.PolaznikID
    JOIN ProgramObrazovanja po ON u.IDProgramObrazovanja = po.ProgramObrazovanjaId
    WHERE po.ProgramObrazovanjaId = @ProgramID;
END;

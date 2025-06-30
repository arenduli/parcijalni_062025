CREATE PROCEDURE AddEducationProgram
    @ProgramName NVARCHAR(100),
    @CSVET INT
AS
BEGIN
    INSERT INTO ProgramObrazovanja (Naziv, CSVET)
    VALUES (@ProgramName, @CSVET);
END;

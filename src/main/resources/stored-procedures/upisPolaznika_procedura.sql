CREATE PROCEDURE EnrollStudent
    @StudentID INT,
    @ProgramID INT
AS
BEGIN
    INSERT INTO Upis (IDPolaznik, IDProgramObrazovanja)
    VALUES (@StudentID, @ProgramID);
END;

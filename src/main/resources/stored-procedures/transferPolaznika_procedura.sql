CREATE PROCEDURE TransferStudent
    @StudentID INT,
    @CurrentProgramID INT,
    @NewProgramID INT
AS
BEGIN
    BEGIN TRANSACTION;

    BEGIN TRY
        DELETE FROM Upis
        WHERE IDPolaznik = @StudentID AND IDProgramObrazovanja = @CurrentProgramID;

        INSERT INTO Upis (IDPolaznik, IDProgramObrazovanja)
        VALUES (@StudentID, @NewProgramID);

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        THROW;
    END CATCH;
END;

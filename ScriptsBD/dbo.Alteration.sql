/****** Object:  StoredProcedure [dbo].[Alteration]    Script Date: 21/11/2018 01:30:15 ******/
DROP PROCEDURE [dbo].[Alteration]
GO

/****** Object:  StoredProcedure [dbo].[Alteration]    Script Date: 21/11/2018 01:30:15 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:      <Author, , Name>
-- Create Date: <Create Date, , >
-- Description: <Description, , >
-- =============================================
CREATE PROCEDURE [dbo].[Alteration]
(
	@UserID int,
	@Tipo NVARCHAR(50),
    @Nome NVARCHAR(50),
	@Alterado NVARCHAR(50),
	@Alteracao NVARCHAR(100),
	@Erro int OUTPUT,
	@resultMessage VARCHAR(1000) OUTPUT
)
AS
BEGIN
	SET @Erro = -1;
	SET @resultMessage = '';

	IF @Tipo = 'musica'
	BEGIN
		DECLARE @MusicaID INT;
		SET @MusicaID = -1;
		
		SELECT @MusicaID = MusInf.MusicInfoID
		FROM dbo.MusicInfo MusInf
		WHERE MusInf.Titulo =  @Nome
		
		IF @Alterado = 'titulo'
		BEGIN
			UPDATE dbo.MusicInfo
				SET Titulo = @Alteracao
				WHERE MusicInfoID = @MusicaID
		END

		IF @Alterado = 'genero'
		BEGIN
			DECLARE @GeneroID INT;
			SET @GeneroID = -1;

			SELECT @GeneroID = Gen.GenreID
			FROM dbo.Genre Gen
			WHERE Gen.Name = @Alteracao

			UPDATE dbo.MusicInfo
				SET Genero = @GeneroID
				WHERE MusicInfoID = @MusicaID
		END
		
	END

	IF @Tipo = 'album'
	BEGIN
		DECLARE @AlbumID INT;
		SET @AlbumID = -1;

		SELECT @AlbumID = AlbInf.AlbumInfoID
		FROM dbo.AlbumInfo AlbInf
		WHERE AlbInf.Name = @Nome

		IF @Alterado = 'nome'
		BEGIN
			UPDATE dbo.AlbumInfo
				SET Name = @Alteracao
				WHERE AlbumInfoID = @AlbumID
		END

		IF @Alterado = 'ano'
		BEGIN
			UPDATE dbo.AlbumInfo
				SET Year = CAST(@Alteracao AS INT)
				WHERE AlbumInfoID = @AlbumID
		END
	END

	IF @Tipo = 'artista'
	BEGIN
		DECLARE @ArtistaID INT;
		SET @ArtistaID = -1;

		SELECT @ArtistaID = Art.ArtistID
		FROM dbo.Artist Art
		WHERE Art.Name = @Nome

		IF @Alterado = 'nome'
		BEGIN 
			UPDATE dbo.Artist
			SET Name = @Alteracao
			WHERE ArtistID = @ArtistaID
		END
	END


	SET @resultMessage = ' ; notificate | none';
	
	SELECT @resultMessage = ' ; notificate | '+ UserDB.username + @resultMessage
	FROM dbo.Historicoo Hist, dbo.Users UserDB 
	WHERE Hist.Tipo = @Tipo AND Hist.Nome = @Nome AND Hist.UsernameID = UserDB.UserID AND Hist.UsernameID != @UserID

	INSERT INTO dbo.Historicoo
	(
		Tipo,
		Nome,
		UsernameID
	)
	VALUES
	(
		@Tipo,
		@Nome,
		@UserID
	)

	SET @Erro = 1;
	RETURN;
END
GO



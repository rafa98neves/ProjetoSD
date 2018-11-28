/****** Object:  StoredProcedure [dbo].[Criar]    Script Date: 21/11/2018 01:30:46 ******/
DROP PROCEDURE [dbo].[Criar]
GO

/****** Object:  StoredProcedure [dbo].[Criar]    Script Date: 21/11/2018 01:30:46 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:      <Author, , Name>
-- Create Date: <Create Date, , >
-- Description: <Description, , >
-- =============================================
CREATE PROCEDURE [dbo].[Criar]
(
	@tipo NVARCHAR(50),
	@nome NVARCHAR(50),
	@info NVARCHAR(100),
	@info2 NVARCHAR(100),
	@Erro int OUTPUT,
	@Description NVARCHAR(1000) OUTPUT
)
AS
BEGIN
    SET @Erro = -10;
	SET @Description = 'false';
	DECLARE @aux INT;
	SET @aux = -1;
	
	IF @tipo = 'artista'
	BEGIN
		IF @info = 'true'
			BEGIN
				SET @aux = 1;
			END
		IF @info = 'false'
			BEGIN
				SET @aux = 0;
			END
		
		IF @aux != -1
		BEGIN
			INSERT INTO dbo.Artist
			(
				Name,
				Composer
			)
			VALUES
			(
				@nome,
				@aux
			)
			SET @Erro = 1;
			SET @Description = 'true';
		END
		RETURN
	END	
	
	IF @tipo = 'album'
	BEGIN
		
		BEGIN
			INSERT INTO dbo.Albuminfo
			(
				Name,
				Year
			)
			VALUES
			(
				@nome,
				cast(@info AS int)
			)
			SET @Erro = 1;
			SET @Description = 'true';
		END
		RETURN
	END	
	
	IF @tipo = 'musica'
	BEGIN
		DECLARE @GenreID INT;
		SET @GenreID = -1;
		
		SELECT @GenreID = Findgen.GenreID
		FROM dbo.Genre Findgen
		WHERE Findgen.Name = @info2
		
		INSERT INTO dbo.MusicInfo
		(
			Titulo,
			Genero
		)
		VALUES
		(
			@nome,
			@GenreID
		)
		
		DECLARE @MusicID INT;
		SET @MusicID = -1;
		
		SELECT @MusicID = Findmusic.MusicInfoID
		FROM dbo.MusicInfo Findmusic
		WHERE Findmusic.Titulo = @nome
		
		
		DECLARE @ArtistID INT;
		SET @ArtistID = -1;
		
		SELECT @ArtistID = Findartist.ArtistID
		FROM dbo.Artist Findartist
		WHERE Findartist.Name = @info
		
		INSERT INTO dbo.Music
		(
			Musicinfo,
			Artist
		)
		VALUES
		(
			@MusicID,
			@ArtistID
		)
		
		SET @Erro = 1;
		SET @Description = 'true';
		RETURN
	END	
	RETURN	
END
GO



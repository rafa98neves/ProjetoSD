/****** Object:  StoredProcedure [dbo].[Search]    Script Date: 21/11/2018 01:35:20 ******/
DROP PROCEDURE [dbo].[Search]
GO

/****** Object:  StoredProcedure [dbo].[Search]    Script Date: 21/11/2018 01:35:20 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:      <Author, , Name>
-- Create Date: <Create Date, , >
-- Description: <Description, , >
-- =============================================
CREATE PROCEDURE [dbo].[Search]
(
	@Nome NVARCHAR(50),
	@TipoSearch NVARCHAR(50),
	@Erro int OUTPUT,
	@resultMessage VARCHAR(1000) OUTPUT
)
AS
BEGIN
    SET @Erro = -1;
	SET @resultMessage = '';

	IF @TipoSearch = 'album'
	BEGIN
		SELECT @resultMessage = @resultMessage + ' ; album | ' + AlbInf.Name
		FROM dbo.AlbumInfo AlbInf
		WHERE AlbInf.Name LIKE '%'+@Nome+'%'
		
		IF @@ROWCOUNT > 0
		BEGIN
			SET @Erro = 1;
			RETURN;
		END
		ELSE
		BEGIN
			SET @resultMessage = ' ; album | none';
			SET @Erro = -1;
			RETURN;
		END
	END

	IF @TipoSearch = 'musica'
	BEGIN
		SELECT @resultMessage = @resultMessage + ' ; musica | ' + MusInf.Titulo
		FROM dbo.MusicInfo MusInf
		WHERE MusInf.Titulo LIKE '%'+@Nome+'%'
		
		IF @@ROWCOUNT > 0
		BEGIN
			SET @Erro = 2;
			RETURN;
		END
		ELSE
		BEGIN
			SET @resultMessage = ' ; musica | none';
			SET @Erro = -1;
			RETURN;
		END
	END

	IF @TipoSearch = 'genero'
	BEGIN
		SELECT @resultMessage = @resultMessage + ' ; genero | ' + GenInf.Name
		FROM dbo.Genre GenInf
		WHERE GenInf.Name LIKE '%'+@Nome+'%'

		IF @@ROWCOUNT > 0
		BEGIN
			SET @Erro = 3;
			RETURN;
		END
		ELSE
		BEGIN
			SET @resultMessage = ' ; genero | none';
			SET @Erro = -1;
			RETURN;
		END
	END

	IF @TipoSearch = 'artista'
	BEGIN
		SELECT @resultMessage = @resultMessage + ' ; artist | ' + ArtInf.Name
		FROM dbo.Artist ArtInf
		WHERE ArtInf.Name LIKE '%'+@Nome+'%'

		IF @@ROWCOUNT > 0
		BEGIN
			SET @Erro = 4;
			RETURN;
		END
		ELSE
		BEGIN
			SET @resultMessage = ' ; artist | none';
			SET @Erro = -1;
			RETURN;
		END
	END
END
GO



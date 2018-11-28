/****** Object:  StoredProcedure [dbo].[AddGenero]    Script Date: 21/11/2018 01:28:05 ******/
DROP PROCEDURE [dbo].[AddGenero]
GO

/****** Object:  StoredProcedure [dbo].[AddGenero]    Script Date: 21/11/2018 01:28:05 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:      <Author, , Name>
-- Create Date: <Create Date, , >
-- Description: <Description, , >
-- =============================================
CREATE PROCEDURE [dbo].[AddGenero]
(
	@Genre NVARCHAR(50),
	@Erro int OUTPUT,
	@Description VARCHAR(1000) OUTPUT
)
AS
BEGIN
	SET @Erro = -10;
	SET @Description = 'false';
	
	DECLARE @countGenre INT;
	SET @countGenre = -1;
	
	SELECT @countGenre = COUNT(*)
	FROM dbo.Genre Genero
	WHERE Genero.Name = @Genre
	
	IF @countGenre = 0
	BEGIN
		INSERT INTO dbo.Genre
		(
			Name
		)
		VALUES
		(
			@Genre
		)
		SET @Erro = 0;
		SET @Description = 'true';
		RETURN;
	END
	RETURN;	
END
GO



/****** Object:  StoredProcedure [dbo].[WriteCritic]    Script Date: 21/11/2018 01:35:36 ******/
DROP PROCEDURE [dbo].[WriteCritic]
GO

/****** Object:  StoredProcedure [dbo].[WriteCritic]    Script Date: 21/11/2018 01:35:36 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:      <Author, , Name>
-- Create Date: <Create Date, , >
-- Description: <Description, , >
-- =============================================
CREATE PROCEDURE [dbo].[WriteCritic]
(
	@User int,
	@Album NVARCHAR(100),
	@PONT int,
	@Critica NVARCHAR(300),
	@Erro int OUTPUT,
	@Description NVARCHAR(1000) OUTPUT
)
AS
BEGIN

	SET @Erro = -10;
	SET @Description = 'negado';
	
	BEGIN
		DECLARE @AlbumID INT;
		SET @AlbumID = -1;
		
		SELECT @AlbumID = Findalbum.AlbumInfoID
		FROM dbo.AlbumInfo Findalbum
		WHERE Findalbum.Name = @Album
		
		INSERT INTO dbo.Review
		(
			UsernameID,
			Album,
			Comment,
			Score
		)
		VALUES
		(
			@User,
			@AlbumID,
			@Critica,
			@PONT
		)
		SET @Erro = 1;
		SET @Description = 'escrito';
		RETURN
	END
	RETURN
END
GO



/****** Object:  StoredProcedure [dbo].[Privileges]    Script Date: 21/11/2018 01:34:37 ******/
DROP PROCEDURE [dbo].[Privileges]
GO

/****** Object:  StoredProcedure [dbo].[Privileges]    Script Date: 21/11/2018 01:34:37 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:      <Author, , Name>
-- Create Date: <Create Date, , >
-- Description: <Description, , >
-- =============================================
CREATE PROCEDURE [dbo].[Privileges]
(
	@User NVARCHAR(50),
	@EDITOR BIT,
	@Erro int OUTPUT,
	@Description NVARCHAR(1000) OUTPUT
)
AS
BEGIN
	SET @Erro = -10;
	SET @Description = 'negado';
	
		
	DECLARE @UserID INT;
	SET @UserID = -1;
		
	DECLARE @auxEditor BIT;
	SET @auxEditor = 0;

	SELECT @UserID = Usr.UserID, @auxEditor = Usr.admin
	FROM dbo.Users Usr
	WHERE Usr.username = @User

	IF @auxEditor = 0
	BEGIN
		SET @auxEditor = 1
	END

	IF @auxEditor = 1
	BEGIN
		SET @auxEditor = 0
	END

	UPDATE dbo.Users
	SET admin = @auxEditor
	WHERE UserID = @UserID
		
	SET @Erro = 1;
	SET @Description = 'done';
	RETURN;	
END
GO



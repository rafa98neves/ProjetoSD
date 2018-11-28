/****** Object:  StoredProcedure [dbo].[Login]    Script Date: 21/11/2018 01:34:19 ******/
DROP PROCEDURE [dbo].[Login]
GO

/****** Object:  StoredProcedure [dbo].[Login]    Script Date: 21/11/2018 01:34:19 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:      <Author, , Name>
-- Create Date: <Create Date, , >
-- Description: <Description, , >
-- =============================================
CREATE PROCEDURE [dbo].[Login]
(
    @User NVARCHAR(50),
	@Password NVARCHAR(50),
	@Erro int OUTPUT,
	@Description VARCHAR(1000) OUTPUT,
	@UserID int OUTPUT,
	@UserEditor bit OUTPUT
)
AS
BEGIN
	SET @Erro = -10;
	SET @Description = 'Ok';
	SET @UserID = -1;
	SET @UserEditor = 0;
	
	SELECT @UserID = UsersTa.UserID, @UserEditor = UsersTa.admin 
	FROM dbo.Users UsersTa 
	WHERE UsersTa.username = @User AND UsersTa.password = @Password;

	IF @UserID < 0
	BEGIN
		SET @Erro = -1;
		SET @Description = 'dbo.Login: Dados de acesso não estão corretos';
		RETURN;
	END

	IF @UserID >= 0 
	BEGIN
		SET @Erro = 0;
		SET @Description = 'dbo.Login: Login efectuado';
		RETURN;
	END
END
GO



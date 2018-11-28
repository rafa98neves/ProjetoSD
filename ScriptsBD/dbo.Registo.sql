/****** Object:  StoredProcedure [dbo].[Registo]    Script Date: 21/11/2018 01:34:58 ******/
DROP PROCEDURE [dbo].[Registo]
GO

/****** Object:  StoredProcedure [dbo].[Registo]    Script Date: 21/11/2018 01:34:58 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[Registo]
	@User NVARCHAR(50),
	@Password NVARCHAR(50),
	@Erro int OUTPUT,
	@Description VARCHAR(1000) OUTPUT,
	@UserID int OUTPUT,
	@UserEditor bit OUTPUT
AS
BEGIN
	SET @Erro = -10;
	SET @Description = 'Ok';
	SET @UserID = -1;
	SET @UserEditor = 0;

	DECLARE @countUser INT; SET @countUser = -1;
	SELECT @countUser = COUNT(*) 
	FROM dbo.Users UsersTa 
	WHERE UsersTa.username = @User

	IF @countUser = 0
	BEGIN
		BEGIN TRY
			DECLARE @countUserTotal INT; SET @countUserTotal = -1;
			SELECT @countUserTotal = COUNT(*)
			FROM dbo.Users

			IF @countUserTotal = 0
			BEGIN
				INSERT INTO dbo.Users
				(
					username,
					password,
					admin
				)
				VALUES
				(
					@User,
					@Password,
					1
				)
			END

			IF @countUserTotal > 0
			BEGIN
				INSERT INTO dbo.Users
				(
					username,
					password,
					admin
				)
				VALUES
				(
					@User,
					@Password,
					0
				)
			END

			SELECT @UserID = UsersTa.UserID, @UserEditor = UsersTa.admin 
			FROM dbo.Users UsersTa 
			WHERE UsersTa.username = @User AND UsersTa.password = @Password;

			SET @Erro = 0;
			SET @Description = 'dbo.Registo: User foi adicionado à tabela dbo.Users';
			RETURN;
		END TRY
		BEGIN CATCH
			SET @Erro = -1;
			SET @Description = 'dbo.Registo: Erro no SQL ao adicionar novo User à tabela dbo.Users';
			RETURN;
		END CATCH
	END

	IF @countUser > 0
	BEGIN
		SET	@Erro = -2;
		SET @Description = 'dbo.Registo: User já existe';
		RETURN;
	END

	SET @Erro = -3;
	SET @Description = 'dbo.Registo: Erro no SQL ao aceder à tabela dbo.Users';
	RETURN;
END
GO



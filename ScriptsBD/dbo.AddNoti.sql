/****** Object:  StoredProcedure [dbo].[AddNoti]    Script Date: 21/11/2018 01:29:14 ******/
DROP PROCEDURE [dbo].[AddNoti]
GO

/****** Object:  StoredProcedure [dbo].[AddNoti]    Script Date: 21/11/2018 01:29:14 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:      <Author, , Name>
-- Create Date: <Create Date, , >
-- Description: <Description, , >
-- =============================================
CREATE PROCEDURE [dbo].[AddNoti]
(
    @Username NVARCHAR(50),
	@NotificationMessage VARCHAR(1000),
	@Erro int OUTPUT,
	@Description VARCHAR(1000) OUTPUT
)
AS
BEGIN
	DECLARE @UserID_Noti INT; SET @UserID_Noti = -1;
	SET @Erro = -1;
	SET @Description ='error';

	SELECT @UserID_Noti = dboUser.UserID
	FROM dbo.Users dboUser
	WHERE dboUser.username = @Username
    
	IF @UserID_Noti >= 0
	BEGIN
		INSERT INTO dbo.Notification
		(
			UserID,
			Message
		)
		VALUES
		(
			@UserID_Noti,
			@NotificationMessage
		)

		SET @Erro = 0;
		SET @Description ='add';
		RETURN;
	END

	RETURN;
END


GO



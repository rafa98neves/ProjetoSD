/****** Object:  StoredProcedure [dbo].[Notifications]    Script Date: 21/11/2018 01:34:29 ******/
DROP PROCEDURE [dbo].[Notifications]
GO

/****** Object:  StoredProcedure [dbo].[Notifications]    Script Date: 21/11/2018 01:34:29 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:      <Author, , Name>
-- Create Date: <Create Date, , >
-- Description: <Description, , >
-- =============================================
CREATE PROCEDURE [dbo].[Notifications]
(
    @User int,
	@resultMessage varchar(1000) OUTPUT
)
AS
BEGIN
	DECLARE @NumNotif int;

    SELECT @NumNotif = COUNT(*)
	FROM dbo.Notification Notif
	WHERE Notif.UserID = @User;
	
	IF(@NumNotif > 0)
	BEGIN
		SET @resultMessage = '';
	END
	ELSE
	BEGIN
		SET @resultMessage = ' ; notification | none';
		RETURN;
	END

    SELECT @resultMessage = (@resultMessage + ' ; notification | ' + Notif.Message)
	FROM dbo.Notification Notif
	WHERE Notif.UserID = @User;

	DELETE FROM dbo.Notification
	WHERE dbo.Notification.UserID = @User;

	RETURN;
END
GO



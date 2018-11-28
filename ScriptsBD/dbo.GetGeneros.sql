/****** Object:  StoredProcedure [dbo].[GetGeneros]    Script Date: 21/11/2018 01:31:59 ******/
DROP PROCEDURE [dbo].[GetGeneros]
GO

/****** Object:  StoredProcedure [dbo].[GetGeneros]    Script Date: 21/11/2018 01:31:59 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:      <Author, , Name>
-- Create Date: <Create Date, , >
-- Description: <Description, , >
-- =============================================
CREATE PROCEDURE [dbo].[GetGeneros]
(
	@Erro int OUTPUT,
	@Description VARCHAR(1000) OUTPUT
)
AS
BEGIN
    Set @Erro = 0;
	Set @Description = ' ; ';
    SELECT @Description = @Description + Gen.Name + ' | '
	FROM dbo.Genre Gen
	Return;
END
GO



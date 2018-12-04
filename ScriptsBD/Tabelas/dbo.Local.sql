SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Local](
	[LocalID] [int] IDENTITY(1,1) NOT NULL,
	[Cidade] [nvarchar](50) NOT NULL,
	[Pais] [nvarchar](50) NOT NULL
) ON [PRIMARY]
GO



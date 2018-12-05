SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Upload](
	[UploadID] [int] IDENTITY(1,1) NOT NULL,
	[Ficheiro] [nvarchar](100) NOT NULL,
	[MusicaID] [int] NOT NULL,
	[UserID] [int] NOT NULL
) ON [PRIMARY]
GO



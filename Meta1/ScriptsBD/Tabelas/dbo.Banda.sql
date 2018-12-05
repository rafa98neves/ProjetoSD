SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Banda](
	[BandaID] [int] IDENTITY(1,1) NOT NULL,
	[Nome] [nvarchar](50) NOT NULL,
	[DataCriacao] [nvarchar](50) NULL,
	[DataFim] [nvarchar](50) NULL,
	[Historia] [text] NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO


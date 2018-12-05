SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Concerto](
	[ConcertoID] [int] IDENTITY(1,1) NOT NULL,
	[Data] [date] NOT NULL,
	[Preco] [float] NULL,
	[LocalID] [int] NOT NULL
) ON [PRIMARY]
GO


SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Concerto_Banda](
	[Concerto_BandaID] [int] IDENTITY(1,1) NOT NULL,
	[ConcertoID] [int] NOT NULL,
	[BandaID] [int] NOT NULL
) ON [PRIMARY]
GO



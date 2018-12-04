SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Concerto_Artista](
	[Concerto_ArtistaID] [int] IDENTITY(1,1) NOT NULL,
	[ConcertoID] [int] NOT NULL,
	[ArtistaID] [int] NOT NULL
) ON [PRIMARY]
GO



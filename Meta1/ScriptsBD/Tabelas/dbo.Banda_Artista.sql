SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Banda_Artista](
	[Banda_ArtistaID] [int] IDENTITY(1,1) NOT NULL,
	[BandaID] [int] NOT NULL,
	[ArtistaID] [int] NOT NULL
) ON [PRIMARY]
GO



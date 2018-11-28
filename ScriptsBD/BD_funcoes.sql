--Criar Genero
INSERT INTO dbo.Genre
(
	Name
)
VALUES
(
	'@Genre'
)


--Criar Notificação
INSERT INTO dbo.Notification
(
	UserID,
	Message
)
VALUES
(
	(SELECT dboUser.UserID
		FROM dbo.Users dboUser
		WHERE dboUser.username = '@Username'),
	'@TextoMensagem'
)


--Associar Música a Artista
INSERT INTO dbo.Music
(
	MusicInfo,
	Artist
)
VALUES
(
	(SELECT MusInf.MusicInfoID
		FROM dbo.MusicInfo MusInf
		WHERE MusInf.Titulo =  '@NomeMusica'),
	(SELECT Art.ArtistID
		FROM dbo.Artist Art
		WHERE Art.Name = '@NomeArtista')
)


--Associar Música a Album
INSERT INTO dbo.Album
(
	AlbumInfo,
	Music
)
VALUES
(
	(SELECT AlbInf.AlbumInfoID
		FROM dbo.AlbumInfo AlbInf
		WHERE AlbInf.Name = '@NomeAlbum'),
	(SELECT MusInf.MusicInfoID
		FROM dbo.MusicInfo MusInf
		WHERE MusInf.Titulo = '@NomeMusica')
)


--Selecionar quais os Users que têm de receber notificação de alteração de informação
-- @Tipo = album||musica..., @Nome = nome música/album, @UserID = ID do utilizador que fez a alteração
SELECT UserDB.username
	FROM dbo.Historicoo Hist, dbo.Users UserDB 
	WHERE Hist.Tipo = '@Tipo' AND Hist.Nome = '@Nome' AND Hist.UsernameID = UserDB.UserID AND Hist.UsernameID != '@UserID'


--Alterar Título de uma música
-- @Alteracao = Nome a ser introduzido, @Nome =  Nome da música que vai sofrer a alteração
UPDATE dbo.MusicInfo
	SET Titulo = '@Alteracao'
	WHERE MusicInfoID = (SELECT MusInf.MusicInfoID
		FROM dbo.MusicInfo MusInf
		WHERE MusInf.Titulo =  '@Nome')

		
--Alterar Genero de uma música
-- @Alteracao = Nome do género que vai ser introduzido, @Nome = Nome da música que vai sofrer a alteração
UPDATE dbo.MusicInfo
	SET Genero = (SELECT Gen.GenreID
			FROM dbo.Genre Gen
			WHERE Gen.Name = '@Alteracao')
	WHERE MusicInfoID = (SELECT MusInf.MusicInfoID
		FROM dbo.MusicInfo MusInf
		WHERE MusInf.Titulo =  '@Nome')
		

--Alterar Nome de um Album
-- @Alteracao = Nome do album que vai ser introduzido, @Nome = Nome do álbum que vai sofrer a alteração
UPDATE dbo.AlbumInfo
	SET Name = '@Alteracao'
	WHERE AlbumInfoID = (SELECT AlbInf.AlbumInfoID
		FROM dbo.AlbumInfo AlbInf
		WHERE AlbInf.Name = '@Nome')
		

--Alterar Ano de um Album
-- @Alteracao = Ano do album que vai ser introduzido, @Nome = Nome do álbum que vai sofrer a alteração
UPDATE dbo.AlbumInfo
	SET Year = CAST('@Alteracao' AS INT)
	WHERE AlbumInfoID = (SELECT AlbInf.AlbumInfoID
		FROM dbo.AlbumInfo AlbInf
		WHERE AlbInf.Name = '@Nome')


--Alterar Nome de um Artista
-- @Alteracao = Nome do artista que vai ser introduzido, @Nome = Nome do artista que vai sofrer a alteração
UPDATE dbo.Artist
	SET Name = '@Alteracao'
	WHERE ArtistID = (SELECT Art.ArtistID
		FROM dbo.Artist Art
		WHERE Art.Name = '@Nome')

		
--Criar Artista
--@nome = Nome do artista, @info = Idade do Artista
INSERT INTO dbo.Artist
(
	Name,
	Idade
)
VALUES
(
	'@nome',
	cast('@info' AS int)
)


--Criar Album
--@nome = Nome do Album, @info = Ano do Album, @info2 = Descrição do álbum, @info3 = Editora responsável pelo álbum
INSERT INTO dbo.Albuminfo
(
	Name,
	Year,
	Description,
	Editora
)
VALUES
(
	'@nome',
	cast('@info' AS int),
	'@info2',
	'@info3'
)


--Criar Música e Associar a Artista
--@nome = Nome da Música, @info = Nome do Artista a que vai ficar associada a música
--@info2 = Nome do género a que a música vai ficar associada, @info3 = Duração da música,
--@info4 = Letra da música, @info5 = ID da banda a que a música vai ficar associada( < 0 caso seja apenas artista)
--@info6 = true/false com a informação se a pessoa introduzida é músico desta mesma música ou não
--@info7 = true/false com a informação se a pessoa introduzida é compositora desta mesma música ou não

INSERT INTO dbo.MusicInfo
(
	Titulo,
	Genero,
	Duracao,
	Letra,
	BandaID
)
VALUES
(
	'@nome',
	(SELECT Findgen.GenreID
		FROM dbo.Genre Findgen
		WHERE Findgen.Name = '@info2'),
	'@info3',
	'@info4',
	'@info5'
)

IF @info5 < 0
BEGIN
	INSERT INTO dbo.Music
	(
		Musicinfo,
		Artist,
		Musico,
		Compositor
	)
	VALUES
	(
		(SELECT Findmusic.MusicInfoID
			FROM dbo.MusicInfo Findmusic
			WHERE Findmusic.Titulo = '@nome'),
		(SELECT Findartist.ArtistID
			FROM dbo.Artist Findartist
			WHERE Findartist.Name = '@info'),
		'@info6',
		'@info7'
	)
END


--Começar ficheiro dbo.Details
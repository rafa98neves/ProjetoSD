

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

IF '@info5' < 0
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

--Criar Banda
--@nome = Nome da banda, @info = Ano Criação, @info2 = Ano Fim, @info3 = História da Banda
INSERT INTO dbo.Banda
	(
		Nome,
		DataCriacao,
		DataFim,
		Historia
	)
	VALUES
	(
		'@nome',
		'@info',
		'@info2',
		'@info3'
	)

--Obter detalhes de um Álbum
--@Nome = Nome do Album
	--Obter Cabeçalho de um Album
	SELECT AlbInf.Name, AlbInf.Year
	FROM dbo.AlbumInfo AlbInf
	WHERE AlbInf.AlbumInfoID = (SELECT AlbInf.AlbumInfoID
			FROM dbo.AlbumInfo AlbInf
			WHERE AlbInf.Name = '@Nome')

			
	--Obter Artistas de todas as músicas presentes num albúm
	SELECT Art.Name
	FROM dbo.Artist Art
	WHERE Art.ArtistID IN (
			SELECT Mus.Artist
			FROM dbo.Music Mus
			WHERE Mus.MusicInfo IN ( 
				SELECT MusInf.MusicInfoID
				FROM dbo.MusicInfo MusInf, dbo.Album Alb
				WHERE Alb.AlbumInfo = (SELECT AlbInf.AlbumInfoID
					FROM dbo.AlbumInfo AlbInf
					WHERE AlbInf.Name = '@Nome') 
				AND Alb.Music = MusInf.MusicInfoID
				)
			)

			
	--Obter nomes das músicas presentes num album
	SELECT MusInf.Titulo
	FROM dbo.MusicInfo MusInf, dbo.Album Alb
	WHERE Alb.AlbumInfo = (SELECT AlbInf.AlbumInfoID
			FROM dbo.AlbumInfo AlbInf
			WHERE AlbInf.Name = '@Nome') 
		AND Alb.Music = MusInf.MusicInfoID
		
		
	--Obter avaliações inseridas pelos Users relacionadas com o Álbum em questão
	SELECT Usr.username, Rev.Score, Rev.Comment
	FROM dbo.Review Rev, dbo.Users Usr
	WHERE Rev.UsernameID = Usr.UserID AND Rev.Album = (SELECT AlbInf.AlbumInfoID
			FROM dbo.AlbumInfo AlbInf
			WHERE AlbInf.Name = '@Nome')
		

--Obter detalhes de uma música
--@Nome = Nome da Música a pesquisar

	--Obter cabeçalho de uma música
	SELECT MusInf.Titulo, Gen.Name
	FROM dbo.MusicInfo MusInf, dbo.Genre Gen
	WHERE MusInf.MusicInfoID = (SELECT MusInf.MusicInfoID
		FROM dbo.MusicInfo MusInf
		WHERE MusInf.Titulo LIKE '%'+'@Nome'+'%')
	AND MusInf.Genero = Gen.GenreID
	
	--Obter artistas de uma música
	SELECT Art.Name
	FROM dbo.Artist Art, dbo.Music Mus
	WHERE Mus.MusicInfo = (SELECT MusInf.MusicInfoID
		FROM dbo.MusicInfo MusInf
		WHERE MusInf.Titulo LIKE '%'+'@Nome'+'%') 
	AND Mus.Artist = Art.ArtistID

	
--Obter detalhes de um género
--@Nome = Nome do genero a pesquisar

	--Obter cabeçalho do genero
	SELECT Gen.Name
	FROM dbo.Genre Gen
	WHERE Gen.Name LIKE '%'+'@Nome'+'%'
	
	--Obter músicas que têm este genero definido
	SELECT MusInf.Titulo
	FROM dbo.MusicInfo MusInf
	WHERE MusInf.Genero = (SELECT Gen.GenreID
		FROM dbo.Genre Gen
		WHERE Gen.Name LIKE '%'+'@Nome'+'%')
	
	
--Obter detalhes de um artista
--@Nome = Nome do artista a pesquisar

	--
	SELECT ArtInf.Name, ArtInf.Idade
	FROM dbo.Artist ArtInf
	WHERE ArtInf.Name LIKE '%'+'@Nome'+'%'
		
		
-- GET Albuns de um Artista
SELECT AlbInf.Name
FROM dbo.AlbumInfo AlbInf, dbo.Album Alb
WHERE AlbInf.AlbumInfoID = Alb.AlbumInfo AND Alb.Music IN (
	SELECT DISTINCT Mus.MusicInfo
	FROM dbo.Music Mus
	WHERE Mus.Artist = ( SELECT ArtInf.ArtistID
		FROM dbo.Artist ArtInf
		WHERE ArtInf.Name LIKE '%'+'@Nome'+'%'
		)
	)
GROUP BY AlbInf.Name


--Returnar todos os generos existentes

SELECT Gen.Name FROM dbo.Genre Gen


--Verificar se utilizador existe na base de dados para fazer Login
--@User = username inserido
--@Password = password inserida

SELECT UsersTa.UserID, UsersTa.admin 
	FROM dbo.Users UsersTa 
	WHERE UsersTa.username = '@User' AND UsersTa.password = '@Password';
	
	
	
--Adicionar ao Histórico editor que fez alterações
--@Tipo = Tipo de alteração (A musica, a album, ...)
--@Nome = Nome do alvo
--@UserID = ID do utilizador que fez a alteração
INSERT INTO dbo.Historicoo
	(
		Tipo,
		Nome,
		UsernameID
	)
	VALUES
	(
		'@Tipo',
		'@Nome',
		'@UserID'
	)

	
	
--Verificar se um utilizador tem notificações e apaga-las.
--@User = Id do utilizador

SELECT Notif.Message
	FROM dbo.Notification Notif
	WHERE Notif.UserID = '@User';

DELETE FROM dbo.Notification
	WHERE dbo.Notification.UserID = '@User';

	
	
--Dar/retirar privilégios de editor a um utilizador
--@User = username do utilizador ao qual

SELECT Usr.UserID, Usr.admin
	FROM dbo.Users Usr
	WHERE Usr.username = '@User'

	IF (SELECT Usr.UserID, Usr.admin
		FROM dbo.Users Usr
		WHERE Usr.username = '@User') = 0
	BEGIN
		UPDATE dbo.Users
		SET admin = 1
		WHERE username = '@User'
	END
	ELSE
	BEGIN
		UPDATE dbo.Users
		SET admin = 0
		WHERE username = '@User'
	END

--Registar Utilizador
--@User = username inserido pelo utilizador
--@Password = password inserida pelo utilizador

IF (SELECT COUNT(*) FROM dbo.Users UsersTa 
	WHERE UsersTa.username = '@User') = 0
BEGIN
	IF (SELECT COUNT(*) FROM dbo.Users ) = 0
	BEGIN
		INSERT INTO dbo.Users
		(
			username,
			password,
			admin
		)
		VALUES
		(
			'@User',
			'@Password',
			1
		)
	END
	IF (SELECT COUNT(*) FROM dbo.Users) > 0
	BEGIN
		INSERT INTO dbo.Users
		(
			username,
			password,
			admin
		)
		VALUES
		(
			'@User',
			'@Password',
			0
		)
	END

	SELECT UsersTa.UserID, UsersTa.admin 
		FROM dbo.Users UsersTa 
		WHERE UsersTa.username = '@User' AND UsersTa.password = '@Password';

END	

	
--Remover informação de uma música ou album
--@Tipo = Tipo de alvo (Musica ou album)
--@Nome = Nome do alvo
--@AdicionaNome = Nome do artista (??)
IF '@Tipo' = 'musica'
BEGIN
	DELETE FROM dbo.Music
	WHERE MusicInfo = (SELECT MusInf.MusicInfoID
		FROM dbo.MusicInfo MusInf
		WHERE MusInf.Titulo =  '@Nome')
	AND Artist = (SELECT Art.ArtistID
		FROM dbo.Artist Art
		WHERE '@AdicionaNome' = Art.Name)
END

IF '@Tipo' = 'album'
BEGIN
	DELETE FROM dbo.Album
	WHERE AlbumInfo = (SELECT AlbInf.AlbumInfoID
		FROM dbo.AlbumInfo AlbInf
		WHERE '@Nome' = AlbInf.Name)
	AND Music = (SELECT MusInf.MusicInfoID
		FROM dbo.MusicInfo MusInf
		WHERE '@AdicionaNome' = MusInf.Titulo)
END

	
--Procurar
--@TipoSearch = Tipo do alvo a ser procurado (album,musica,..)
--@Nome = Nome do alvo

IF '@TipoSearch' = 'album'
BEGIN
	SELECT AlbInf.Name
	FROM dbo.AlbumInfo AlbInf
	WHERE AlbInf.Name LIKE '%'+'@Nome'+'%'
END

IF '@TipoSearch' = 'musica'
	BEGIN
		SELECT MusInf.Titulo
		FROM dbo.MusicInfo MusInf
		WHERE MusInf.Titulo LIKE '%'+'@Nome'+'%'
	END

IF '@TipoSearch' = 'genero'
	BEGIN
		SELECT GenInf.Name
		FROM dbo.Genre GenInf
		WHERE GenInf.Name LIKE '%'+'@Nome'+'%'
	END

IF '@TipoSearch' = 'artista'
	BEGIN
		SELECT ArtInf.Name
		FROM dbo.Artist ArtInf
		WHERE ArtInf.Name LIKE '%'+'@Nome'+'%'
	END

	
--Fazer Critica a um album	
--@User = ID do utilizador que fez a critica
--@Album = nome do album a ser criticado
--@Critica = Critica feita pelo utilizador
--@PONT = pontuação dada pelo utilizador	

INSERT INTO dbo.Review
(
	UsernameID,
	Album,
	Comment,
	Score
)
VALUES
(
	'@User',
	(SELECT Findalbum.AlbumInfoID
		FROM dbo.AlbumInfo Findalbum
		WHERE Findalbum.Name = '@Album'),
	'@Critica',
	'@PONT'
)

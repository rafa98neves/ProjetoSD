package com.company;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import sun.plugin2.main.server.ModalitySupport;

import java.util.Scanner;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.net.*;
import java.io.*;


/**
 * Description: Interface que o User vê e utiliza para comunicação para o resto do programa
 *
 * @param
 * @return
 */
public class ClienteRMI extends UnicastRemoteObject implements DropMusic_C_I{
	private static User online;
	private static ClienteRMI c;
	public static DropMusic_S_I h;
	private static String Server = "Drop1";
	private static int PORT = 7000;

	ClienteRMI() throws RemoteException {
		super();
	}

	public void ChangeUserToEditor(boolean change){
		online.ChangeUserToEditor(change);
	}

	public void ping(){
	}

	public void Print(String s){
		System.out.println(s);
	}

	public static void LogOut(){
		while(true){
			try{
				h.UserQuit(c, online.GetNome());
				break;
			}catch(Exception c){
				BackUp(false);
			}
		}
		System.exit(0);
	}

	public static void BackUp(boolean preRegisto){
		int connection = 0;
		int counter = 0;
		do{
			counter++;
			try {
				Server = "Drop2";
				PORT = 7001;
				h = (DropMusic_S_I) LocateRegistry.getRegistry(PORT).lookup(Server);
				if(!preRegisto) h.NewUser(c, online.GetNome());
			}catch(Exception e1){
				try{
					Server = "Drop1";
					PORT = 7000;
					h = (DropMusic_S_I) LocateRegistry.getRegistry(PORT).lookup(Server);
					if(!preRegisto) h.NewUser(c, online.GetNome());
				}catch(Exception e2){
					connection++;
					try{
						Thread.sleep(1000);
					}catch(Exception e3){
						System.out.println("Problemas com a thread main: " + e3);
					}
				}
			}
			if(connection != counter) break;
		}while(connection != 30);

		if(connection == 30){
			System.out.println("Nao foi possivel estabelecer a ligacao ao servidor, tente mais tarde");
			System.exit(0);
		}
	}

	public static void MainScreen(){
		System.out.println("\t\t\t >>DropMusic<< ");
		System.out.println("\n");
		System.out.printf("Pretende:\n\t1.Login\n\t2.Registo\n\t0.Exit\n");

		while(true){
			int opcao;

			while (true){
				try {
					Scanner sc = new Scanner(System.in);
					opcao = sc.nextInt();
					if(opcao != 0 && opcao != 1 && opcao != 2) System.out.println("Opcao Invalida");
					else break;
				} catch (Exception err) {
					System.out.println("Escreva um digito por favor");
				}
			}

			switch(opcao){
				case 1: Login();
					break;
				case 2: Registo();
					break;
				case 0: LogOut();
					break;
				default: System.out.println("Opcao Invalida");
					break;
			}
		}
	}

	public static void Registo(){
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		boolean registou = false;
		String[] resposta;
		String nome = new String();
		String password = new String();
		while(true){
			System.out.printf("\nNome de utlizador (sem espacos ou caracteres especiais): ");
			try{
				nome = reader.readLine();
			}catch(Exception aa){
				System.out.println("Problemas com o reader");
			}

			System.out.printf("Password: ");
			try{
				password = reader.readLine();
			}catch(Exception aaa){
				System.out.println("Problemas com o reader");
			}
			while(true){
				try {
					resposta = h.RegistUser(nome,password);
					if(resposta[0].compareTo("true")==0){
						System.out.println("Registo efectuado com sucesso!");
						registou = true;
					}
					break;
				} catch (Exception e) {
					BackUp(true);
				}
			}
			if(!registou){
				int choice = 3;
				System.out.println("Username ja esta em uso, escolha outro");
				System.out.println("Pretende:\n1.Tentar outra vez\n2.Fazer Login\n\n0.Exit");
				while (true){
					try {
						Scanner sc2 = new Scanner(System.in);
						choice = sc2.nextInt();
						if(choice != 0 && choice != 1 && choice != 2) System.out.println("Opcao Invalida");
						else break;
					} catch (Exception err) {
						System.out.println("Escreva um digito por favor");
					}
				}
				if(choice==2) Login();
				else if(choice==0) LogOut();
			}
			else{
				break;
			}
		}
		if(resposta[2].compareTo("true")==0) online = new User(nome, password,true, Integer.parseInt(resposta[1]));
		else online = new User(nome, password,false, Integer.parseInt(resposta[1]));
		DropMusic();
	}

	public static void Login(){
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		boolean flag = true;
		String nome = new String();
		String password = new String();
		String[] resposta = new String[3];
		while(flag){
			System.out.printf("\nNome de utlizador: ");
			try{
				nome = reader.readLine();
			}catch(Exception aa){
				System.out.println("Problemas com o reader");
			}

			System.out.printf("Password: ");
			try{
				password = reader.readLine();
			}catch(Exception aaa){
				System.out.println("Problemas com o reader");
			}
			while(true) {
				try {
					resposta = h.CheckUser(nome,password);
					if(resposta[0].compareTo("true")==0){
						System.out.println("Login efectuado com sucesso!");
						flag = false;
						break;
					} else {
						System.out.println("\nUsername ou password errados!");
						System.out.println("\nPretende:\n1.Tentar outra vez\n2.Registar\n\n0.Exit");
						int choice = 3;
						while (true){
							try {
								Scanner sc = new Scanner(System.in);
								choice = sc.nextInt();
								if(choice != 0 && choice != 1 && choice != 2) System.out.println("Opcao Invalida");
								else break;
							} catch (Exception err) {
								System.out.println("Escreva um digito por favor");
							}
						}
						if (choice == 2) Registo();
						else if (choice == 0) LogOut();
						break;
					}
				} catch (Exception e) {
					BackUp(false);
				}
			}
		}
		if(resposta[2].compareTo("true")==0) online = new User(nome, password,true, Integer.parseInt(resposta[1]));
		else online = new User(nome, password,false, Integer.parseInt(resposta[1]));
		DropMusic();
	}

	public static void DropMusic(){
		boolean exit = false;
		System.out.println("\n\n\t\t >>DropMusic<<");
		while(true){
			try{
				h.NewUser(c, online.GetNome());
				h.CheckNotifications(online.GetID(),c);
				Thread.sleep(500);
				break;
			}catch(Exception c){
				BackUp(false);
			}
		}
		Scanner sc = new Scanner(System.in);
		if(online.IsEditor()){
			System.out.println("\n1.Pesquisar\n2.Criar Playlist\n3.Partilhar musicas\n4.Download de musicas\n5.Upload de musicas\n6.Criar\n7.Dar previlegios\n0.Log Out");
			while(!exit){
				int choice = -1;
				while (true){
					try {
						sc = new Scanner(System.in);
						choice = sc.nextInt();
						if(choice <0 && choice >6) System.out.println("Opcao Invalida");
						else break;
					} catch (Exception err) {
						System.out.println("Escreva um digito por favor");
					}
				}
				switch(choice){
					case 1:	Pesquisar();
							break;
					case 2: Playlist();
							break;
					case 3: Partilhar();
							break;
					case 4: Download();
							break;
					case 5: Upload();
							break;
					case 6: Criar();
							break;
					case 7: GivePrev();
							break;
					case 0: exit=true;
							break;
					default: System.out.println("Opcao Invalida");
				}
			}
		}
		else{
			System.out.println("\n1.Pesquisar\n2.Criar Playlist\n3.Partilhar musicas\n4.Donwload de musicas\n5.Upload de musicas\n0.Log Out");
			while(!exit){
				int choice = -1;
				while (true){
					try {
						sc = new Scanner(System.in);
						choice = sc.nextInt();
						if(choice <0 && choice > 5) System.out.println("Opcao Invalida");
						else break;
					} catch (Exception err) {
						System.out.println("Escreva um digito por favor");
					}
				}
				switch(choice){
					case 1:	Pesquisar();
							break;
					case 2: Criar();
							break;
					case 3: Playlist();
							break;
					case 4: Download();
							break;
					case 5: Upload();
							break;
					case 0: exit=true;
							break;
					default: System.out.println("Opcaoo Invalida");
				}
			}
		}
		MainScreen();
	}

	public static void Criar(){
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);

		System.out.println("\n\n\t\t >>Criar conteudo<<");
		Scanner sc = new Scanner(System.in);
		System.out.println("\n1.Criar artista\n2.Criar album\n3.Criar musica\n0.Back");
		int choice = -1;
		while (true){
			try {
				sc = new Scanner(System.in);
				choice = sc.nextInt();
				if(choice <0 && choice >3) System.out.println("Opcao Invalida");
				else break;
			} catch (Exception err) {
				System.out.println("Escreva um digito por favor");
			}
		}
		int choice2, year;
		String nome = "", compositor, genero;
		String artista ="";
		switch(choice){
			case 1: //artista
				System.out.println("\nNome: ");
				try{
					nome = reader.readLine();
				}catch(Exception aaa){
					System.out.println("Problemas com o reader");
				}
				System.out.println("\n1.Compositor\n2.Nao Compoe ");
				while (true){
					try {
						sc = new Scanner(System.in);
						choice2 = sc.nextInt();
						if(choice2 <0 && choice2 >2) System.out.println("Opcao Invalida");
						else break;
					} catch (Exception err) {
						System.out.println("Escreva um digito por favor");
					}
				}
				if(choice2 == 1) compositor = "true";
				else compositor = "false";
				while(true) {
					try {
						h.Criar(online.GetID(), "artista", nome, compositor, " ");
						break;
					} catch (Exception c) {
						BackUp(false);
					}
				}
				System.out.println("O artista foi criado! Podera agora fazer alteracoes nele");
				break;
			case 2: //album
				System.out.println("\nNome: ");
				try{
					nome = reader.readLine();
				}catch(Exception aaa){
					System.out.println("Problemas com o reader");
				}
				System.out.println("Ano: ");
				while (true){
					try {
						sc = new Scanner(System.in);
						year = sc.nextInt();
						if(year <0 && year >9999) System.out.println("Opcao Invalida");
						else break;
					} catch (Exception err) {
						System.out.println("Escreva um digito por favor");
					}
				}
				while(true) {
					try {
						h.Criar(online.GetID(), "album", nome, Integer.toString(year), "none");
						break;
					} catch (Exception c) {
						BackUp(false);
					}
				}
				System.out.println("O album foi criado! Podera agora fazer alteracoes nele");
				break;
			case 3: //musica
				System.out.println("\nNome: ");
				try{
					nome = reader.readLine();
				}catch(Exception c){
					System.out.println("Problemas com o reader");
				}
				while(true){
					System.out.println("\nArtista: ");
					try{
						artista = reader.readLine();
					}catch(Exception c){
						System.out.println("Problemas com o reader");
					}
					String[] respostas;
					while(true){
						try{
							respostas = h.Find(online.GetID(),artista,"artista");
							break;
						}catch(Exception c){
							BackUp(false);
						}
					}
					if(respostas[0].compareTo("none")==0) System.out.println("Nada encontrado para: " + artista);
					else {
						int possibilidades;
						for (possibilidades = 1; possibilidades <= respostas.length; possibilidades++) {
							System.out.println(possibilidades + ". ->" + respostas[possibilidades - 1]);
						}
						System.out.println("0. Exit");
						int pos = 0;
						while (true) {
							try {
								sc = new Scanner(System.in);
								pos = sc.nextInt();
								if (pos < 0 && pos > respostas.length) System.out.println("Opcao Invalida");
								else break;
							} catch (Exception err) {
								System.out.println("Escreva um digito por favor");
							}
						}
						if (pos != 0) {
							artista = respostas[pos - 1];
							break;
						}
					}
				}
				while(true){
					System.out.println("\nGenero: ");
					String[] respostas;
					while(true){
						try{
							respostas = h.GetGeneros(online.GetID());
							break;
						}catch(Exception c){
							BackUp(false);
						}
					}
					if(respostas[0].compareTo("none")==0) System.out.println("Nenhum genero encontrado");
					else {
						int possibilidades = 0;
						while(respostas[possibilidades].compareTo("none") != 0) {
							System.out.println(possibilidades+1 + ". ->" + respostas[possibilidades]);
							possibilidades++;
						}
						System.out.println("0. Exit");
						int pos = 0;
						while (true) {
							try {
								sc = new Scanner(System.in);
								pos = sc.nextInt();
								if (pos < 0 && pos > respostas.length) System.out.println("Opcao Invalida");
								else break;
							} catch (Exception err) {
								System.out.println("Escreva um digito por favor");
							}
						}
						if (pos != 0) {
							genero = respostas[possibilidades];
							break;
						}
					}
				}
				while(true) {
					try {
						h.Criar(online.GetID(), "musica", nome, artista,genero);
						break;
					} catch (Exception c) {
						BackUp(false);
					}
				}
				System.out.println("A musica foi criada! Podera agora fazer alteracoes nela");
				break;
			case 0:
				DropMusic();
			default: System.out.println("Opcao Invalida");
		}
		try{
			Thread.sleep(5000);
		}catch (Exception e){}
		DropMusic();
	}

	public static void Pesquisar(){
		System.out.println("\n\n\t\t >>Pesquisar<<");
		Scanner sc = new Scanner(System.in);
		System.out.println("\n1.Pesquisar musica\n2.Pesquisar album\n3.Pesquisar artista\n0.Back");
		int choice = -1;
		while (true){
			try {
				sc = new Scanner(System.in);
				choice = sc.nextInt();
				if(choice <0 && choice >3) System.out.println("Opcao Invalida");
				else break;
			} catch (Exception err) {
				System.out.println("Escreva um digito por favor");
			}
		}
		switch(choice){
			case 1:	Pesquisar("musica");
					break;
			case 2: Pesquisar("album");
					break;
			case 3: Pesquisar("artista");
					break;
			case 0: DropMusic();
					break;
			default: System.out.println("Opcao Invalida");
		}
	}

	public static void Pesquisar(String escolha){

		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		boolean flag = false;
		String search = new String();
		String[] respostas;
		Scanner sc = new Scanner(System.in);
		if(escolha.compareTo("album")==0){
			System.out.println("\nPesquisar por:");
			System.out.println("\n1.Nome do Album\n2.Nome do artista");
			int choice = -1;
			while (true){
				try {
					sc = new Scanner(System.in);
					choice = sc.nextInt();
					if(choice !=1 && choice != 2) System.out.println("Opcao Invalida");
					else break;
				} catch (Exception err) {
					System.out.println("Escreva um digito por favor");
				}
			}
			switch(choice){
				case 1:
					System.out.println("\nNome: ");
					try{
						search = reader.readLine();
					}catch(Exception aa){
						System.out.println("Nao percebemos o que quis dizer");
					}
					break;
				case 2:
					System.out.println("\nNome: ");
					escolha = "artista";
					try{
						search = reader.readLine();
					}catch(Exception aa){
						System.out.println("Nao percebemos o que quis dizer");
					}
					flag = true;
					break;
				default:
					System.out.println("Opcao invalida!");
			}
		}
		else{
			System.out.println("\nNome: ");
			try{
				search = reader.readLine();
			}catch(Exception aa){
				System.out.println("Nao percebemos o que quis dizer");
			}
		}

		while(true){
			try{
				respostas = h.Find(online.GetID(),search,escolha);
				break;
			} catch (Exception re) {
				BackUp(false);
			}
		}

		if(respostas[0].compareTo("none")==0) System.out.println("Nada encontrado para: " + search);
		else{
			for(int possibilidades = 1; possibilidades<=respostas.length; possibilidades++){
				System.out.println(possibilidades + ". ->" + respostas[possibilidades-1]);
			}
			System.out.println("0.Back");
			int numero = -1;
			while (true){
				try {
					sc = new Scanner(System.in);
					numero = sc.nextInt();
					if(numero < 0 && numero > respostas.length) System.out.println("Opcao Invalida");
					else break;
				} catch (Exception err) {
					System.out.println("Escreva um digito por favor");
				}
			}
			if(numero == 0) DropMusic();

			String[] details;
			while(true){
				try{
					details = h.GetDetails(online.GetID(),respostas[numero-1],escolha);
					break;
				}catch(Exception re2){
					BackUp(false);
				}
			}
			if(flag && details[0].compareTo("none")!=0){
				int j = 0, AlbumNumber = -1;
				String[] details2;
				escolha = "album";
				System.out.println("Qual album procura?");
				for(int i = 0; i<details.length; i+=2){
					if(details[i].compareTo("Album") == 0){
						if(AlbumNumber == -1) AlbumNumber = i;
						System.out.println(j+1 + " ." + details[i+1]);
						j++;
					}
				}
				System.out.println("0. Back");
				while (true){
					try {
						sc = new Scanner(System.in);
						numero = sc.nextInt();
						if(numero < 0 && numero > j) System.out.println("Opcao Invalida");
						else break;
					} catch (Exception err) {
						System.out.println("Escreva um digito por favor");
					}
				}
				if(numero == 0) DropMusic();
				else {
					while (true) {
						try {
							details2 = h.GetDetails(online.GetID(), details[AlbumNumber+numero+(numero-1)], escolha);
							break;
						} catch (Exception re2) {
							BackUp(false);
						}
					}
					for (int i = 0; i < details2.length; i += 2) {
						if(details2[i].compareTo("Critica") == 0){
							System.out.println(details2[i+1] + " pontuou o album com " + details[i+2] + " pontos e fez a critica: \n" + details[i+3] + "\n");
							i += 2;
						}
						else System.out.println(details2[i] + ": " + details2[i + 1]);
					}
				}
			}
			else if(!flag && details[0].compareTo("none")!=0){
				for(int i = 0; i<details.length; i+=2){
					System.out.println("Palavra : " + details[i]);
					if(details[i].compareTo("Critica") == 0){
						System.out.println(details[i+1] + " pontuou o album com " + details[i+2] + " pontos e fez a critica: \n" + details[i+3] + "\n");
						i += 4;
					}
					else System.out.println(details[i] + ": " + details[i + 1]);
				}
			}
			if(escolha.compareTo("album")==0) FazerCritica(respostas[numero-1]);
			if(online.IsEditor()) FazerAlteracoes(escolha, respostas[numero-1], details);
		}
	}

	public static void FazerCritica(String album){
		Scanner sc = new Scanner(System.in);
		String choice = new String();
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		System.out.println("Prentende fazer um critica [y/n]?");
		try{
			choice = reader.readLine();
		}catch(Exception re){
			System.out.println("Erro a escrever: " + re);
		}
		if(choice.compareTo("y")==0){
			String critica;
			int pontuacao;
			System.out.printf("Pontuacao [0 a 10]: ");
			while(true){
				pontuacao = sc.nextInt();
				if(pontuacao <= 10 || pontuacao >= 0) break;
				else System.out.printf("Escolha uma pontuacao entre 0 e 10!\n");
			}
			System.out.printf("\nComentario >>> ");
			try {
				critica = reader.readLine();
			}catch (Exception read) {
				System.out.println("Nao percebemos o que quis dizer... tente mais tarde");
				return;
			}
			try{
				h.Write(online.GetID(), online.GetNome(), pontuacao, critica, album);
			}catch(Exception t){
				BackUp(false);
				try{
					h.Write(online.GetID(), online.GetNome(), pontuacao, critica, album);
				}catch(Exception tt){
					System.out.println("Nao foi possivel fazer o seu comentario, tenta mais tarde");
				}
			}
		}
	}

	public static void FazerAlteracoes(String escolha, String nome, String[] details){
		int counter;
		String choice = new String();
		Scanner sc = new Scanner(System.in);
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		while(true){
			System.out.printf("Pretende fazer alteracoes?[y/n]");
			try{
				choice = reader.readLine();
			}catch(Exception re){
				System.out.println("Erro a escrever: " + re);
			}
			if(choice.compareTo("y")==0){
				String alterado, alteracao;
					if(escolha.compareTo("album") == 0){
						System.out.printf("O que quer alterar?[nome/ano/RemoverMusica/AdicionarMusica/Exit]: ");
						alterado = sc.nextLine().toLowerCase();
						if(alterado.compareTo("exit")==0) break;
						switch(alterado){
							case "nome":
							case "ano":
								System.out.printf("\nNovo>>> ");
								try{
									choice = reader.readLine();
								}catch(Exception re){
									System.out.println("Erro a escrever: " + re);
								}
								while(true){
									try{
										h.AlterarDados(online.GetID(), online.GetNome(), escolha, nome, alterado, choice);
										break;
									}catch(Exception re2){
										BackUp(false);
									}
								}
								break;
							case "removermusica":
								counter = 1;
								int MusicIndex = -1;
								String[] beforeChoices = new String[256];
								for(int i = 0; i<details.length; i+=2){
									if(details[i].compareTo("Music")==0){
										if(MusicIndex == -1) MusicIndex = i;
										System.out.println(counter + ". " + details[i+1]);
										counter++;
									}
									beforeChoices[counter-1] = details[i+1];
								}
								System.out.println("0. Back");
								counter = sc.nextInt();
								if(counter == 0) DropMusic();
								while(true){
									try{
										h.AddRemoveSomething(online.GetID(), "album",nome,beforeChoices[counter],true); //Remove musica escolhida de album com nome 'nome'
										details[MusicIndex + (counter) + (counter -2) ] = "Removed";
										break;
									}catch(Exception re3){
										BackUp(false);
									}
								}
								break;
							case "adicionarmusica":
								System.out.printf("\nNome da Musica>>> ");
								try{
									choice = reader.readLine();
								}catch(Exception re){
									System.out.println("Erro a escrever: " + re);
								}
								String[] respostas = new String[256];
								while(true){
									try{
										respostas = h.Find(online.GetID(),choice,"musica");
										break;
									}catch(Exception re4){
										BackUp(false);
									}
								}

								if(respostas[0].compareTo("none")==0) System.out.println("Nada encontrado para: " + choice);
								else{
									for(int possibilidades = 1; possibilidades<=respostas.length; possibilidades++){
										System.out.println(possibilidades + ". ->" + respostas[possibilidades-1]);
									}
									counter = sc.nextInt();
									while(true){
										try{
											h.AddRemoveSomething(online.GetID(),"album", nome, respostas[counter - 1],false); //Adiciona musica escolhida a album com nome 'nome'
											break;
										}catch(Exception re5){
											BackUp(false);
										}
									}
								}
								break;

							default: System.out.println("Opcao Invalida");
						}
					}
					else if(escolha.compareTo("musica") == 0) {
						System.out.printf("O que quer alterar?[titulo/artistas/genero/Exit]: ");
						alterado = sc.nextLine().toLowerCase();
						if(alterado.compareTo("exit")==0) break;
						switch(alterado){
							case "titulo":
								System.out.printf("\nNovo>>> ");
								try{
									choice = reader.readLine();
								}catch(Exception re){
									System.out.println("Erro a escrever: " + re);
								}
								while(true){
									try{
										h.AlterarDados(online.GetID(), online.GetNome(), escolha, nome, alterado, choice);
										break;
									}catch(Exception re2){
										BackUp(false);
									}
								}
								break;
							case "artistas":
								System.out.printf("Quer:\n 1.Remover artista\n 2.Adicionar artista\n 0.Back\n ");
								counter = sc.nextInt();
								if(counter == 0) break;
								else if(counter == 1){
									counter = 1;
									String[] beforeChoices = new String[256];
									for(int i = 0; i<details.length; i+=2){
										if(details[i].compareTo("Artist")==0) {
											System.out.println(counter + ". " + details[i + 1]);
											counter++;
										}
										beforeChoices[counter-1] = details[i+1];
									}
									counter = sc.nextInt();
									while(true){
										try{
											h.AddRemoveSomething(online.GetID(), "musica",nome,beforeChoices[counter],true);
											break;
										}catch(Exception re3){
											BackUp(false);
										}
									}
									break;
								}
								else{
									System.out.printf("\nNome do artista>>> ");
									try{
										choice = reader.readLine();
									}catch(Exception re){
										System.out.println("Erro a escrever: " + re);
									}
									String[] respostas = new String[256];
									while(true){
										try{
											respostas = h.Find(online.GetID(),choice,"artista");
											break;
										}catch(Exception re4){
											BackUp(false);
										}
									}

									if(respostas[0].compareTo("none")==0) System.out.println("Nada encontrado para: " + choice);
									else{
										for(int possibilidades = 1; possibilidades<=respostas.length; possibilidades++){
											System.out.println(possibilidades + ". ->" + respostas[possibilidades-1]);
										}
										counter = sc.nextInt();
										while(true){
											try{
												h.AddRemoveSomething(online.GetID(), "musica", nome, respostas[counter - 1],false); //Adiciona artista escolhido a musica com nome 'nome'
												break;
											}catch(Exception re2){
												BackUp(false);
											}
										}
									}
									break;
								}
							case "genero":
								String[] respostas = new String[50];
								while(true){
									try{
										respostas = h.GetGeneros(online.GetID());
										break;
									}catch(Exception re){
										BackUp(false);
									}
								}
								System.out.println("Novo genero: ");
								int i = 0;
								while(respostas[i] != null && respostas[i].compareTo("none")!=0){
									System.out.println(i+1 + ". " + respostas[i]);
									i++;
								}
								System.out.println("0. Back");
								int esc = sc.nextInt();
								if(esc == 0) break;
								else if(esc > 0 && esc <= i+1){
									try{
										h.AlterarDados(online.GetID(),online.GetNome(), escolha, nome, alterado, respostas[esc-1]);
										break;
									}catch(Exception re2){
										BackUp(false);
									}
								break;
								}
								else System.out.println("Resposta Invalida");
								break;
							default: System.out.println("Opcao Invalida");
						}
					}
					else if(escolha.compareTo("artista") == 0) {
						System.out.printf("O que quer alterar?[Nome/Compositor/Exit]: ");
						alterado = sc.nextLine().toLowerCase();
						if(alterado.compareTo("exit")==0) break;
						switch(alterado){
							case "nome":
								System.out.printf("\nNome do artista>>> ");
								try{
									choice = reader.readLine();
								}catch(Exception re){
									System.out.println("Erro a escrever: " + re);
								}
								while(true){
									try{
										h.AlterarDados(online.GetID(), online.GetNome(),"artista",nome,"nome",choice);
										break;
									}catch(Exception re3){
										BackUp(false);
									}
								}
							case "compositor":
								for(int i = 0; i<details.length; i+=2){
									if(details[i].compareTo("compositor")==0){
										if(details[i+1].compareTo("true")==0){
											System.out.println("Pretende remover o titulo de compositor deste artista? [y/n]");
											try{
												choice = reader.readLine();
											}catch(Exception re){
												System.out.println("Erro a escrever: " + re);
											}
											if(choice.compareTo("y")==0){
												while(true){
													try{
														String t = "false";
														h.AlterarDados(online.GetID(), online.GetNome(),"artista",nome,"compositor",t);
														break;
													}catch(Exception re3){
														BackUp(false);
													}
												}
											}
										}
										else{
											System.out.println("Pretende dar o titulo de compositor deste artista? [y/n]");
											try{
												choice = reader.readLine();
											}catch(Exception re){
												System.out.println("Erro a escrever: " + re);
											}
											if(choice.compareTo("y")==0){
												while(true){
													try{
														String t = "true";
														h.AlterarDados(online.GetID(), online.GetNome(),"artista",nome,"compositor",t);
														break;
													}catch(Exception re3){
														BackUp(false);
													}
												}
											}
										}
									}
								}
								break;
							default: System.out.println("Opcao Invalida");
						}
					}
					else{
						System.out.printf("O que quer alterar?[AcrescentarGenero/Exit]: ");
						alterado = sc.nextLine().toLowerCase();
						if(alterado.compareTo("exit")==0);
						else if(alterado.compareTo("acrescentargenero")==0){
							System.out.println("Genero a acrescentar: ");
							try{
								choice = reader.readLine();
							}catch(Exception re){
								System.out.println("Erro a escrever: " + re);
							}
							while(true){
								try{
									h.AddGenero(online.GetID(), choice);
								}catch(Exception re2){
									BackUp(false);
								}
							}
						}
						else System.out.println("Opcao Invalida");
						break;
					}
				}
			else break;
		}
		DropMusic();
	}

	public static void Playlist(){
		/*System.out.println("\n\n\n\n\t\t >>Playlist<<");
		String[] playlists = h.GetPlaylists();
		int counter = 1;
		if(playlist[0].compareTo("none")!=0){
			for(int i = 0; i<playlists.length; i++){

				System.out.println(counter + ". " + playlists[i]);
				counter++;
			}
		}
		System.out.println(counter +". Criar nova");*/
		System.out.println("Funcionalidade a ser desenvolvida.");
		DropMusic();
	}

	public static void Upload(){
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		System.out.println("\n\n\t\t >>Upload de musica<<");
		System.out.printf("\n Nome da musica>> ");
		String escolha;
		String[] respostas;

		try{
			escolha = reader.readLine();
		}catch(Exception a1){
			System.out.println("Erro a escrever");
			return;
		}

		while(true){
			try{
				respostas = h.Find(online.GetID(),escolha,"musica");
				break;
			} catch (Exception a2) {
				BackUp(false);
			}
		}
		if(respostas[0].compareTo("none")==0) System.out.println("Nada encontrado para: " + escolha);
		else {
			int i = 0;
			for(i = 0; i<respostas.length; i++) {
				System.out.println(i+1 + ". -> " + respostas[i]);
			}
			System.out.println("0. -> Back");
			Scanner sc = new Scanner(System.in);
			int numero = sc.nextInt();
			if(numero == 0) DropMusic();
			String MusicChoice = respostas[numero-1];
			if (numero != 0) {
				String localizacao = " ";
				String[] endereco = new String[2];
				File musica;
				while (true) {
					reader = new BufferedReader(input);
					System.out.println("localizacao do ficheiro: ");
					while(true) {
						try {
							localizacao = reader.readLine();
							break;
						} catch (Exception a3) {
							System.out.println("Erro a escrever");
						}
					}

					musica = new File(localizacao);
					if (!musica.exists() || !musica.isFile()) {
						System.out.println("Nao e um ficheiro: " + musica);
					} else break;
				}
				while (true) {
					try {
						endereco = h.TransferMusic(online.GetID(), "upload", MusicChoice);
						break;
					} catch (Exception a2) {
						System.out.println("...Erro: " + c);
						BackUp(false);
					}
				}
				Socket s = null;
				while (true) {
					try {
						s = new Socket(endereco[0], Integer.parseInt(endereco[1]));
						DataOutputStream out = new DataOutputStream(s.getOutputStream());
						DataInputStream in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
						InputStream is = new FileInputStream(musica);
						byte[] buffer = new byte[8192];
						int count;
						while ((count = is.read(buffer)) > 0) {
							out.write(buffer, 0, count);
						}
						out.close();
						in.close();
						s.close();
						break;
					} catch (Exception c) {
						System.out.println("...Erro: " + c);
						BackUp(false);
					}
				}
				System.out.println("Musica transferida!");
			}
		}
		DropMusic();
	}

	public static void Partilhar(){
		System.out.println("\n\n\t\t >>Partilha de musica<<");
		String[] resposta;
		while(true) {
			try {
				resposta = h.ShareMusic(online.GetID());
				break;
			} catch (Exception x) {
				BackUp(false);
			}
		}
		if(resposta[0].compareTo("none") == 0) System.out.println("Faca primeiro o upload de uma musica");
		else{
			System.out.println("Qual musica pretende partilhar? ");
			int i = 0;
			while (resposta[i].compareTo("none") != 0){
				System.out.println(i+1 + ". -> " + resposta[i]);
				i++;
			}
			System.out.println("0. Back");
			int choice = 0;
			while (true){
				try {
					Scanner sc = new Scanner(System.in);
					choice = sc.nextInt();
					if(choice < 0 && choice > i) System.out.println("Opcao Invalida");
					else break;
				} catch (Exception err) {
					System.out.println("Escreva um digito por favor");
				}
			}
			while(true) {
				try {
					h.ShareMusic(online.GetID(), resposta[choice - 1]);
					break;
				}catch (Exception x1) {
					BackUp(false);
				}
			}
			System.out.println("Musica partilhada!");
		}
		DropMusic();
	}

	public static void Download(){

		String NomeMusica = new String();
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);

		System.out.println("\n\n\t\t >>Download de musica<<");
		System.out.printf("\n Nome da musica>> ");
		String escolha;
		String[] respostas;
		try{
			escolha = reader.readLine();
		}catch(Exception a1){
			System.out.println("Erro a escrever");
			return;
		}

		while(true){
			try{
				respostas = h.Find(online.GetID(),escolha,"musica");
				break;
			} catch (Exception a2) {
				BackUp(false);
			}
		}
		if(respostas[0].compareTo("none")==0) System.out.println("Nada encontrado para: " + escolha);
		else {
			int i = 0;
			for(i = 0; i<respostas.length; i++) {
				System.out.println(i+1 + ". -> " + respostas[i]);
			}
			System.out.println("0. -> Back");
			Scanner sc = new Scanner(System.in);
			int numero = sc.nextInt();
			if(numero == 0) DropMusic();
			String MusicChoice = respostas[numero-1];
			if (numero != 0) {
				String localizacao = new String();
				String[] endereco;
				while (true) {
					reader = new BufferedReader(input);
					System.out.println("Diretoria: ");
					try {
						localizacao = reader.readLine();
					} catch (Exception a3) {
						System.out.println("Erro a escrever");
						DropMusic();
					}
					reader = new BufferedReader(input);
					System.out.println("Nome da musica no seu pc: ");
					try {
						NomeMusica = reader.readLine();
						break;
					} catch (Exception a3) {
						System.out.println("Erro a escrever");
						DropMusic();
					}
				}
				while (true) {
					try {
						endereco = h.TransferMusic(online.GetID(), "download", MusicChoice);
						break;
					} catch (Exception a2) {
						BackUp(false);
					}
				}

				Socket s = null;
				while (true) {
					try {
						s = new Socket(endereco[0], Integer.parseInt(endereco[1]));
						FileOutputStream out = new FileOutputStream(localizacao+NomeMusica+".mp3");
						DataInputStream in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
						byte[] bytes = new byte[8192];
						int count;
						while ((count = in.read(bytes)) > 0) {
							out.write(bytes, 0, count);
						}
						s.close();
						out.close();
						in.close();
						break;
					} catch (EOFException e) {
						System.out.println("EOF:" + e);
					} catch (IOException e) {
						System.out.println("IO:" + e);
					}catch (Exception c){
						BackUp(false);
					}
				}
				System.out.println("Musica transferida!");
			}
		}
		DropMusic();
	}

	public static void GivePrev(){
		Scanner sc = new Scanner(System.in);
		System.out.println("\n\n\t\t >>Dar previlegios<<");
		System.out.printf("\nNome do utilizador a dar previlegios: ");
		String user = sc.nextLine();
		while(true){
			try{
				h.GivePriv(online.GetID(), online.IsEditor(), user);
				Thread.sleep(500);
				break;
			} catch (Exception re) {
				BackUp(false);
			}
		}
		DropMusic();
	}

	// ============================MAIN===========================

	public static void main(String args[]) {
		boolean exit=false;
		String a;

		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);

		try {
			h = (DropMusic_S_I) LocateRegistry.getRegistry(7000).lookup(Server);
		}catch (Exception e1) {
			BackUp(true);
		}

		try{
			c = new ClienteRMI();
		}catch(Exception e2){
			System.out.println("Problemas a criar o cliente: " + e2);
		}
		MainScreen();
	}

}

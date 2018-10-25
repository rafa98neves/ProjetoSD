package com.company;

import java.util.Scanner;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.net.*;
import java.io.*;


public class ClienteRMI extends UnicastRemoteObject implements DropMusic_C_I{
	private static User online;
	private static ClienteRMI c;
	public static DropMusic_S_I h;
	private static String Server = "Drop1";
	private static int PORT = 7000;
	
	ClienteRMI() throws RemoteException {
		super();
	}
		
	public void ChangeUserToEditor(){
		online.ChangeUserToEditor(true);
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
		Scanner sc = new Scanner(System.in);
		
		while(true){
			int opcao = sc.nextInt();
			switch(opcao){
				case 1: Login();
					break;
				case 2: Registo();
					break;
				case 0: LogOut();
					break;
				default: System.out.println("Op��o Inv�lida");
					break;
			}
		}
	}
	
	public static void Registo(){
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		boolean registou = false;
		String nome = new String();
		String password = new String();
		Scanner sc = new Scanner(System.in);
		while(true){
			System.out.printf("\nNome de utlizador (sem espa�os ou caracteres especiais): ");
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
					if(h.RegistUser(nome,password)){
						System.out.println("Registo efectuado com sucesso!");
						registou = true;
					}
					break;
				} catch (Exception e) {
					BackUp(true);
				}
			}
			if(!registou){
				System.out.println("Username ja esta em uso, escolha outro");
				System.out.println("Pretende:\n1.Tentar outra vez\n2.Fazer Login\n\n0.Exit");
				int choice = sc.nextInt();
				if(choice==2) Login();
				else if(choice==0) LogOut();
			}
			else{
				break;
			}
		}
		online = new User(nome, password);
		DropMusic();
	}
	
	public static void Login(){
		String nome = new String();
		String password = new String();
		Scanner sc = new Scanner(System.in);
		System.out.printf("\nNome de utlizador: ");
		nome = sc.next();
		System.out.printf("Password: ");
		password = sc.next();
		while(true){
			try {
				if(h.CheckUser(nome, password,c)){
					System.out.println("Login efectuado com sucesso!");
					break;
				}
				else{
					System.out.println("\nUsername ou password errados!");
					System.out.println("\nPretende:\n1.Tentar outra vez\n2.Registar\n\n0.Exit");
					int choice = sc.nextInt();
					if(choice==2) Registo();
					else if(choice==0) LogOut();
					break;
				}
			} catch (Exception e) {
				BackUp(false);
			}
		}
		online = new User(nome,password);
		DropMusic();
	}
	
	public static void DropMusic(){	
		
		int opcao;
		boolean exit = false;
		System.out.println("\n\n\n\n\t\t >>DropMusic<<");
		while(true){
			try{
				h.NewUser(c, online.GetNome());
				h.CheckNotifications(online.GetNome(), c); 
				Thread.sleep(500);
				break;
			}catch(Exception c){
				BackUp(false);
			}
		}
		Scanner sc = new Scanner(System.in);
		if(online.IsEditor()){
			System.out.println("\n1.Pesquisar\n2.Criar Playlist\n3.Partilhar m�sicas\n4.Donwload de m�sicas\n5.Upload de m�sicas\n6.Dar previl�gios\n0.Log Out");
			while(!exit){
				opcao = sc.nextInt();
				switch(opcao){
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
					case 6: GivePrev();
							break;    
					case 0: exit=true;
							break;
					default: System.out.println("Op��o Inv�lida");
				}
			}
		}
		else{
			System.out.println("\n1.Pesquisar\n2.Criar Playlist\n3.Partilhar m�sicas\n4.Donwload de m�sicas\n5.Upload de m�sicas\n0.Log Out");
			while(!exit){
				opcao = sc.nextInt();
				switch(opcao){
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
					case 0: exit=true;
							break;
					default: System.out.println("Op��o Inv�lida");
				}
			}
		}
		MainScreen();
	}
	
	public static void Pesquisar(){
		int opcao;
		boolean exit = false;
		System.out.println("\n\n\t\t >>Pesquisar<<");
		Scanner sc = new Scanner(System.in);
		System.out.println("\n1.Pesquisar musica\n2.Pesquisar album\n3.Pesquisar genero\n4.Pesquisar artista\n0.Back");
		opcao = sc.nextInt();
		switch(opcao){
			case 1:	Pesquisar("musica");
					break;
			case 2: Pesquisar("album");
					break;
			case 3: Pesquisar("genero");
					break;
			case 4: Pesquisar("artista");
					break;
			case 0: DropMusic();
					break;
			default: System.out.println("Op��o Inv�lida");
		}	
	}
	
	public static	 void Pesquisar(String escolha){
		String search;
		String[] respostas;
		Scanner sc = new Scanner(System.in);
		
		System.out.printf("\nNome:");
		search = sc.nextLine();
		while(true){
			try{
				respostas = h.Find(search,escolha,c);
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
			int numero = sc.nextInt();
			String[] details;
			while(true){
				try{
					details = h.GetDetails(respostas[numero-1],escolha,c);
					break;
				}catch(Exception re2){
					BackUp(false);
				}
			}
			if(details[0].compareTo("none")!=0){
				for(int i = 0; i<details.length; i+=2){
					System.out.println(details[i] + ": " + details[i+1]);
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
			try{
				while ((critica = reader.readLine()) != null){
					try{
						h.Write(online.GetNome(), pontuacao, critica, album);
					}catch(Exception t){
						BackUp(false);
						try{
							h.Write(online.GetNome(), pontuacao, critica, album);
						}catch(Exception tt){
							System.out.println("Nao foi possivel fazer o seu comentario, tenta mais tarde");
						}
					}
				}
			}catch(Exception re4){
				System.out.println("N�o foi possivel fazer o seu comentario, tente mais tarde");
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
			System.out.printf("Pretende fazer altera��es?[y/n]");
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
										h.AlterarDados(online.GetNome(), escolha, nome, alterado, choice);  
										break;
									}catch(Exception re2){
										BackUp(false);
									}
								}
								break;
							case "removermusica":
								counter = 1;
								String[] beforeChoices = new String[256];
								for(int i = 0; i<details.length; i+=2){
									if(details[i].compareTo("musica")==0) System.out.println(counter + ". " + details[i+1]);
									beforeChoices[counter-1] = details[i+1];
								}
								counter = sc.nextInt();
								while(true){
									try{
										h.AddRemoveSomething("album",nome,beforeChoices[counter],true); //Remove musica escolhida de album com nome 'nome'
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
										respostas = h.Find(choice,"musica",c);
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
											h.AddRemoveSomething("album", nome, respostas[counter],false); //Adiciona musica escolhida a album com nome 'nome'
											break;
										}catch(Exception re5){
											BackUp(false);
										}
									}
								}
								break;
								
							default: System.out.println("Op��o Inv�lida");
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
										h.AlterarDados(online.GetNome(), escolha, nome, alterado, choice);  
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
										if(details[i].compareTo("artista")==0) System.out.println(counter + ". " + details[i+1]);
										beforeChoices[counter-1] = details[i+1];
									}
									counter = sc.nextInt();
									while(true){
										try{
											h.AddRemoveSomething("artista",nome,beforeChoices[counter],true); 
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
											respostas = h.Find(choice,"artista",c);
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
												h.AddRemoveSomething("musica", nome, respostas[counter],false); //Adiciona artista escolhido a musica com nome 'nome'
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
										respostas = h.GetGeneros();
										break;
									}catch(Exception re){
										BackUp(false);
									}
								}
								System.out.println("Novo genero: ");
								int i = 0;
								while(respostas[i] != null){
									System.out.println(i+1 + ". " + respostas[i]);
									i++;
								}
								System.out.println("0. Back");
								int esc = sc.nextInt();
								if(esc == 0) break;
								else if(esc > 0 && esc <= i+1){
									try{
										h.AlterarDados(online.GetNome(), escolha, nome, alterado, respostas[esc-1]);  
										break;
									}catch(Exception re2){
										BackUp(false);
									}	
								break;
								}
								else System.out.println("Resposta Invalida");
								break;
							default: System.out.println("Op��o Inv�lida");
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
										h.AlterarDados(online.GetNome(),"artista",nome,"nome",choice); 
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
														h.AlterarDados(online.GetNome(),"artista",nome,"compositor",t); 
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
														h.AlterarDados(online.GetNome(),"artista",nome,"compositor",t); 
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
							default: System.out.println("Op��o Inv�lida");
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
									h.AddGenero(choice);
								}catch(Exception re2){
									BackUp(false);
								}
							}
						}
						else System.out.println("Op��o Invalida");
						break;
					}
				}				
			else break;
		}
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
	}
	
	public static void Upload(){
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		
		System.out.println("\n\n\t\t >>Upload de musica<<");
		System.out.println("\n\n Nome da musica>> ");
		String escolha;
		String[] respostas = new String[256];
		
		try{
			escolha = reader.readLine();
		}catch(Exception a1){
			System.out.println("Erro a escrever");
			return;
		}
		
		while(true){
			try{
				respostas = h.Find(escolha,"musica",c);
				break;
			} catch (Exception a2) {
				BackUp(false);
			}
		}
		
		if(respostas[0].compareTo("none")==0) System.out.println("Nada encontrado para: " + escolha);
		else{
			int pos = 0;
			while(respostas[pos] != null){
				System.out.println(pos + ". -> " + respostas[pos+1]);
			}
			System.out.println("0. -> Back");
			Scanner sc = new Scanner(System.in);
			int numero = sc.nextInt();
			if(numero!=0){
				String localizacao;
				String[] endereco = new String[2];
				System.out.println("localizacao do ficheiro: ");
				try{
					localizacao = reader.readLine();
				}catch(Exception a3){
					System.out.println("Erro a escrever");
					return;
				}
				File musica = new File(localizacao);
				if (!musica.exists() || !musica.isFile()) throw new IllegalArgumentException("Nao e um ficheiro: " + musica);
				
				while(true){
					try{
						endereco = h.TransferMusic(online.GetNome());
						break;
					} catch (Exception a2) {
						BackUp(false);
					}
				}
				
				Socket s = null;
				while(true){
					try{
						s = new Socket(endereco[0],Integer.parseInt(endereco[1]));
						DataOutputStream out = new DataOutputStream(s.getOutputStream());
						byte [] musicbyte  = new byte [(int)musica.length()];
						out.write(musicbyte,0,musicbyte.length);
					}catch(Exception c){
						BackUp(false);
					}
				}
			}
		}
	
	
	}
	
	public static void Partilhar(){
		System.out.println("\n\n\t\t >>Partilha de musica<<");
	}
	
	public static void Download(){
		System.out.println("\n\n\t\t >>Download de musica<<");
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		
		System.out.println("\n\n Nome da musica>> ");
		String escolha;
		String[] respostas = new String[256];
		
		try{
			escolha = reader.readLine();
		}catch(Exception a1){
			System.out.println("Erro a escrever");
			return;
		}
		
		while(true){
			try{
				respostas = h.Find(escolha,"musica",c);
				break;
			} catch (Exception a2) {
				BackUp(false);
			}
		}
		
		if(respostas[0].compareTo("none")==0) System.out.println("Nada encontrado para: " + escolha);
		else{
			int pos = 0;
			while(respostas[pos] != null){
				System.out.println(pos + ". -> " + respostas[pos+1]);
			}
			Scanner sc = new Scanner(System.in);
			System.out.println("0. -> Back");
			int numero = sc.nextInt();
			if(numero!=0){
				String localizacao;
				String[] endereco = new String[2];
				System.out.println("localizacao de destino: ");
				try{
					localizacao = reader.readLine();
				}catch(Exception a3){
					System.out.println("Erro a escrever");
					return;
				}
				while(true){
					try{
						endereco = h.TransferMusic(online.GetNome());
						break;
					} catch (Exception a2) {
						BackUp(false);
					}
				}
				Socket s = null;
				while(true){
					try{
						s = new Socket(endereco[0],Integer.parseInt(endereco[1]));
						DataInputStream in = new DataInputStream(s.getInputStream());
						int musica = in.read();
						//guardar em localizacao
					}catch(Exception c){
						BackUp(false);
					}
				}
				
			}
		}
	}
	
	public static void GivePrev(){
		Scanner sc = new Scanner(System.in);
		System.out.println("\n\n\t\t >>Dar previlegios<<");
		System.out.printf("\nNome do utilizador a dar previlegios: ");
		String user = sc.nextLine();
		while(true){
			try{
				h.GivePriv(online.IsEditor(), user, c);
				Thread.sleep(500);
				break;
			} catch (Exception re) {
				BackUp(false);
			}
		}
		MainScreen();
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
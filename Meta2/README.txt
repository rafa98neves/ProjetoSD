Abrir a pasta exportada do ficheiro DropMusic no programa Intellij IDEA.

Abrir as definições do projeto, Libraries, e assegurar que fazemos as seguintes 
importações:
 - (from: Maven) javassist:javassist:3.11.0.GA
 - (from: Maven) javax.servlet:jstl:1.2
 - (from: Maven) javax.websocket:javax.websocket-api:1.11
 - (from: Maven) org.apache.struts:struts2-spring-plugin:2.5.14.1
 - (Java) (Diretoria do Projeto)\web\WEB-INF\lib

Ainda dentro das definições de projeto clicar sobre todos os elementos que se encontram 
na tabela do lado direito de modo a seguirem para a tabela do lado esquerdo.

Dentro da lib que é importada já se encontram os ficheiros necessários para efectuar a
ligação à base de dados.

Se por algum motivo existir erros de importação de ficheiros o necessário a fazer é abrir
o menu File e selecionar a opção 'Invalidate Cache / Restart'
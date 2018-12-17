package action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import util.scribejava.apis.DropBoxApi;
import util.scribejava.apis.DropBoxApi2;
import util.scribejava.core.builder.ServiceBuilder;
import util.scribejava.core.model.OAuthRequest;
import util.scribejava.core.model.Response;
import util.scribejava.core.model.Token;
import util.scribejava.core.model.Verb;
import util.scribejava.core.oauth.OAuthService;

import java.io.*;
import java.util.Map;

public class UploadAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private static final String API_APP_KEY = "ay4b0xio8wtgpja";
	private static final String API_APP_SECRET = "z7dzhg7ihti9w3x";
	private String dir = null, musica = null;

	@Override
	public String execute() {
		if(session.containsKey("InDrop")) {
			if ((boolean) session.get("InDrop") == false) {
				return "drop";
			}
			if (this.musica != null && !musica.equals("") && !musica.equals("") && this.musica != null) {
				setDir(dir);
				setMusica(musica);
				if(Upload()) return SUCCESS;
				else return "failed";
			}
		}
		return "drop";
	}

	public boolean Upload(){
		OAuthService service = new ServiceBuilder()
				.provider(DropBoxApi2.class)
				.apiKey(API_APP_KEY)
				.apiSecret(API_APP_SECRET)
				.callback("http://localhost:8080/LogInDropBox_Callback.action")
				.build();

		Token accessToken = (Token) session.get("token");
		boolean answer = addFile("DropMusic/"+musica+".mp3", service, accessToken);
		return answer;
	}
	public void setDir(String dir){
		this.dir=dir;
	}

	public String getDir(){
		return dir;
	}

	public void setMusica(String musica){
		this.musica=musica;
	}

	public String getMusica(){
		return musica;
	}

	private boolean addFile(String path, OAuthService service, Token accessToken) {
		OAuthRequest request = new OAuthRequest(Verb.POST, "https://content.dropboxapi.com/2/files/upload\n", service);
		request.addHeader("Content-Type",  "application/octet-stream");
		request.addHeader("Authorization", "Bearer " + accessToken.getToken());
		request.addHeader("Dropbox-API-Arg",
				"{\"path\": \"/"+path+"\", " +
						"\"mode\": \"add\", " +
						"\"autorename\": true, " +
						"\"mute\": false, " +
						"\"strict_conflict\": false}");

		String file = readFile();
		request.addPayload(file);
		Response response = request.send();
		return true;
	}

	public String readFile() {
		String filedata;
		try {
			File f = new File(this.dir);
			FileInputStream in = new FileInputStream(f);
			int size = in.available();
			byte c[] = new byte[size];
			for (int i = 0; i < size; i++) {
				c[i] = (byte) in.read();
			}
			filedata = new String(c, "utf-8");
		} catch (Exception c) {
			return null;
		}
		return filedata;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}

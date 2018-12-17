package services;

import model.DownloadModel;
import model.interfaces.SearchModel;
import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import util.scribejava.apis.DropBoxApi2;
import util.scribejava.core.builder.ServiceBuilder;
import util.scribejava.core.model.OAuthRequest;
import util.scribejava.core.model.Response;
import util.scribejava.core.model.Token;
import util.scribejava.core.model.Verb;
import util.scribejava.core.oauth.OAuthService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DownloadService implements services.interfaces.SearchService, SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private static final String API_APP_KEY = "ay4b0xio8wtgpja";
    private static final String API_APP_SECRET = "z7dzhg7ihti9w3x";

    public DownloadService() { }

    @Override
    public List<Object> search(SearchModel query, Map<String, Object> Session) {
        System.out.println("2");
        if (query instanceof DownloadModel) {
            System.out.println("1");
            String[] respostas;

            List<Object> results = new ArrayList<Object>();
            JSONObject files = ListFiles();
            for(int i=0; i<files.size();i++)
                results.add(files.get(i));
            return results;
        }
        return null;
    }

    public JSONObject ListFiles(){
        String path = "DropMusic";
        OAuthService service = new ServiceBuilder()
                .provider(DropBoxApi2.class)
                .apiKey(API_APP_KEY)
                .apiSecret(API_APP_SECRET)
                .callback("http://localhost:8080/LogInDropBox_Callback.action")
                .build();

        Token accessToken = (Token) session.get("token");

        accessToken = new Token( accessToken.getToken(), "");
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.dropboxapi.com/2/files/list_folder", service);
        request.addHeader("authorization", "Bearer " + accessToken.getToken());
        request.addPayload("{\n" +
                "    \"path: /"+path+", " +
                "    \"recursive\": false," +
                "    \"include_media_info\": false," +
                "    \"include_deleted\": false," +
                "    \"include_has_explicit_shared_members\": false," +
                "    \"include_mounted_folders\": true" +
                "}");

        request.addHeader("Content-Type",  "application/json");
        Response response = request.send();

        System.out.println("Got it! Lets see what we found...");
        System.out.println("HTTP RESPONSE: =============");
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        System.out.println("END RESPONSE ===============");

        JSONObject rj = (JSONObject) JSONValue.parse(response.getBody());
        JSONArray contents = (JSONArray) rj.get("entries");
        JSONObject item = new JSONObject();
        for (int i=0; i<contents.size(); i++) {
            System.out.println("...." + contents.get(i));
            item = (JSONObject) contents.get(i);
        }
        return item;
    }


    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}

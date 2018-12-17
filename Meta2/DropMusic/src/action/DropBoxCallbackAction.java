package action;

import com.opensymphony.xwork2.ActionContext;
import model.interfaces.HeyBean;
import sun.awt.HeadlessToolkit;
import util.scribejava.core.builder.ServiceBuilder;
import util.scribejava.core.exceptions.OAuthException;
import util.scribejava.core.model.*;
import util.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import util.scribejava.apis.DropBoxApi2;

import java.util.Map;
import java.util.Scanner;

public class DropBoxCallbackAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private static final String API_APP_KEY = "ay4b0xio8wtgpja";
    private static final String API_APP_SECRET = "z7dzhg7ihti9w3x";
    public static String code;

    @Override
    public String execute() {
        Scanner in = new Scanner(System.in);

        OAuthService service = new ServiceBuilder()
                .provider(DropBoxApi2.class)
                .apiKey(API_APP_KEY)
                .apiSecret(API_APP_SECRET)
                .callback("http://localhost:8080/LogInDropBox_Callback.action")
                .build();

        try {
            Verifier verifier = new Verifier(code);
            Token accessToken = service.getAccessToken(null, verifier);
            Map<String, Object> session = ActionContext.getContext().getSession();
            session.put("token",accessToken);
            HeyBean.AddToken((String) session.get("ID"), accessToken.getToken());
            session.put("InDrop",true);

        } catch(OAuthException e) {
            e.printStackTrace();
            return "error";
        } finally {
            in.close();
        }
        return "success";
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}

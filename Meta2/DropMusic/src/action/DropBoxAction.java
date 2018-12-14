package action;

import com.opensymphony.xwork2.ActionContext;
import util.scribejava.core.builder.ServiceBuilder;
import util.scribejava.core.exceptions.OAuthException;
import util.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;

import util.scribejava.apis.DropBoxApi2;

import java.util.Map;
import java.util.Scanner;

public class DropBoxAction extends ActionSupport{
    private static final long serialVersionUID = 4L;
    private static final String API_APP_KEY = "ay4b0xio8wtgpja";
    private static final String API_APP_SECRET = "z7dzhg7ihti9w3x";
    private static final String API_USER_TOKEN = "";
    private static String dropUrl;

    @Override
    public String execute() {

        OAuthService service = new ServiceBuilder()
                .provider(DropBoxApi2.class)
                .apiKey(API_APP_KEY)
                .apiSecret(API_APP_SECRET)
                .callback("http://localhost:8080/LogInDropBox_Callback.action")
                .build();

        try {
             setDropUrl(service.getAuthorizationUrl(null));
             return "redirect";
        } catch(OAuthException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public void setDropUrl(String dropUrl){
        this.dropUrl = dropUrl;
    }

    public String getDropUrl(){
        return dropUrl;
    }

}

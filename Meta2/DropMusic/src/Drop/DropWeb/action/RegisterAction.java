package Drop.DropWeb.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;
import Drop.DropWeb.model.HeyBean;

public class RegisterAction extends ActionSupport implements SessionAware{
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String username = null, password = null;

    @Override
    public String execute() {
        if(this.username != null && !username.equals("") && !password.equals("") && this.password != null) {
            if(HeyBean.CheckUser(username,password) == true) return SUCCESS;
            else return NONE;
        }
        else
            return NONE;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}




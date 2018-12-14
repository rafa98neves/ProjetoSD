package action;

import com.opensymphony.xwork2.ActionSupport;
import model.interfaces.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class GiveAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String username = null;

    @Override
    public String execute() {
        if(this.username != null) {

            if(HeyBean.GivePerm((String) session.get("ID"), username)) return "success";
            else return "failed";
        }
        else
            return "failed";
    }

    public void setUsername(String username){
        this.username=username;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = session;
    }
}

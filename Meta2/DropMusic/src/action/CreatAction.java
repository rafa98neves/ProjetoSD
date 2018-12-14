package action;

import com.opensymphony.xwork2.ActionSupport;
import model.interfaces.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class CreatAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String creating = null, info1 = null, info2 = null, info3 = null;

    @Override
    public String execute() {
        if(this.creating != null && this.info1 != null && this.info2 != null && this.info3 != null) {
            if(HeyBean.Cria((String) session.get("ID"),creating,info1,info2,info3)) return "success";
            else return "failed";
        }
        else
            return "failed";
    }

    public void setCreating(String creating){
        this.creating=creating;
    }

    public void setInfo1(String info1){
        this.info1=info1;
    }

    public void setInfo2(String info2){
        this.info2=info2;
    }

    public void setInfo3(String info3){
        this.info3=info3;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}

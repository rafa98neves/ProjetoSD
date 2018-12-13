package action;

import com.opensymphony.xwork2.ActionSupport;
import model.interfaces.HeyBean;

public class GiveAction extends ActionSupport{
    private String username = null;

    @Override
    public String execute() {
        if(this.username != null) {
            if(HeyBean.GivePerm(username)) return "success";
            else return "failed";
        }
        else
            return "failed";
    }

    public void setUsername(String username){
        this.username=username;
    }

}

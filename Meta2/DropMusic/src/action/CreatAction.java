package action;

import com.opensymphony.xwork2.ActionSupport;
import model.interfaces.HeyBean;

public class CreatAction extends ActionSupport{
    private String creating = null, info1 = null, info2 = null, info3 = null;

    @Override
    public String execute() {
        if(this.creating != null && this.info1 != null && this.info2 != null && this.info3 != null) {
            if(HeyBean.Cria(creating,info1,info2,info3)) return "success";
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

}

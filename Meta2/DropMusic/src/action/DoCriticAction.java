package action;

import com.opensymphony.xwork2.ActionSupport;
import model.interfaces.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class DoCriticAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String pontuacao = null, Critica = null, alvo = null;

    @Override
    public String execute() {
        alvo = (String) session.get("LastSearched");
        if(this.pontuacao != null && !pontuacao.equals("") && this.Critica != null && !Critica.equals("")) {
            if(HeyBean.Critica((String) session.get("ID"),(String) session.get("username"),Integer.parseInt(pontuacao),Critica,alvo)) return "success";
            else return "failed";
        }
        else
            return "failed";
    }

    public void setPontuacao(String p){
        this.pontuacao=p;
    }

    public void setCritica(String t){
        this.Critica=t;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}

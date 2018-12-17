package action;

import com.opensymphony.xwork2.ActionSupport;
import model.interfaces.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.security.acl.LastOwnerException;
import java.util.Map;

public class AlterarDetalhesAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String info1 = null, info2 = null, info3 = null, info4 = null, tipo = null, alvo = null;

	@Override
	public String execute() {
		setAlvo();
		setTipo();
		if(tipo != null && !tipo.equals("") && alvo != null && !alvo.equals("")) {
			if (info1 != null && !info1.equals("")) {
				if (!HeyBean.ChangeInfo((String) session.get("ID"),(String) session.get("username"), tipo, alvo, "nome", info1)) return "failed";
			}
			if (info2 != null && !info2.equals("")) {
				if (!HeyBean.ChangeInfo((String) session.get("ID"),(String) session.get("username"), tipo, alvo, "nome", info2)) return "failed";
			}
			if (info3 != null && !info3.equals("")) {
				if (!HeyBean.ChangeInfo((String) session.get("ID"),(String) session.get("username"), tipo, alvo, "nome", info3)) return "failed";
			}
			if (info4 != null && !info4.equals("")) {
				if (!HeyBean.ChangeInfo((String) session.get("ID"),(String) session.get("username"), tipo, alvo, "nome", info4)) return "failed";
			}
			return SUCCESS;
		}
		return "failed";
	}

	public void setInfo1(String info){
		this.info1=info;
	}
	public void setInfo2(String info){
		this.info2=info;
	}
	public void setInfo3(String info){
		this.info3=info;
	}
	public void setInfo4(String info){
		this.info4=info;
	}
	public void setTipo(){
		if(session.containsKey("LastSearchType"))
			this.tipo=(String) session.get("LastSearchType");
	}
	public void setAlvo(){
		if(session.containsKey("LastSearched"))
			this.alvo=(String) session.get("LastSearched");
	}


	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}

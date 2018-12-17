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
	private String item_1 = null, item_2 = null, item_3 = null, item_4 = null;
	@Override
	public String execute() {
		setAlvo();
		setTipo();
		if(tipo != null && !tipo.equals("") && alvo != null && !alvo.equals("")) {
			if (info1 != null && !info1.equals("")) {
				if (!HeyBean.ChangeInfo((String) session.get("ID"),(String) session.get("username"), tipo, alvo, item_1, info1)) return "failed";
			}
			if (info2 != null && !info2.equals("")) {
				if (!HeyBean.ChangeInfo((String) session.get("ID"),(String) session.get("username"), tipo, alvo, item_2, info2)) return "failed";
			}
			if (info3 != null && !info3.equals("")) {
				if (!HeyBean.ChangeInfo((String) session.get("ID"),(String) session.get("username"), tipo, alvo, item_3, info3)) return "failed";
			}
			if (info4 != null && !info4.equals("")) {
				if (!HeyBean.ChangeInfo((String) session.get("ID"),(String) session.get("username"), tipo, alvo, item_4, info4)) return "failed";
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

	public void setItem_1(String item){
		this.item_1=item;
	}
	public void setItem_2(String item){
		this.item_2=item;
	}
	public void setItem_3(String item){
		this.item_3=item;
	}
	public void setItem_4(String item){
		this.item_4=item;
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

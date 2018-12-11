package action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;
import model.interfaces.HeyBean;

public class LoginAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null, password = null;

	@Override
	public String execute() {
		if(this.username != null && !username.equals("") && !password.equals("") && this.password != null) {
			String[] respostas = HeyBean.CheckUser(username,password,false);
			if(respostas[0].compareTo("true") == 0) {
				getHeyBean().setUsername(username);
				session.put("username",username);
				session.put("loggedin",true);
				if (respostas[2].compareTo("true") == 0) session.put("editor",true);
				else session.put("editor",false);
				return "success";
			}
			else return "failed";
		}
		else
			return "failed";
	}

	public HeyBean getHeyBean() {
		if(!session.containsKey("heyBean"))
			this.setHeyBean(new HeyBean());
		return (HeyBean) session.get("heyBean");
	}

	public void setHeyBean(HeyBean heyBean) {
		this.session.put("heyBean", heyBean);
	}

	public void setUsername(String username){
		this.username=username;
	}
	public void setPassword(String password){
		this.password=password;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}

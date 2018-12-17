package action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
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
				session.put("ID",respostas[1]);
				session.put("loggedin",true);

				//Ve se o utilizador é editor
                if (respostas[2].compareTo("true") == 0) session.put("editor",true);
                else session.put("editor",false);

                //Ve se o utilizador tem notificacoes pendentes
				String[] notificacoes = HeyBean.CheckNotifications(respostas[1]);
				if(notificacoes != null) {
					session.put("n_notificacoes", notificacoes[0]);
					if (Integer.parseInt(notificacoes[0]) > 0) {
						ArrayList not = new ArrayList();
						int i = 1;
						while (notificacoes[i] != null && !notificacoes[i].equals("")){
							not.add(notificacoes[i]);
							i++;
						}
						session.put("noti", not);
					}
				}
				else session.put("n_notificacoes",0);

//                //Ve se o utilizador tem conta DropBox associada
//				String token = HeyBean.GetToken(respostas[1]);
//				if(token != null){
//				    session.put("InDrop",true);
//				    session.put("token",token);
//                }
//                else session.put("InDrop",false);
//                System.out.println("RETURNOU");
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

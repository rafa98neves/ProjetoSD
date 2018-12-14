package action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import util.DropBoxRestClient;

import java.util.Map;

public class DropBoxAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String creating = null, info1 = null, info2 = null, info3 = null;

    @Override
    public String execute() {
        DropBoxRestClient user = new DropBoxRestClient();
        return SUCCESS;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}

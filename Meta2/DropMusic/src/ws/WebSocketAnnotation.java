package ws;

import model.interfaces.HeyBean;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws")
public class WebSocketAnnotation {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private static final Set<WebSocketAnnotation> users = new CopyOnWriteArraySet<>();
    private String username;
    private Session session;

    public WebSocketAnnotation(){}

    @OnOpen
    public void start(Session session) {
        this.session = session;
    }

    @OnClose
    public void end() {
        users.remove(this);
    }

    @OnMessage
    public void receiveMessage(String message) {
        if(message.contains("#")){
            String[] divider = message.split(" ");
            this.username = divider[1];
            users.add(this);
            for(WebSocketAnnotation s : users)
                System.out.println(s.username);
        }
        else sendMessage(message);
    }
    
    @OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }

    private void sendMessage(String text) {
        String[] divide = new String[2];
        divide = text.split(" : ");
    	try {
    	    boolean flag = false;
			for(WebSocketAnnotation s : users){
			    if(s.username.compareTo(divide[0])==0){
			        s.session.getBasicRemote().sendText(divide[1]);
			        flag = true;
			        break;
                }
            }
            if(!flag) HeyBean.GivePrev("0",divide[0]);
		} catch (IOException e) {
			try {
				this.session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    }
}

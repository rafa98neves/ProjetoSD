package util.scribejava.apis;

import util.scribejava.core.builder.api.DefaultApi10a;
import util.scribejava.core.model.Token;
import util.scribejava.core.services.PlaintextSignatureService;
import util.scribejava.core.services.SignatureService;

public class YammerApi extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "https://www.yammer.com/oauth/authorize?oauth_token=%s";

    @Override
    public String getRequestTokenEndpoint() {
        return "https://www.yammer.com/oauth/request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.yammer.com/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }

    @Override
    public SignatureService getSignatureService() {
        return new PlaintextSignatureService();
    }
}

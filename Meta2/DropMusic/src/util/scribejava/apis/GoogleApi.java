package util.scribejava.apis;

import util.scribejava.core.builder.api.DefaultApi10a;
import util.scribejava.core.model.Token;
import util.scribejava.core.model.Verb;

public class GoogleApi extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.google.com/accounts/OAuthGetAccessToken";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://www.google.com/accounts/OAuthGetRequestToken";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public Verb getRequestTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }
}

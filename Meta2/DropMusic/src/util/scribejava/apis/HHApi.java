package util.scribejava.apis;

import util.scribejava.core.builder.api.DefaultApi20;
import util.scribejava.core.extractors.AccessTokenExtractor;
import util.scribejava.core.extractors.JsonTokenExtractor;
import util.scribejava.core.model.OAuthConfig;
import util.scribejava.core.model.OAuthConstants;
import util.scribejava.core.model.Verb;
import util.scribejava.core.oauth.OAuthService;

import util.scribejava.apis.service.HHOAuthServiceImpl;

public class HHApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://m.hh.ru/oauth/authorize?response_type=code&client_id=%s";
    private static final String TOKEN_URL = "https://m.hh.ru/oauth/token?grant_type=" + OAuthConstants.AUTHORIZATION_CODE;

    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return String.format(AUTHORIZE_URL, config.getApiKey());
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return new HHOAuthServiceImpl(this, config);
    }
}

package util.scribejava.apis;

import util.scribejava.core.builder.api.DefaultApi20;
import util.scribejava.core.extractors.AccessTokenExtractor;
import util.scribejava.core.extractors.JsonTokenExtractor;
import util.scribejava.core.model.OAuthConfig;
import util.scribejava.core.model.Verb;
import util.scribejava.core.oauth.OAuthService;
import util.scribejava.core.utils.OAuthEncoder;
import util.scribejava.core.utils.Preconditions;
import util.scribejava.apis.service.TutByOAuthServiceImpl;

public class TutByApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "http://profile.tut.by/auth?client_id=%s&response_type=code&redirect_uri=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "http://profile.tut.by/getToken";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Valid url is required for a callback. Tut.by does not support OOB");
        return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return new TutByOAuthServiceImpl(this, config);
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }
}

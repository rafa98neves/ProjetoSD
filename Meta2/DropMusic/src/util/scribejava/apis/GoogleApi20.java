package util.scribejava.apis;

import util.scribejava.apis.google.GoogleJsonTokenExtractor;
import util.scribejava.apis.service.GoogleOAuthServiceImpl;
import util.scribejava.core.builder.api.DefaultApi20;
import util.scribejava.core.extractors.AccessTokenExtractor;
import util.scribejava.core.model.OAuthConfig;
import util.scribejava.core.model.OAuthConstants;
import util.scribejava.core.model.Verb;
import util.scribejava.core.oauth.OAuthService;
import util.scribejava.core.utils.OAuthEncoder;
import util.scribejava.core.utils.Preconditions;

public class GoogleApi20 extends DefaultApi20 {

    private static final String AUTHORIZE_URL
            = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=%s&redirect_uri=%s&scope=%s";

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://accounts.google.com/o/oauth2/token";
    }

    @Override
    public String getAuthorizationUrl(final OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Must provide a valid url as callback. Google+ does not support OOB");
        final StringBuilder sb = new StringBuilder(String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(
                config.getCallback()), OAuthEncoder.encode(config.getScope())));

        final String state = config.getState();
        if (state != null) {
            sb.append('&').append(OAuthConstants.STATE).append('=').append(OAuthEncoder.encode(state));
        }
        return sb.toString();
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new GoogleJsonTokenExtractor();
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return new GoogleOAuthServiceImpl(this, config);
    }

}

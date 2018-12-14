package util.scribejava.apis;

import util.scribejava.apis.service.DoktornaraboteOAuthServiceImpl;
import util.scribejava.core.builder.api.DefaultApi20;
import util.scribejava.core.extractors.AccessTokenExtractor;
import util.scribejava.core.extractors.JsonTokenExtractor;
import util.scribejava.core.model.OAuthConfig;
import util.scribejava.core.model.OAuthConstants;
import util.scribejava.core.model.Verb;
import util.scribejava.core.oauth.OAuthService;
import util.scribejava.core.utils.OAuthEncoder;
import util.scribejava.core.utils.Preconditions;

public class DoktornaraboteApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "http://auth.doktornarabote.ru/OAuth/Authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=%s";
    private static final String TOKEN_URL = "http://auth.doktornarabote.ru/OAuth/Token";

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(
            config.getCallback(),
            "Must provide a valid url as callback. Doktornarabote does not support OOB");
        final StringBuilder sb = new StringBuilder(
            String.format(
                AUTHORIZE_URL,
                config.getApiKey(),
                OAuthEncoder.encode(config.getCallback()),
                OAuthEncoder.encode(config.getScope())
            )
        );

        final String state = config.getState();
        if (state != null) {
            sb.append('&').append(OAuthConstants.STATE).append('=').append(OAuthEncoder.encode(state));
        }
        return sb.toString();
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return new DoktornaraboteOAuthServiceImpl(this, config);
    }
}

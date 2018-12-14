package util.scribejava.apis;

import util.scribejava.core.builder.api.DefaultApi20;
import util.scribejava.core.extractors.AccessTokenExtractor;
import util.scribejava.core.extractors.JsonTokenExtractor;
import util.scribejava.core.model.OAuthConfig;
import util.scribejava.core.model.OAuthConstants;
import util.scribejava.core.utils.OAuthEncoder;
import util.scribejava.core.utils.Preconditions;

public class LiveApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://oauth.live.com/authorize?client_id=%s&redirect_uri=%s&response_type=code";
    private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://login.live.com/oauth20_token.srf?grant_type=" + OAuthConstants.AUTHORIZATION_CODE;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Live does not support OOB");

        // Append scope if present
        if (config.hasScope()) {
            return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.
                    getScope()));
        } else {
            return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
        }
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }
}

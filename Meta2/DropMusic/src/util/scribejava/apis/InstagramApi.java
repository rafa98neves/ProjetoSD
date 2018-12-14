package util.scribejava.apis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.scribejava.core.builder.api.DefaultApi20;
import util.scribejava.core.exceptions.OAuthException;
import util.scribejava.core.extractors.AccessTokenExtractor;
import util.scribejava.core.model.AbstractRequest;
import util.scribejava.core.model.OAuthConfig;
import util.scribejava.core.model.OAuthConstants;
import util.scribejava.core.model.OAuthRequest;
import util.scribejava.core.model.Response;
import util.scribejava.core.model.Token;
import util.scribejava.core.model.Verb;
import util.scribejava.core.model.Verifier;
import util.scribejava.core.oauth.OAuth20ServiceImpl;
import util.scribejava.core.oauth.OAuthService;
import util.scribejava.core.utils.OAuthEncoder;
import util.scribejava.core.utils.Preconditions;

public class InstagramApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://api.instagram.com/oauth/authorize/?"+
            "client_id=%s&" +
            "scope=%s&" +
            "redirect_uri=%s&" +
            "response_type=code&" +
            "access_type=offline";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.instagram.com/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return String.format(AUTHORIZE_URL,
                config.getApiKey(),
                OAuthEncoder.encode(config.getScope()),
                OAuthEncoder.encode(config.getCallback()));

    }
    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new AccessTokenExtractor() {
            private Pattern accessTokenPattern =
                    Pattern.compile("\"access_token\"\\s*:\\s*\"(\\S*?)\"");

            @Override
            public Token extract(String response) {
                Preconditions.checkEmptyString(response, "Cannot extract a token from a null or empty String");
                Matcher matcher = accessTokenPattern.matcher(response);
                if (matcher.find()) {
                    return new Token(matcher.group(1), "", response);
                } else {
                    throw new OAuthException("Cannot extract an acces token. Response was: " + response);
                }
            }
        };
    }


    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public OAuthService createService(final OAuthConfig config) {
        return new OAuth20ServiceImpl(this, config) {
            public Token getAccessToken(Token requestToken, Verifier verifier) {
                OAuthRequest request = new OAuthRequest(getAccessTokenVerb(),
                        getAccessTokenEndpoint(), this);
                request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
                request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
                request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
                request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
                request.addBodyParameter("grant_type", "authorization_code");
                if (config.hasScope()) {
                    request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());
                }
                Response response = request.send();
                return getAccessTokenExtractor().extract(response.getBody());
            }
            
            @Override
            public void signRequest(Token accessToken, AbstractRequest request) {
            	System.out.println(accessToken + " token");
                request.addQuerystringParameter("access_token", accessToken.getToken());
            	System.out.println("asking for " + request.getCompleteUrl());
            }
        };
    }
}
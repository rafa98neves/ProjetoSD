package util.scribejava.apis.service;

import util.scribejava.core.builder.api.DefaultApi20;
import util.scribejava.core.model.AbstractRequest;
import util.scribejava.core.model.OAuthConfig;
import util.scribejava.core.model.Token;
import util.scribejava.core.oauth.OAuth20ServiceImpl;

public class HHOAuthServiceImpl extends OAuth20ServiceImpl {

    public HHOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    public void signRequest(Token accessToken, AbstractRequest request) {
        request.addHeader("Authorization", "Bearer " + accessToken.getToken());
    }
}

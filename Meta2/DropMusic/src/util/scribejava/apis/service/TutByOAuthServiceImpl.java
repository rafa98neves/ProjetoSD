package util.scribejava.apis.service;

import util.scribejava.core.builder.api.DefaultApi20;
import util.scribejava.core.model.AbstractRequest;
import util.scribejava.core.model.OAuthConfig;
import util.scribejava.core.model.OAuthConstants;
import util.scribejava.core.model.Token;
import util.scribejava.core.oauth.OAuth20ServiceImpl;

public class TutByOAuthServiceImpl extends OAuth20ServiceImpl {

    public TutByOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    public void signRequest(Token accessToken, AbstractRequest request) {
        request.addQuerystringParameter(OAuthConstants.TOKEN, accessToken.getToken());
    }

}

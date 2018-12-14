package util.scribejava.core.services;

import java.io.UnsupportedEncodingException;
import util.scribejava.codec.binary.Base64;
import util.scribejava.core.exceptions.OAuthSignatureException;

public class CommonsEncoder extends Base64Encoder {

    @Override
    public String encode(byte[] bytes) {
        try {
            return new String(Base64.encodeBase64(bytes), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new OAuthSignatureException("Can't perform base64 encoding", e);
        }
    }

    @Override
    public String getType() {
        return "CommonsCodec";
    }

    public static boolean isPresent() {
        try {
            Class.forName("util.scribejava.codec.binary.Base64");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}

package jiandgyu.jimechu.config.security;

import lombok.Data;

@Data
public class TokenInfo {

    private String grantType;
    private String accessToken;

    public TokenInfo(String grantType, String accessToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
    }
}

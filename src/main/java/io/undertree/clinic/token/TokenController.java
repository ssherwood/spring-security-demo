package io.undertree.clinic.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@Profile("cloud")
public class TokenController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OAuth2RestTemplate oauth2RestTemplate;

    @Value("${security.oauth2.resource.userInfoUri}")
    private String userInfoUri;

    @Value("${resourceServerUrl}")
    private String resourceServerUrl;

    @RequestMapping(value = "/secured/access_token")
    public String showAccessToken(OAuth2Authentication principal) {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) principal.getDetails();

        Jwt decodedToken = JwtHelper.decode(details.getTokenValue());
        return "<html>\n" +
                "<body>\n" +
                "\n" +
                "<form action=\"/logout\" method=\"POST\">\n" +
                "    <button id=\"logout\" type=\"submit\">Logout</button>\n" +
                "</form>\n" +
                "\n" +
                "<pre>" + prettyPrint(decodedToken.getClaims()) + "</pre>\n" +
                "\n" +
                "<pre>" + details.getTokenValue() + "</pre>\n" +
                "</body>\n" +
                "</html>";
    }

    @RequestMapping(value = "/secured/userinfo")
    public String showUserinfo() {
        Map<String, Object> userInfoResponse = oauth2RestTemplate.getForObject(userInfoUri, Map.class);

        return "<html>\n" +
                "<body>\n" +
                "\n" +
                "<form action=\"/logout\" method=\"POST\">\n" +
                "    <button id=\"logout\" type=\"submit\">Logout</button>\n" +
                "</form>\n" +
                "\n" +
                "<pre>" + prettyPrint(userInfoResponse) + "</pre>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }

    private String prettyPrint(String decodedTokenJson) {
        try {
            return this.objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(this.objectMapper.readValue(decodedTokenJson, Object.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String prettyPrint(Object objectMap)  {
        try {
            return prettyPrint(this.objectMapper.writeValueAsString(objectMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.ndgndg91.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndgndg91.auth.AuthInfo;
import com.ndgndg91.controller.LoginInterface.Login;
import com.ndgndg91.model.MemberDTO;
import com.ndgndg91.service.MemberService;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import static com.ndgndg91.model.enums.LoginType.GOOGLE;

@Log4j
@Controller
public class GoogleController implements Login {

    @Autowired
    private MemberService memberService;

    @Inject
    private AuthInfo authInfo;

    @Autowired
    @Qualifier("googleOAuth2Parameters")
    private OAuth2Parameters googleOAuth2Parameters;

    @RequestMapping(value = "/auth/google/callback")
    public String doSessionAssignActionPage(HttpServletRequest request, @RequestParam Map<String, Object> paramMap,
                                            HttpSession session) throws Exception {
        for (String key : paramMap.keySet()) {
            log.info(key + " : " + paramMap.get(key));
        }
        String code = request.getParameter("code");
        log.info(code);

        //RestTemplate을 사용하여 Access Token 및 profile을 요청한다.
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("code", code);
        parameters.add("client_id", authInfo.getClientId());
        parameters.add("client_secret", authInfo.getClientSecret());
        parameters.add("redirect_uri", googleOAuth2Parameters.getRedirectUri());
        parameters.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange("https://www.googleapis.com/oauth2/v4/token", HttpMethod.POST, requestEntity, Map.class);
        Map responseMap = responseEntity.getBody();

        // id_token 라는 키에 사용자가 정보가 존재한다.
        // 받아온 결과는 JWT (Json Web Token) 형식으로 받아온다. 콤마 단위로 끊어서 첫 번째는 현 토큰에 대한 메타 정보, 두 번째는 우리가 필요한 내용이 존재한다.
        // 세번째 부분에는 위변조를 방지하기 위한 특정 알고리즘으로 암호화되어 사이닝에 사용한다.
        //Base 64로 인코딩 되어 있으므로 디코딩한다.

        for (Object key : responseMap.keySet()) {
            log.info(key + " : " + responseMap.get(key));
        }
        session.setAttribute("googleAccessToken", responseMap.get("access_token"));
        log.info(responseMap.get("access_token"));
        String[] tokens = ((String) responseMap.get("id_token")).split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String body = new String(decoder.decode(tokens[1]));

        log.info(tokens.length);
        for (int i = 0; i < tokens.length; i++){
            log.info(new String(decoder.decode(tokens[0]), StandardCharsets.UTF_8));
        }

        //Jackson을 사용한 JSON을 자바 Map 형식으로 변환
        ObjectMapper mapper = new ObjectMapper();
        Map result = mapper.readValue(body, Map.class);

        for (Object key : result.keySet()) {
            log.info(key + " : " + result.get(key));
        }

        Optional<MemberDTO> optionalMember = Optional.ofNullable(memberService.selectOneMeberByEmail(String.valueOf(result.get("email"))));
        if (!optionalMember.isPresent()) {
            MemberDTO newMember = new MemberDTO.Builder((String) result.get("at_hash"), (String) result.get("email"))
                    .nick((String) result.get("name"))
                    .loginType(GOOGLE.toString())
                    .pictureUrl((String) result.get("picture"))
                    .locale((String) result.get("locale"))
                    .build();
            memberService.insertMemberExcludePwParameter(newMember);
            setMemberToSession(newMember, session);
            return "redirect:/";
        }

        setMemberToSession(optionalMember.get(), session);
        return "redirect:/";
    }

    @RequestMapping(value = "/auth/google/logout")
    public String googleLogout(HttpSession session) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://accounts.google.com/o/oauth2/revoke?token="+session.getAttribute("googleAccessToken"));
        HttpResponse response = client.execute(post);
        log.info(response.getStatusLine().getStatusCode());
        log.info(response.getStatusLine().getReasonPhrase());
        session.removeAttribute("googleAccessToken");
        session.removeAttribute("loginUserInfo");
        session.invalidate();
        return "redirect:/";
    }
}

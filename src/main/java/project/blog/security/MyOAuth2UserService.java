package project.blog.security;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.support.incrementer.MySQLIdentityColumnMaxValueIncrementer;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.blog.main.user.User;
import project.blog.main.user.UserRepository;

import javax.annotation.meta.TypeQualifierNickname;
import java.lang.annotation.ElementType;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MyOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User user = super.loadUser(userRequest);

        //계정 -> sub
        //비밀번호 -> ""
        //email -> email
        //닉네임 -> name



        //로그인 분기점 설정
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        MysocialUser mysocialUser;

        switch (registrationId) {
            case "google" -> mysocialUser = googleService(user);
            case "naver" -> mysocialUser = naverService(user);
            case "kakao" -> mysocialUser = kakaoService(user);
            default -> throw new IllegalStateException("Unexpected value: " + registrationId);
        }

        User newUser = userRepository.findBySigninId(mysocialUser.getSub()).orElse(null);

        if(newUser == null) {
            newUser = new User();
            newUser.setSigninId(mysocialUser.getSub());
            newUser.setPassword(mysocialUser.getPass());
            newUser.setNickname(mysocialUser.getName());
            newUser.setEmail(mysocialUser.getEmail());
            newUser.setCreateDate(LocalDateTime.now());

            userRepository.save(newUser);
        }

        return super.loadUser(userRequest);

    }

    public MysocialUser googleService(OAuth2User user){
        String sub = user.getAttribute("sub");
        String pass = "";
        String name = user.getAttribute("name");
        String email = user.getAttribute("email");

        return new MysocialUser(sub, pass, name, email);
    }

    public MysocialUser naverService(OAuth2User user){
        Map<String, Object> response = user.getAttribute("response");
        String sub = response.get("id").toString();
        String pass = "";
        String name = response.get("name").toString();
        String email = response.get("email").toString();

        return new MysocialUser(sub, pass, name, email);
    }

    public MysocialUser kakaoService(OAuth2User user){
        String sub = user.getAttribute("id").toString();
        String pass = "";

        Map<String, Object> kakaoAccount = user.getAttribute("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String name = (String)profile.get("nickname");
        String email = (String)kakaoAccount.get("email");

        return new MysocialUser(sub, pass, name, email);
    }
}
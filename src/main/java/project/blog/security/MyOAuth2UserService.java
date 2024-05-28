package project.blog.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.blog.main.user.User;
import project.blog.main.user.UserRepository;

import java.time.LocalDateTime;

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

        String sub = user.getAttribute("sub");
        String pass = "";
        String name = user.getAttribute("name");
        String email = user.getAttribute("email");

        User newUser = userRepository.findBySigninId(sub).orElse(null);

        if(newUser == null) {
            newUser = new User();
            newUser.setSigninId(sub);
            newUser.setPassword(pass);
            newUser.setNickname(name);
            newUser.setEmail(email);
            newUser.setCreateDate(LocalDateTime.now());

            userRepository.save(newUser);
        }

        return super.loadUser(userRequest);
    }
}
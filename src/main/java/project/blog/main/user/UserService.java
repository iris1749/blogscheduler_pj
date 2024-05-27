package project.blog.main.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.blog.main.DataNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(String signinId, String password, String nickname, String email) {
        User user = new User();
        user.setSigninId(signinId);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname);
        user.setEmail(email);
        user.setCreateDate(LocalDateTime.now());

        return userRepository.save(user);
    }

    public User getUser(String signinId){
        Optional<User> siteUser = this.userRepository.findBySigninId(signinId);
        if (siteUser.isPresent()){
            return siteUser.get();
        } else{
            throw new DataNotFoundException("user not found");
        }
    }
}

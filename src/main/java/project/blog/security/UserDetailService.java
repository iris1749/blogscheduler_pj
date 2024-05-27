package project.blog.security;

import project.blog.main.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String signinId) throws UsernameNotFoundException {

        project.blog.main.user.User user = userRepository.findBySigninId(signinId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 아이디입니다.")
        );

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("USER");
        List<SimpleGrantedAuthority> authorities = List.of(authority); // 권한이 여러개일 경우 List.of()로 추가 가능

        return new User(user.getSigninId(), user.getPassword(), authorities); // 3가지 필수 인증 정보. 아이디, 비밀번호, 권한
    }
}

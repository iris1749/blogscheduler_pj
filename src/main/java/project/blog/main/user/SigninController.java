package project.blog.main.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SigninController {

    private final UserService userService;

    @GetMapping("/signin")
    public String signin(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "sign_form";
    }

    @PostMapping("/signin")
    public String signin(@Valid UserForm userForm, BindingResult bindingResult) {
        // 아이디와 비밀번호를 검증하고 로그인 처리
        if (bindingResult.hasErrors()) {
            // 유효성 검사 실패 시 에러 메시지 처리
            return "sign_form"; // 로그인 폼 페이지 다시 표시
        }

        // 로그인 성공 시 다음 동작 수행
        return "redirect:/"; // 로그인 후 이동할 페이지로 리다이렉트
    }
}

package project.blog.main.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class SignController {

    private static final Logger logger = LoggerFactory.getLogger(SignController.class);
    private final UserService userService;

    // Spring이 URL 매핑을 통해 이 메서드를 호출합니다.
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "sign_form";
    }

    // Spring이 URL 매핑을 통해 이 메서드를 호출합니다.
    @PostMapping("/signup")
    public String signup(@Valid UserForm userForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "sign_form";
        }

        try {
            userService.create(userForm.getSigninId(), userForm.getPassword(), userForm.getNickname(), userForm.getEmail());
        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation during signup", e);
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "sign_form";
        } catch (Exception e) {
            logger.error("Error during signup", e);
            bindingResult.reject("signupFailed", e.getMessage());
            return "sign_form";
        }

        return "redirect:/signin";
    }

    // Spring이 URL 매핑을 통해 이 메서드를 호출합니다.
    @GetMapping("/signin")
    public String showLoginForm(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/"; // 이미 로그인된 경우 홈페이지로 리디렉션
        }
        model.addAttribute("userForm", new UserForm());
        return "sign_form";
    }

}

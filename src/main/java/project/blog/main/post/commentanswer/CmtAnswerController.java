package project.blog.main.post.commentanswer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import project.blog.main.post.comment.Comment;
import project.blog.main.post.comment.CommentForm;
import project.blog.main.post.comment.CommentService;
import project.blog.main.user.User;
import project.blog.main.user.UserService;

import java.security.Principal;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class CmtAnswerController {

    private final CommentService commentService;
    private final CmtAnswerService cmtAnswerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createCmtAnswer(Model model, @PathVariable("id") Integer id, @Valid CmtAnswerForm cmtAnswerForm,
                                BindingResult bindingResult, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        User user = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("comment", comment);
            return "post_detail";
        }

        CmtAnswer cmtAnswer = this.cmtAnswerService.create(comment, cmtAnswerForm.getContent(), user);
        return String.format("redirect:/blog/posts_%s#comment_%s",
                comment.getPost().getId(), comment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String cmtAnswerModify(CmtAnswerForm cmtAnswerForm, @PathVariable("id") Integer id, Principal principal) {
        CmtAnswer cmtAnswer = this.cmtAnswerService.getCmtAnswer(id);
        if (!cmtAnswer.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        cmtAnswerForm.setContent(cmtAnswer.getContent());
        return "cmtAnswer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String cmtAnswerModify(CmtAnswerForm cmtAnswerForm, BindingResult bindingResult,
                                  @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "cmtAnswer_form";
        }
        CmtAnswer cmtAnswer = this.cmtAnswerService.getCmtAnswer(id);
        if (!cmtAnswer.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.cmtAnswerService.modify(cmtAnswer, cmtAnswerForm.getContent());

        return String.format("redirect:/blog/posts_%s#comment_%s",
                cmtAnswer.getComment().getPost().getId(), cmtAnswer.getComment().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String cmtAnswerDelete(Principal principal, @PathVariable("id") Integer id) {
        CmtAnswer cmtanswer = this.cmtAnswerService.getCmtAnswer(id);
        if (!cmtanswer.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.cmtAnswerService.delete(cmtanswer);

        return String.format("redirect:/blog/posts_%s#comment_%s",
                cmtanswer.getComment().getPost().getId(), cmtanswer.getComment().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String cmtAnswerLike(Principal principal, @PathVariable("id") Integer id) {
        CmtAnswer cmtAnswer = this.cmtAnswerService.getCmtAnswer(id);
        User user = this.userService.getUser(principal.getName());
        this.cmtAnswerService.vote(cmtAnswer, user);
        return String.format("redirect:/blog/posts_%s#comment_%s",
                cmtAnswer.getComment().getPost().getId(), cmtAnswer.getComment().getId());
    }

}

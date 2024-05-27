package project.blog.main.post.comment;

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
import project.blog.main.post.post.Post;
import project.blog.main.post.post.PostService;
import project.blog.main.user.User;
import project.blog.main.user.UserService;

import java.security.Principal;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    //댓글 생성 제어
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Integer id, @Valid CommentForm commentForm,
                               BindingResult bindingResult, Principal principal) {
        Post post = this.postService.getPost(id);
        User user = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "post_detail";
        }
        Comment comment = this.commentService.create(post,
                commentForm.getContent(), user);
        return String.format("redirect:/blog/posts_%s#comment_%s",
                comment.getPost().getId(), comment.getId());

    }

    //댓글 수정 수신제어
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String commentModify(CommentForm commentForm, @PathVariable("id") Integer id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        commentForm.setContent(comment.getContent());
        return "comment_form";
    }

    //댓글 수정 송신제어
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.commentService.modify(comment, commentForm.getContent());
        return String.format("redirect:/blog/posts_%s#comment_%s",
                comment.getPost().getId(), comment.getId());
    }

    //댓글 삭제 수신제어
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Integer id) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/blog/posts_%s",
                comment.getPost().getId());
    }

    //댓글 좋아요 수신제어
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/like/{id}")
    public String commentVote(Principal principal, @PathVariable("id") Integer id) {
        Comment comment = this.commentService.getComment(id);
        User user = this.userService.getUser(principal.getName());
        this.commentService.like(comment, user);
        return String.format("redirect:/blog/posts_%s#comment_%s",
                comment.getPost().getId(), comment.getId());
    }

}

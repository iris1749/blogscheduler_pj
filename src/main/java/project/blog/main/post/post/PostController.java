package project.blog.main.post.post;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.blog.main.user.User;
import project.blog.main.user.UserService;
import project.blog.main.post.post.PostService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/blog")
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;
    private final UserService userService;

//    user id별 게시글 구현
//    @GetMapping(value = "/{id}")
//    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm, CommentForm commentForm,
//                         @RequestParam(name = "sortoption", defaultValue = "latest") String sortoption,
//                         @RequestParam(name = "answer_page", defaultValue = "0") int answer_page) {
//
//        Question question = this.questionService.getQuestion(id);
//        questionService.updateView(id);
//        model.addAttribute("question", question);
//
//        // 정렬 옵션에 따라 다른 정렬 기준으로 페이지를 조회
//        Page<Answer> paging = this.answerService.getListByQuestion(question, answer_page, sortoption);
//        model.addAttribute("paging", paging);
//
//        // 각 답변에 대한 댓글을 가져와서 리스트에 추가
//        List<Comment> commentList = new ArrayList<>();
//        for (Answer answer : paging.getContent()) {
//            List<Comment> commentsForAnswer = this.commentService.getCommentsByAnswer(answer);
//            commentList.addAll(commentsForAnswer);
//        }
//        model.addAttribute("comment", commentList);
//
//        return "question_detail";
//    }

    @GetMapping("/posts")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "search_kw", defaultValue = "") String kw
                      ) {

        Page<Post> paging = this.postService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("search_kw", kw);
        return "question_list";
    }

    //post 작성 기능
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String postCreate(PostForm postForm) {
        return "post_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String questionCreate(@Valid PostForm postForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "post_form";
        }
        User user = this.userService.getUser(principal.getName());
        this.postService.create(postForm.getSubject(), postForm.getContent(), user, postForm.getCategory());
        return "redirect:/question/list";
    }


}

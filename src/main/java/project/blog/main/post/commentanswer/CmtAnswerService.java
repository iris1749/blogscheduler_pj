package project.blog.main.post.commentanswer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.blog.main.DataNotFoundException;
import project.blog.main.post.comment.Comment;
import project.blog.main.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CmtAnswerService {

    private final CmtAnswerRepository cmtAnswerRepository;

    public CmtAnswer create(Comment comment, String content, User author) {
        CmtAnswer cmtAnswer = new CmtAnswer();
        cmtAnswer.setContent(content);
        cmtAnswer.setCreateDate(LocalDateTime.now());
        cmtAnswer.setComment(comment);
        cmtAnswer.setAuthor(author);
        this.cmtAnswerRepository.save(cmtAnswer);
        return cmtAnswer;
    }

    public List<CmtAnswer> getCommentsByComment(Comment comment) {
        return cmtAnswerRepository.findByComment(comment);
    }

    public CmtAnswer getCmtAnswer(Integer id) {
        Optional<CmtAnswer> cmtAnswer = this.cmtAnswerRepository.findById(id);
        if (cmtAnswer.isPresent()) {
            return cmtAnswer.get();
        } else {
            throw new DataNotFoundException("comment not found");
        }
    }

    public void modify(CmtAnswer cmtAnswer, String content) {
        cmtAnswer.setContent(content);
        cmtAnswer.setModifyDate(LocalDateTime.now());
        this.cmtAnswerRepository.save(cmtAnswer);
    }

    public void delete(CmtAnswer cmtAnswer) {
        this.cmtAnswerRepository.delete(cmtAnswer);
    }

    public void vote(CmtAnswer cmtAnswer, User user) {
        cmtAnswer.getLike().add(user);
        this.cmtAnswerRepository.save(cmtAnswer);
    }
}

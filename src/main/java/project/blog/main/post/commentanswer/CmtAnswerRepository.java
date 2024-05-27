package project.blog.main.post.commentanswer;

import org.springframework.data.jpa.repository.JpaRepository;
import project.blog.main.post.comment.Comment;

import java.util.List;

public interface CmtAnswerRepository extends JpaRepository<CmtAnswer, Integer> {
    List<CmtAnswer> findByComment(Comment comment);
}

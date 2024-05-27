package project.blog.main.post.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.blog.main.post.post.Post;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findByPost(Post post, Pageable pageable);
}

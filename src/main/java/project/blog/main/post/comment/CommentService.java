package project.blog.main.post.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.blog.main.DataNotFoundException;
import project.blog.main.post.post.Post;
import project.blog.main.user.User;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    
    private final CommentRepository commentRepository;

    public Comment create(Post post, String content, User author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setPost(post);
        comment.setAuthor(author);
        this.commentRepository.save(comment);
        return comment;
    }

    //댓글 정렬 알고리즘
    public Page<Comment> getListByPost(Post post, int page, String sortOption) {
        Pageable pageable;
        if (sortOption.equals("latest")) {
            pageable = PageRequest.of(page, 5, Sort.by("createDate").descending());
        } else if (sortOption.equals("earliest")) {
            pageable = PageRequest.of(page, 5, Sort.by("createDate").ascending());
        } else if (sortOption.equals("recommended")) {
            pageable = PageRequest.of(page, 5, Sort.by("voter").descending());
        } else {
            pageable = PageRequest.of(page, 5, Sort.by("createDate").descending());
        }
        return commentRepository.findByPost(post, pageable);
    }

    //댓글 불러오기
    public Comment getComment(Integer id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new DataNotFoundException("comment not found");
        }
    }

    //댓글 수정
    public void modify(Comment comment, String content) {
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        this.commentRepository.save(comment);
    }

    //댓글 삭제
    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }

    //댓글 좋아요(추천)
    public  void like(Comment comment, User user){
        comment.getLike().add(user);
        this.commentRepository.save(comment);
    }

}

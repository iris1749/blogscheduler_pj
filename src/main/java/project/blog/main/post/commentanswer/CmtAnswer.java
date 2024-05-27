package project.blog.main.post.commentanswer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.blog.main.post.comment.Comment;
import project.blog.main.user.User;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class CmtAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    private Comment comment;

    @ManyToOne
    private User author;

    private LocalDateTime modifyDate;

    @ManyToMany
    private Set<User> like;
}

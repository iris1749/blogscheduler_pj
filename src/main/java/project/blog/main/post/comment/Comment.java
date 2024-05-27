package project.blog.main.post.comment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.blog.main.post.commentanswer.CmtAnswer;
import project.blog.main.post.post.Post;
import project.blog.main.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User author;

    private LocalDateTime modifyDate;

    @ManyToMany
    private Set<User> like;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CmtAnswer> commentAnswerList; // 대답에 속한 답글 컬렉션 필드
}

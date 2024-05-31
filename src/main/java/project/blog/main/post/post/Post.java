package project.blog.main.post.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.blog.main.user.User;
import project.blog.main.post.comment.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String introcontent ;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @ManyToOne
    private User author;

    private LocalDateTime modifyDate;

    //좋아요 추가
    @ManyToMany
    private Set<User> like;

    //태그
    private String category;

    //조회수 추가
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

}
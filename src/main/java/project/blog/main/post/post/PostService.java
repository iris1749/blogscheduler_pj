package project.blog.main.post.post;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import project.blog.main.DataNotFoundException;
import project.blog.main.user.User;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    //검색기능
    // 검색기능 (제목과 작성자 검색만)
    public Specification<Post> search(String kw) {
        return (root, query, criteriaBuilder) -> {
            if (kw.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            // Join author for username search
            Join<Post, User> authorJoin = root.join("author", JoinType.LEFT);

            // 검색 조건을 제목과 작성자 이름으로 제한
            Predicate titlePredicate = criteriaBuilder.like(root.get("subject"), "%" + kw + "%");
            Predicate authorPredicate = criteriaBuilder.like(authorJoin.get("username"), "%" + kw + "%");

            // OR 조건을 사용해 두 가지 검색 조건을 결합
            return criteriaBuilder.or(titlePredicate, authorPredicate);
        };
    }

    public Page<Post> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
        Specification<Post> specification = search(kw);

        Page<Post> result = postRepository.findAll(specification, pageable); // 올바른 메서드 사용

        return result;
    }

    public Post getPost(Integer id) {
        Optional<Post> post = this.postRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        } else {
            throw new DataNotFoundException("post not found");
        }
    }

    public void create(String subject, String content, String introcontent, User user, String category) {
        Post post = new Post();
        post.setSubject(subject);
        post.setContent(content);
        post.setIntrocontent(introcontent);
        post.setCreateDate(LocalDateTime.now());
        post.setAuthor(user);
        post.setCategory(category);
        this.postRepository.save(post);
    }

    public void modify(Post post, String subject, String content, String category) {
        post.setSubject(subject);
        post.setContent(content);
        post.setModifyDate(LocalDateTime.now());
        post.setCategory(category);

        this.postRepository.save(post);
    }

    public void delete(Post post) {
        this.postRepository.delete(post);
    }

    public void like(Post post, User user){
        post.getLike().add(user);
        this.postRepository.save(post);
    }

    // 조회수 부분 추가
    @Transactional
    public int updateView(Integer id) {
        return this.postRepository.updateView(id);
    }


}

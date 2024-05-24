package project.blog.main.post.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAll(Pageable pageable);

    Page<Post> findAll(Specification<Post> spec, Pageable pageable); // 정적 메서드가 아닌 일반 메서드로 변경

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN p.author u " +
            "WHERE p.subject LIKE %:kw% " +
            "OR u.username LIKE %:kw%")
    Page<Post> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

    @Modifying
    @Query("update Post p set p.view = p.view + 1 where p.id = :id")
    int updateView(@Param("id") Integer id);
}

package project.blog.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MysocialUser {
    private String sub;
    private String pass;
    private String name;
    private String email;


}

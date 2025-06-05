package kr.ac.hansung.cse.hellospringdatajpa.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserInfo {

    private Integer id;

    private String email;

    private String password;

    private List<Role> roles;

    private Boolean isAdmin;
}

package com.Eban.server.pojo;

import com.Eban.server.config.CustomAuthorityDeserializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_admin")
@ApiModel(value = "Admin对象", description = "")

//登录时用到UserDetails里的loadUserByUsername方法，因此要实现接口，或者新建一个类实现接口也可以，返回的是UserDetails
public class Admin implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "住宅电话")
    private String telephone;

    @ApiModelProperty(value = "联系地址")
    private String address;


    /**
     * 这里的是否启用的getter属性，和继承的UserDetails接口中的isEnabled会重复，
     * 因此会报重载错误，所以必须要舍去这里的getter方法
     */
    @Getter(AccessLevel.NONE)
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户头像")
    private String userFace;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "用户角色")
    @TableField(exist = false)
    private List<Role> roles;


    @Override
    /**
     * 当我们更新个人信息同时附带个人权限，会发现报错。
     * //原因：这是我们的 Admin 实体类实现了 UserDetails 接口，重写了 getAuthorities() 方法，但是Admin 实体类却没有对应的 Collection<? extends GrantedAuthority> 属性，也无法创建含有Collection<? extends GrantedAuthority> 属性的构造函数。JSON无法进行反序列化，导致报错
     * //解决：自定义反序列化类，在 getAuthorities() 方法定义使用自定义的反序列化类进行
     */
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
//    注意必须要实现GrantedAuthority类，这样才能被CustomUrlDecisionManager获取，角色信息
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        下面就是将角色的名字拿出来转成Simplexxxx类型返回，这样就可以在CustomUrlDecisionManager中获取用户的角色了
        List<SimpleGrantedAuthority> authorities = roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role
                        .getName()))
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

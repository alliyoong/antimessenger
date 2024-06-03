package com.khanh.antimessenger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

import static com.khanh.antimessenger.constant.ExceptionConstant.*;

@Entity
@Table(name = "MESSACCOUNTS")
@Data
@EqualsAndHashCode(exclude = "accountVerificationList")
@ToString(exclude = "accountVerificationList")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MessAccount{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long accountId;

    @NotEmpty(message = EMPTY_FIRSTNAME_ERROR_MSG)
    @Column(name = "FIRST_NAME")
    private String firstName;

    @NotEmpty(message = EMPTY_LASTNAME_ERROR_MSG)
    @Column(name = "LAST_NAME")
    private String lastName;

    @NotEmpty(message = EMPTY_USERNAME_ERROR_MSG)
    @Column(name = "USERNAME")
    private String username;

    @NotEmpty(message = EMPTY_EMAIL_ERROR_MSG)
    @Email(message = INVALID_EMAIL_ERROR_MSG)
    private String email;

    @NotEmpty(message = EMPTY_PASSWORD_ERROR_MSG)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String address;
    private String phone;
    private String bio;
    private LocalDateTime lastLoginDateDisplay;
    private LocalDateTime lastLoginDate;

    @Column(name = "ENABLED")
//    @Generated(event = EventType.INSERT)
    private Integer isEnabled = 0;

    @Column(name = "NON_LOCKED")
    private Integer isNonLocked = 0;

    @Column(name = "USING_MFA")
    private Integer isUsingMfa = 0;

    @Column(name = "CREATED_AT", insertable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "IMAGE_URL", insertable = false)
    private String imageUrl;

//    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    @JoinTable(name = "account_role",
//    joinColumns = @JoinColumn(name = "account_id"),
//    inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Set<Role> roleSet;
    @NotNull(message = EMPTY_ROLE_ERROR_MSG)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", columnDefinition = "NUMBER DEFAULT ON NULL 1")
    private Role role;

//    public void addRole(Role role) {
//        if(this.roleSet == null)
//            this.roleSet = new HashSet<>();
//        this.roleSet.add(role);
//    }

//    public void removeRole(Role role) {
//        if (this.roleSet != null && this.roleSet.size() >= 2) {
//            this.roleSet.remove(role);
//        } else {
//            throw new RuntimeException("Cannot remove role of this user");
//        }
//    }
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "messAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<AccountVerification> accountVerificationList;

//    @JsonIgnore
//    @OneToOne(fetch = FetchType.LAZY, mappedBy = "messAccount", cascade = CascadeType.ALL, orphanRemoval = true)
//    public RefreshToken refreshToken;
}

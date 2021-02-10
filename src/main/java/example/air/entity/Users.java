package example.air.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
    public class Users implements UserDetails {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id")
        private long id;

        @Column(name = "username")
        @NotBlank(message = "Please fill the username")
        private String username;

        @Column(name = "password")
        @NotBlank(message = "Please fill the password")
        private String password;

        @Column(name = "active")
        private boolean active;

        @Email(message = "Email is not correct")
        @Column(name = "email")
        @NotBlank(message = "Please fill the email")
        private String email;

        @Column(name = "activation_code")
        private String activationCode;

    @ElementCollection(targetClass = Roles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles;

    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private  Set<Message> messages;

    @ManyToMany
    @JoinTable(name = "user_subscriptions", joinColumns = {@JoinColumn(name = "channel_id")},inverseJoinColumns = {@JoinColumn(name = "subscriber_id")})
    private Set<Users> subscribers = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_subscriptions", joinColumns = {@JoinColumn(name = "subscriber_id")},inverseJoinColumns = {@JoinColumn(name = "channel_id")})
    private Set<Users> subscriptions = new HashSet<>();


    public boolean isAdmin(){
        return roles.contains(Roles.ADMIN);
    }
    public Users() {
    }

    public String getUsername() {
        return username;
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
        return isActive();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<Users> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<Users> subscribers) {
        this.subscribers = subscribers;
    }

    public Set<Users> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Users> subscriptons) {
        this.subscriptions = subscriptons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return id == users.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", roles=" + roles +
                '}';
    }
}

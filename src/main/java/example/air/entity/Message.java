package example.air.entity;

import example.air.entity.util.MessageHelper;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "text")
    @NotBlank(message = "Please fill the message")
    @Length(max = 2048,message = "Message to long (more than 2048 symbols")
    private String text;

    @Column(name = "tag")
    @Length(max = 255,message = "Message to long (more than 255 symbols")
    private String tag;

    @Column(name = "filename")
    private String filename;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users author;

    @ManyToMany
    @JoinTable(name = "message_like", joinColumns = {@JoinColumn(name = "message_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<Users> likes = new HashSet<>();

    public Message() {
    }

    public Message(String text, String tag, Users author) {
        this.text = text;
        this.tag = tag;
        this.author = author;
    }

    public Users getAuthor() {
        return author;
    }

    public String getAuthorName() {
        return MessageHelper.getAuthorName(author);
    }

    public void setAuthor(Users user) {
        this.author = user;
    }

    public long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Set<Users> getLikes() {
        return likes;
    }

    public void setLikes(Set<Users> likes) {
        this.likes = likes;
    }
}

package example.air.entity.util;


import example.air.entity.Users;

public abstract class MessageHelper {
    public static String getAuthorName(Users author) {
        return author != null ? author.getUsername() : "<none>";
    }
}

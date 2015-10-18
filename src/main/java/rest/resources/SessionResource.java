package rest.resources;

import core.models.User;

/**
 * Created by Raihan on 10/18/2015.
 */
public class SessionResource {
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    private User user;
    private boolean isAuthenticated;
}

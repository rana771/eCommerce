package rest.mvc;

import core.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rest.resources.SessionResource;
import rest.resources.UserResource;

/**
 * Created by Raihan on 15-Oct-15.
 */

@Controller
@RequestMapping("/users")
public class UserController {
    @RequestMapping(value = "/authenticate")
    public ResponseEntity<SessionResource> getLoggedInUser(){
        User loginUser = new User();
        loginUser.setUserName("username");
        loginUser.setPassword("password");
        loginUser.setId(1L);

        SessionResource response = new SessionResource();
        response.setUser(loginUser);
        response.setAuthenticated(false);

        return new ResponseEntity<SessionResource>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserResource> getSingleUser(@PathVariable Long id) {
        return new ResponseEntity<UserResource>(HttpStatus.OK);
    }
}

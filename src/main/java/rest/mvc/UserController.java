package rest.mvc;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rest.resources.UserResource;

/**
 * Created by Raihan on 15-Oct-15.
 */

@Controller
@RequestMapping("/rest/users")
public class UserController {
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserResource> getSingleUser(@PathVariable Long id) {
        return new ResponseEntity<UserResource>(HttpStatus.NO_CONTENT);
    }
}

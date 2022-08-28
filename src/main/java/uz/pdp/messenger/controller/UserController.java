package uz.pdp.messenger.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.messenger.util.UserImpl;

@Controller
@RequestMapping("/messenger")
public class UserController {
    private final UserImpl userImpl;


    public UserController(final UserImpl userImpl) {
        this.userImpl = userImpl;
    }
}

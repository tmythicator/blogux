package de.hska.lkit.blogux.controller;

import de.hska.lkit.blogux.places.Home;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import de.hska.lkit.blogux.model.User;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import de.hska.lkit.blogux.repo.UserRepository;

/**
 * @author atimchenko
 *
 */
@Controller
public class UserController {

	private final UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

  @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
  public String showUserMain(@ModelAttribute User user, @ModelAttribute Home home, @PathVariable String username, Model model, HttpServletRequest req) {
		User currentUser = (User)req.getAttribute("currentUser");
		User inspectedUser = userRepository.getUser(username);
    model.addAttribute("user", inspectedUser != null ? inspectedUser : new User());
    model.addAttribute("home", home != null ? home : new Home());
    home.setIsself(false);
    home.setCurrentUser(currentUser);

    return "main_template";
  }

  @RequestMapping(value = "/user/{username}", method = RequestMethod.GET, params="action=followUnfollow")
  public String followUnfollow(@ModelAttribute User user, @ModelAttribute Home home,
    @PathVariable String username, Model model, HttpServletRequest req) {
		User currentUser = (User)req.getAttribute("currentUser");
		userRepository.followUnfollow(currentUser, user);

    model.addAttribute("user", user != null ? user : new User());
    model.addAttribute("home", home != null ? home : new Home());
    home.setIsself(false);

    return "redirect:/user/"+username;
  }
}

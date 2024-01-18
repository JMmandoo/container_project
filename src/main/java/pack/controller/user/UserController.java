package pack.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import pack.model.user.UserDto;
import org.springframework.web.bind.annotation.ResponseBody;
import pack.service.users.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/firstLogin")
	public String loginGo(HttpSession session) {
		if (session.getAttribute("userSession") != null) {
			return "redirect:/usersessionkeep";
		}
		return "user/userlogin";
	}

	@GetMapping("/userJoinGo")
	public String userJoinGo(HttpSession session) {
		if (session.getAttribute("userSession") != null) {
			return "redirect:/usersessionkeep";
		}
		return "user/userjoin";
	}

	@GetMapping("/ownerlogingo")
	public String ownerLoginGo(HttpSession session) {
		if (session.getAttribute("ownerSession") != null) {
			return "redirect:/ownersessionkeep";
		}
		return "../templates/owner/ownerlogin";
	}

	@GetMapping("/userInfoFind")
	public String userInfoFinding() {
		return "user/useridfind";
	}

	@PostMapping("userJoinClick")
	public String userRegister(UserDto userDto) {
		boolean isRegistered = userService.registerUser(userDto);
		if (isRegistered) {
			return "user/userlogin";
		} else {
			return "user/userjoin";
		}
	}

	@PostMapping("/userLogSuccess")
	public String userLogin(@RequestParam("user_id") String userId,
													@RequestParam("user_pwd") String password,
													HttpSession session) {
		UserDto user = userService.loginUser(userId, password);
		if (user != null) {
			session.setAttribute("userSession", user);
			return "redirect:/usersessionkeep";
		} else {
			return "user/userlogin";
		}
	}

	@GetMapping("/userupdate")
	public String userUpdatePage(Model model, HttpSession session) {
		UserDto user = (UserDto) session.getAttribute("userSession");
		model.addAttribute("userSession", user);
		return "user/userupdate";
	}

	@PostMapping("/userInfoUpdate")
	public String userInfoupdate(UserDto userDto, Model model, HttpSession session) {
		boolean isUpdated = userService.updateUser(userDto);
		if(isUpdated) {
			return "user/userlogin";
		} else {
			return "user/usermypage";
		}
	}

	@GetMapping("/userdelete")
	public String userDeletePage(Model model, HttpSession session) {
		UserDto user = (UserDto) session.getAttribute("userSession");
		model.addAttribute("userSession", user);
		return "user/userdelete";
	}

	@PostMapping("/userInfoDelete")
	public String userInfoDelete(UserDto userDto, HttpSession session) {
		boolean isDeleted = userService.deleteUser(userDto);
		if(isDeleted) {
			session.removeAttribute("userSession");
			return "user/userlogin";
		} else {
			return "user/userdelete";
		}
	}

	@GetMapping("/userlogoutgo")
	public String userLogoutProcess(HttpSession session) {
		session.removeAttribute("userSession");
		return "redirect:/";
	}

	@ResponseBody
	@PostMapping("/userIdCheck")
	public int idCheck(@RequestParam("user_id") String userId) {
		return userService.checkUserId(userId);
	}

	@ResponseBody
	@PostMapping("/userIdInfoFind")
	public String userIdFindProcess(@RequestParam("user_name") String userName,
																	@RequestParam("user_email") String userEmail,
																	@RequestParam("user_jumin") String userJumin) {
		UserDto user = userService.findUserId(userName, userEmail, userJumin);
		if (user != null) {
			return user.getUser_id();
		} else {
			return "not_found";
		}
	}

	@GetMapping("/usersessionkeep")
	public String userSessionKeep(HttpSession session) {
		UserDto userSession = (UserDto) session.getAttribute("userSession");
		if (userSession != null) {
			return "user/usermypage";
		} else {
			return "../templates/index";
		}
	}
}

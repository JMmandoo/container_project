package pack.controller.booking;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pack.model.booking.bookingDTO;
import pack.model.user.UserDto;
import pack.model.admin.AdminBean;
import pack.service.booking.BookingService;

import java.util.ArrayList;

@Controller
@RequestMapping("booking")
public class BookingController {
	@Autowired
	private BookingService bookingService;

	@GetMapping("booking")
	public String booking() {
		return "booking/booking";
	}

	@PostMapping("/bookingDo")
	public String bookingDo(bookingDTO bookingdto, AdminBean bean) {
		if (bookingService.bookingInsertAndUpdate(bookingdto, bean)) {
			return "redirect:/booking/bookingInfo";
		} else {
			return "/booking/booking";
		}
	}

	@GetMapping("/bookingInfo")
	public String bookingProcess(HttpSession session, Model model) {
		UserDto user = (UserDto) session.getAttribute("userSession");
		if (user == null) {
			return "redirect:/userlogin";
		}

		String user_id = user.getUser_id();
		ArrayList<bookingDTO> bookingdto = bookingService.bookingListAll(user_id);
		session.setAttribute("bookList", bookingdto);
		model.addAttribute("bList", bookingdto);
		return "booking/bookingInfo";
	}

	@GetMapping("bookDelete")
	public String bookDelete(bookingDTO bookingDto, Model model, HttpSession session){
		if (bookingService.bookingDelete(bookingDto)) {
			return "booking/booking";
		} else {
			return "redirect:bookingInfo";
		}
	}
}

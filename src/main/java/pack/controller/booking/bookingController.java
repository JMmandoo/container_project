package pack.controller.booking;



import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import pack.model.booking.bookingDTO;
import pack.model.user.UserDto;
import pack.controller.FormBean;
import pack.model.admin.AdminBean;
import pack.model.booking.BookingDao;
import pack.model.booking.BookingMapperInter;
import pack.service.booking.BookingService;


@Controller
@RequestMapping("booking")
public class bookingController {
	@Autowired
	private BookingService bookingService;

	@GetMapping("booking")
	public String booking() {
		return "booking/booking";
	}

	@PostMapping("/bookingDo")
	public String bookingDo(bookingDTO bookingDto, AdminBean bean) {
		if (bookingService.bookingDo(bookingDto, bean)) {
			return "redirect:/booking/bookingInfo";
		} else {
			return "/booking/booking";
		}
	}

	@GetMapping("/bookingInfo")
	public String bookingProcess(HttpSession session, Model model) {
		String userId = (String) session.getAttribute("user_id");
		ArrayList<bookingDTO> bookingList = bookingService.bookingListAll(userId);
		model.addAttribute("bList", bookingList);
		return "booking/bookingInfo";
	}

	@GetMapping("bookDelete")
	public String bookDelete(@RequestParam("bookingId") int bookingId, Model model, HttpSession session) {
		bookingDTO bookingDto = new bookingDTO(); // bookingDTO에 필요한 정보 설정
		if (bookingService.bookDelete(bookingDto)) {
			return "booking/booking";
		} else {
			return "redirect:bookingInfo";
		}
	}
}
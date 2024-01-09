package pack.service.booking;

import org.springframework.ui.Model;

import pack.model.admin.AdminBean;
import pack.model.booking.bookingDTO;

import java.util.ArrayList;

public interface BookingService {
	boolean bookingDo(bookingDTO bookingDto, AdminBean adminBean);
	ArrayList<bookingDTO> bookingListAll(String userId);
	boolean bookDelete(bookingDTO bookingDto);
}

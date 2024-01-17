package pack.service.booking;

import pack.model.booking.bookingDTO;
import pack.model.admin.AdminBean;
import java.util.ArrayList;

public interface BookingService {
  boolean bookingInsertAndUpdate(bookingDTO bookingdto, AdminBean bean);
  ArrayList<bookingDTO> bookingListAll(String userId);
  boolean bookingDelete(bookingDTO bookingDto);
}

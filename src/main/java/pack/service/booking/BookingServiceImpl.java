package pack.controller.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pack.model.booking.BookingDao;
import pack.model.booking.bookingDTO;
import pack.model.admin.AdminBean;
import pack.service.booking.BookingService;

import java.util.ArrayList;

@Service
public class BookingServiceImpl implements BookingService {

  @Autowired
  private BookingDao bookingDao;

  @Override
  public boolean bookingDo(bookingDTO bookingDto, AdminBean adminBean) {
    boolean isBookingInserted = bookingDao.bookingInsert(bookingDto);
    boolean isStatusUpdated = bookingDao.contStatusUpdate(adminBean);
    return isBookingInserted && isStatusUpdated;
  }

  @Override
  public ArrayList<bookingDTO> bookingListAll(String userId) {
    return bookingDao.bookingListAll(userId);
  }

  @Override
  public boolean bookDelete(bookingDTO bookingDto) {
    return bookingDao.bookingDelete(bookingDto);
  }
}

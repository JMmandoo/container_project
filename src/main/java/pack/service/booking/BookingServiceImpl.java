package pack.service.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pack.model.booking.BookingDao;
import pack.model.booking.bookingDTO;
import pack.model.admin.AdminBean;

import java.util.ArrayList;

@Service
public class BookingServiceImpl implements BookingService {

  @Autowired
  private BookingDao dao;

  @Override
  public boolean bookingInsertAndUpdate(bookingDTO bookingdto, AdminBean bean) {
    boolean insertResult = dao.bookingInsert(bookingdto);
    boolean updateResult = dao.contStatusUpdate(bean);
    return insertResult && updateResult;
  }

  @Override
  public ArrayList<bookingDTO> bookingListAll(String userId) {
    return dao.bookingListAll(userId);
  }

  @Override
  public boolean bookingDelete(bookingDTO bookingDto) {
    return dao.bookingDelete(bookingDto);
  }
}

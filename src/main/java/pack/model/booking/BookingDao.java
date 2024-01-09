
package pack.model.booking;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pack.model.admin.AdminBean;


@Repository
public class BookingDao {
	@Autowired
	private BookingMapperInter bookingMapperInter;

	@Transactional
	public boolean bookingInsert(bookingDTO bookingdto) {
		boolean insertSuccess = false;
		try {
			int insertCount = bookingMapperInter.bookingInsert(bookingdto);
			if (insertCount > 0) {
				insertSuccess = true;
			}

		} catch (Exception e) {
			System.out.println("예외 발생: " + e.getMessage());
			// 로그 기록 또는 추가 예외 처리
		}
		return insertSuccess;
	}

	@Transactional // 성공하면 커밋, 실패하면 롤백
	public boolean contStatusUpdate(AdminBean adminbean) {
		boolean updateSuccess = false;
		System.out.println("상태 업데이트 메서드 시작");
		try {
			int updateCount = bookingMapperInter.contStatusUpdate(adminbean);
			System.out.println("SQL 실행 결과: " + updateCount);
			if (updateCount > 0)
				updateSuccess = true;
		} catch (Exception e) {
			System.out.println("예외 발생: " + e.getMessage());
			// 로그 기록 또는 추가 예외 처리
			updateSuccess = false;
		}
		return updateSuccess;
	}

	public ArrayList<bookingDTO> bookingListAll(String userId){
		ArrayList<bookingDTO> bookingList = (ArrayList<bookingDTO>)bookingMapperInter.bookingList(userId);
		System.out.println("user_id : " + userId);
		return bookingList;
	}

	// 예약 삭제 bookingInfo 취소하기
	public boolean bookingDelete(bookingDTO bookingdto) {
		boolean deleteSuccess = false;
		int deleteCount = bookingMapperInter.bookingDelete(bookingdto);
		if(deleteCount >= 0 ) deleteSuccess = true;
		return deleteSuccess;
	}
}


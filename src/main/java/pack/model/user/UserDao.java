package pack.model.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.servlet.http.HttpSession;
import pack.model.DataMapperInter;


@Repository
public class UserDao {

  @Autowired
  private DataMapperInter dataMapperInter;

  private boolean isEmpty(String value) {
    return value == null || value.trim().isEmpty();
  }

  // 사용자의 회원가입 데이터 유효성을 검사하고, 필드 값이 비어 있거나 정규식 패턴에 맞지 않는 경우 회원가입을 방지
  private boolean joinUserData(UserDto userDto) {
    boolean isValidUser = false;
    // 각 필드의 유효성 검사를 수행
    if (isEmpty(userDto.getUser_id()) ||
        isEmpty(userDto.getUser_pwd()) ||
        isEmpty(userDto.getUser_name()) ||
        isEmpty(userDto.getUser_tel()) ||
        isEmpty(userDto.getUser_email()) ||
        isEmpty(userDto.getUser_addr()) ||
        // 필드 중 하나라도 비어 있거나 정규식에 유효하지 않으면 false를 반환
        !userDto.getUser_id().matches("^[a-zA-Z\\d]{4,}$") ||
        !userDto.getUser_tel().matches("^[0-9-]+$") ||
        !userDto.getUser_jumin().matches("^\\d{6}-\\d{7}$") ||
        !userDto.getUser_pwd().equals(userDto.getUser_repwd()) || // 비밀번호 필드값과 비밀번호 확인 필드값의 데이터가 값은지 확인한다
        !userDto.getUser_name().matches("^[가-힣]{2,}$") ||
        !userDto.getUser_pwd().matches("^.{4,}$") ||
        !userDto.getUser_email().matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")) {
      // 비밀번호가 일치하지 않거나 이름, 비밀번호, 이메일이 조건을 만족하지 않으면 유효하지 않음.
      isValidUser = false;
    } else {
      // 모든 필드와 정규식에 유효하고 비밀번호가 일치하면 true로 설정.
      isValidUser = true;
    }
    // 최종 반환값
    return isValidUser;
  }

  public boolean userInsertData(UserDto userDto) {
    boolean isInsertSuccessful = false;
    try {
      if (joinUserData(userDto)) {
        int re = dataMapperInter.userInsertData(userDto);
        if (re > 0) {
          isInsertSuccessful = true;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return isInsertSuccessful;
  }


  // 사용자 로그인 가능 여부 판단하는 메서드
  public UserDto userLoginProcess(String user_id, String user_pwd) {
    // 반환되는 값은 클라이언트가 입력한 아이디와 비밀번호 값이다.
    return dataMapperInter.userLoginProcess(user_id, user_pwd);
  }

  // 사용자 회원수정에 필요한 메서드
  public boolean userDataUpdate(UserDto userDto) {
    // boolean 기본 타입이 false지만 가독성을 위해 추가
    boolean isUpdateSuccessful = false;
    int re = dataMapperInter.userUpdate(userDto);
    if (re > 0) isUpdateSuccessful = true;
    return isUpdateSuccessful;
  }

  // 회원삭제
  public boolean userDataDelete(UserDto userDto) {
    boolean isDeleteSuccessful = false;
    int re = dataMapperInter.userDelete(userDto);
    if (re > 0) {
      isDeleteSuccessful = true;
    }
    return isDeleteSuccessful;
  }

  // 사용자 회원가입시 중복체크
  public int userIdCheck(String user_id) {
    int result = dataMapperInter.userIdCheck(user_id);
    return result; // 중복되는 경우 1을 반환하고, 중복되지 않으면 0을 반환
  }

  // 사용자 아이디 찾기
  public UserDto userIdFind(String user_name, String user_email, String user_jumin) {
    return dataMapperInter.userIdFind(user_name, user_email, user_jumin);
  }
}

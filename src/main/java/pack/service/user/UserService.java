package pack.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pack.model.user.UserDao;
import pack.model.user.UserDto;

@Service
public class UserService {

  @Autowired
  private UserDao userDao;

  public boolean registerUser(UserDto userDto) {
    return userDao.userInsertData(userDto);
  }

  public UserDto loginUser(String userId, String password) {
    return userDao.userLoginProcess(userId, password);
  }

  public boolean updateUser(UserDto userDto) {
    return userDao.userDataUpdate(userDto);
  }

  public boolean deleteUser(UserDto userDto) {
    return userDao.userDataDelete(userDto);
  }

  public int checkUserId(String userId) {
    return userDao.userIdCheck(userId);
  }

  public UserDto findUserId(String userName, String userEmail, String userJumin) {
    return userDao.userIdFind(userName, userEmail, userJumin);
  }
}


package pack.service.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pack.model.user.UserDao;
import pack.model.user.UserDto;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserDao userDao;

  @Override
  public boolean registerUser(UserDto userDto) {
    return userDao.userInsertData(userDto);
  }

  @Override
  public UserDto loginUser(String userId, String password) {
    return userDao.userLoginProcess(userId, password);
  }

  @Override
  public boolean updateUser(UserDto userDto) {
    return userDao.userDataUpdate(userDto);
  }

  @Override
  public boolean deleteUser(UserDto userDto) {
    return userDao.userDataDelete(userDto);
  }

  @Override
  public int checkUserId(String userId) {
    return userDao.userIdCheck(userId);
  }

  @Override
  public UserDto findUserId(String userName, String userEmail, String userJumin) {
    return userDao.userIdFind(userName, userEmail, userJumin);
  }
}

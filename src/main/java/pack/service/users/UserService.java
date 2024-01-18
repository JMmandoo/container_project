package pack.service.users;

import pack.model.user.UserDto;

public interface UserService {
  boolean registerUser(UserDto userDto);
  UserDto loginUser(String userId, String password);
  boolean updateUser(UserDto userDto);
  boolean deleteUser(UserDto userDto);
  int checkUserId(String userId);
  UserDto findUserId(String userName, String userEmail, String userJumin);
}

package pack.model.user;

import lombok.Data;

@Data
public class UserDto {
	private String user_id, user_pwd, user_name, user_tel, user_email, user_addr, user_jumin, user_repwd;
}

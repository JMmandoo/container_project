package pack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.http.HttpSession;
import pack.model.user.UserDto;
import org.springframework.ui.Model;

@Controller
public class MapKeyController {
  @Value("${kakao.api.key}")
  private String kakaoApiKey;
    @GetMapping("/manageMapSession")
    public String index(Model model, HttpSession session) {
       UserDto userSession = (UserDto) session.getAttribute("userSession");
       // 만약 세션에 사용자 정보가 있는 경우
       if (userSession != null) {
         model.addAttribute("kakaoApiKey", kakaoApiKey);
           return "kakaomap";
       }                              
       else {
           return "../templates/user/userlogin";
       }
    }
}

package pack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.http.HttpSession;
import pack.model.user.UserDto;
import org.springframework.ui.Model;
//templates 폴더 내의 파일은 URL에서 직접 접근할 수 없으므로 컨트롤러를 통해 접근, conajax.html을 호출
@Controller
public class ConajaxController {
  @Value("${kakao-api-key}")
  private String kakaoApiKey;
    @GetMapping("/conajax")
    public String index(Model model, HttpSession session) {
       UserDto userSession = (UserDto) session.getAttribute("userSession");
       // 만약 세션에 사용자 정보가 있는 경우
       if (userSession != null) {
           // 사용자 정보를 유지하면 로그인페이지에서 home 버튼을 눌러도 로그인 세션이 있는채로 마이페이지로 이동.
         model.addAttribute("kakaoApiKey", kakaoApiKey);
           return "conajax";
       }                              
       else {
           // 세션에 사용자 정보가 없는 경우에는 index로 이동.
           return "../templates/user/userlogin";
       }
    }
}

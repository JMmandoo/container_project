package pack.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import pack.model.admin.AdminDao;
import pack.model.admin.AdminDto;

@Controller
public class AdminController {

	@Autowired
	private AdminDao adminDao;
	
	// 메인 페이지에서 노란점 클릭했을 때 (성공)
	@GetMapping("adminLoginGo") 
	public String adminlogingo() {
		return "../templates/admin/adminlogin";
	}
	
	
	// 로그인 요청 처리 (성공)
    @PostMapping("adminloginok")
    public String processLoginForm(@RequestParam("admin_id") String admin_id,
            					   @RequestParam("admin_pwd") String admin_pwd,
                                   Model model, HttpSession session){
        // 사용자 로그인 처리
    	AdminDto admin = adminDao.adminloginProcess(admin_id, admin_pwd);

        if (admin != null) {
        	session.setAttribute("adminSession", admin); // 세션에 사용자 정보 저장
        	return "admin/adminloginok";  // 로그인 성공 시 adminloginok.html로 이동
            
        } else {
            // 로그인 실패
            return "admin/adminlogin"; 
        }
    }

	@GetMapping("/adminlogout")
	public String userLogoutProcess(HttpSession session) {
	    session.removeAttribute("adminSession"); // 세션 유지 종료
	    return "redirect:/"; // 로그아웃 클릭시 메인 홈페이지로 이동 
	}

	// 세션값 유지하게 하기
	@GetMapping("/adminsessionkeep")
	public String adminSessionKeep(HttpSession session) {
		AdminDto adminSession = (AdminDto) session.getAttribute("adminSession");
	    if (adminSession != null) {
	        return "admin/adminloginok"; 
	    } else {
	    	return "../templates/index";
	    }
	}
	

	
}

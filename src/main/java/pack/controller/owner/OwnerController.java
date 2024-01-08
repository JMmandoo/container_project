package pack.controller.owner;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import pack.model.owner.OwnerDto;
import pack.service.service.OwnerService;

@Controller
public class OwnerController {

  @Autowired
  private OwnerService ownerService;

  @GetMapping("ownerJoinGo")
  public String ownerChoice(HttpSession session) {
    if (session.getAttribute("ownerSession") != null) {
      return "redirect:/ownersessionkeep";
    }
    return "owner/ownerjoin";
  }

  @GetMapping("userlogingo")
  public String userLoginGo(HttpSession session) {
    if (session.getAttribute("userSession") != null) {
      return "redirect:/usersessionkeep";
    }
    return "user/userlogin";
  }

  @PostMapping("ownerJoinClick")
  public String ownerRegister(OwnerDto ownerDto) {
    boolean isRegistered = ownerService.registerOwner(ownerDto);
    if (isRegistered) {
      return "../templates/owner/ownerlogin";
    } else {
      return "../templates/owner/ownerjoin";
    }
  }

  @PostMapping("ownerLogSuccess")
  public String ownerLogin(@RequestParam("business_num") String businessNum,
                           @RequestParam("owner_pwd") String ownerPwd,
                           HttpSession session) {
    OwnerDto owner = ownerService.loginOwner(businessNum, ownerPwd);
    if (owner != null) {
      session.setMaxInactiveInterval(1800);
      session.setAttribute("ownerSession", owner);
      return "owner/ownermain";
    } else {
      return "owner/ownerlogin";
    }
  }

  @GetMapping("/ownerupdate")
  public String ownerUpdatePage(Model model, HttpSession session) {
    OwnerDto owner = (OwnerDto) session.getAttribute("ownerSession");
    model.addAttribute("ownerSession", owner);
    return "owner/ownerupdate";
  }

  @PostMapping("ownerInfoUpdate")
  public String ownerUpdate(OwnerDto ownerDto, HttpSession session) {
    boolean isUpdated = ownerService.updateOwner(ownerDto);
    if (isUpdated) {
      return "owner/ownerlogin";
    } else {
      return "owner/ownerupdate";
    }
  }

  @GetMapping("/ownerdelete")
  public String ownerDeletePage(Model model, HttpSession session) {
    // 세션에서 회원 정보를 가져와서 모델에 추가
    OwnerDto owner = (OwnerDto) session.getAttribute("ownerSession");
    model.addAttribute("ownerSession", owner);
    return "owner/ownerdelete"; // 회원 수정 페이지로 이동
  }

  @GetMapping("/ownersessionkeep")
  public String ownerSessionKeep(HttpSession session) {
    OwnerDto ownerSession = (OwnerDto) session.getAttribute("ownerSession");
    if (ownerSession != null) {
      // 세션에 ownerSession값이 존재할 경우 ownermain.html 페이지로 이동
      return "owner/ownermain";
    } else {
      // 세션값이 없을 경우
      return "../templates/index";
    }
  }

  // 공급자 회원탈퇴 페이지에서 회원탈퇴 버튼을 클릭할 때
  @PostMapping("/ownerInfoDelete")
  public String ownerInfoDelete(OwnerDto ownerDto, HttpSession session) {
    try {
      boolean isDeleted = ownerService.deleteOwner(ownerDto);
      if (isDeleted) {
        session.invalidate(); // 세션 정보를 제거
        return "redirect:/ownerlogin"; // 로그인 페이지로 리디렉션
      } else {
        return "redirect:/ownerdelete"; // 삭제 실패 시 다시 탈퇴 페이지로
      }
    } catch (Exception e) {
      // 예외 발생 시 적절한 에러 페이지나 로그 처리
      return "error";
    }
  }
}

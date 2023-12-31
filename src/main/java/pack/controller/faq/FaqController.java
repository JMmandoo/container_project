package pack.controller.faq;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pack.model.faq.FaqDao;
import pack.model.faq.FaqDto;

@Controller
public class FaqController {

   @Autowired 
   public FaqDao faqDao;
   
   private int tot;        // 전체 FAQ 레코드 수를 저장하는 멤버 변수, 이 변수는 페이지 수 계산을 위해 사용
   private int plist = 10;  //  페이지당 행 수를 나타내는 멤버 변수로, 한 페이지에 몇 개의 FAQ 항목을 표시할지를 결정
   
   public ArrayList<FaqDto> getListData(ArrayList<FaqDto> list, int page){
      // 페이지 번호(page)와 FAQ 목록(list)을 받아와서 해당 페이지에 표시할 FAQ 항목을 추출하여 반환하는 메서드, 페이징 처리를 위해 사용
      ArrayList<FaqDto> result = new ArrayList<FaqDto>();
      
      int start = (page - 1) * plist; // 현재 페이지에서 표시할 FAQ 항목의 시작 인덱스를 계산
      int end = Math.min(start + plist, list.size());  // 페이지에 표시할 FAQ 항목의 끝 인덱스를 계산하며, 리스트 크기를 초과하지 않도록 조정
      
      for (int i = start; i < end; i++) {
         result.add(list.get(i));
      }
      return result; // 페이지에 표시할 FAQ 항목을 담을 ArrayList를 초기화하고, 해당 페이지의 FAQ 항목을 복사하여 반환
   }
   
   public int getPageSu() { // 총 페이지 수 얻기
      tot = faqDao.totalFaq();
      int pagesu = tot / plist;
      if(tot % plist > 0) pagesu += 1;
      // FAQ 데이터베이스에 있는 전체 FAQ 레코드 수를 조회하고, 페이지당 행 수(plist)로 나눈 후 나머지가 있으면 페이지 수를 1 증가시켜 반환
      return pagesu; 
   }
   
   public int getSearchPageSu(FaqBean bean) { // 총 검색 페이지 수 얻기
      ArrayList<FaqDto> searchtot = (ArrayList<FaqDto>)faqDao.searchFaq(bean);
      tot = searchtot.size();
      int pagesu = tot / plist;
      if(tot % plist > 0) pagesu += 1;
      // FAQ 검색 조건(bean)을 받아와서 해당 검색 결과의 레코드 수를 조회하고, 페이지당 행 수(plist)로 나눈 후 나머지가 있으면 페이지 수를 1 증가시켜 반환
      return pagesu;
   }

   @GetMapping("searchfaquser")
   public String searchUser(@RequestParam(value = "searchpage", defaultValue = "1" ) int searchpage, 
                     // 요청 파라미터 중 searchpage 값을 읽어와서 searchpage 변수에 저장, defaultValue 속성을 사용하여 파라미터가 전달되지 않을 경우 기본값으로 1을 설정
                        @RequestParam(name = "searchName", required = false) String searchName,
                        @RequestParam(name = "searchValue", required = false) String searchValue,
                        // searchName 및 searchValue라는 두 개의 요청 파라미터를 읽고, required = false로 설정되어 이 파라미터들이 요청에 없을 경우에도 오류가 발생하지 않음.
                        Model model) { //  Model 객체를 메서드 파라미터로 받아서 사용, Model 객체를 사용하여 뷰에 데이터를 전달
      int spage = searchpage; // 검색 페이지 번호를 저장할 spage 변수를 선언하고, 요청에서 받은 searchpage 값을 할당
       if (searchpage <= 0) spage = 1; // 페이지 번호가 0 이하인 경우 1로 설정
       FaqBean bean = new FaqBean(); //  FaqBean 객체를 생성 및 검색 조건 설정
       bean.setSearchName(searchName);
       bean.setSearchValue(searchValue);
       // 사용자가 입력한 검색 조건과 검색어를 FaqBean 객체에 설정
       
       ArrayList<FaqDto> slist = (ArrayList<FaqDto>) faqDao.searchFaq(bean); 
       // faqDao를 사용하여 FaqBean을 기반으로 FAQ를 검색하고, 결과를 slist에 저장, FaqDto는 FAQ 데이터를 담는 데이터 전송 객체
       ArrayList<FaqDto> sresult = getListData(slist, spage); 
       // 검색된 FAQ 목록(slist)과 현재 페이지 번호(spage)를 사용하여 페이지에 표시할 FAQ 목록을 계산하여 sresult에 저장, 페이지네이션 기능을 구현하는 함수
       
       model.addAttribute("searchName", searchName);
       model.addAttribute("searchValue", searchValue);
       model.addAttribute("faq", sresult);
       model.addAttribute("pagesu", getSearchPageSu(bean));
       model.addAttribute("page", spage);
       // 검색 조건, 검색 결과, 페이지 정보 등을 모델에 추가, 이렇게 모델에 추가한 데이터는 뷰 템플릿에서 사용
       
      return "../templates/faq/faqusersearch";
   }
   
   @GetMapping("/searchfaqowner")
   public String searchOwner(@RequestParam(value = "searchpage") int searchpage,
                            @RequestParam(name = "searchName", required = false) String searchName,
                            @RequestParam(name = "searchValue", required = false) String searchValue,
                            Model model) {
       int spage = searchpage;
       if (searchpage <= 0) spage = 1;
       FaqBean bean = new FaqBean(); // FaqBean 생성 및 검색 조건 설정
       bean.setSearchName(searchName);
       bean.setSearchValue(searchValue);
       
       ArrayList<FaqDto> slist = (ArrayList<FaqDto>) faqDao.searchFaq(bean);
       ArrayList<FaqDto> sresult = getListData(slist, spage);
       
       model.addAttribute("searchName", searchName);
       model.addAttribute("searchValue", searchValue);
       model.addAttribute("faq", sresult);
       model.addAttribute("pagesu", getSearchPageSu(bean));
       model.addAttribute("page", spage);
       
       return "../templates/faq/faqownersearch";
   }
   
   @GetMapping("searchfaqadmin")
   public String searchadmin(@RequestParam(value = "searchpage", defaultValue = "1" ) int searchpage,
                        @RequestParam(name = "searchName", required = false) String searchName,
                        @RequestParam(name = "searchValue", required = false) String searchValue,
                        Model model) {
      
      int spage = searchpage;
       if (searchpage <= 0) spage = 1;
       FaqBean bean = new FaqBean(); // FaqBean 생성 및 검색 조건 설정
       bean.setSearchName(searchName);
       bean.setSearchValue(searchValue);
       
       ArrayList<FaqDto> slist = (ArrayList<FaqDto>) faqDao.searchFaq(bean);
       ArrayList<FaqDto> sresult = getListData(slist, spage);
       
       model.addAttribute("searchName", searchName);
       model.addAttribute("searchValue", searchValue);
       model.addAttribute("faqadmin", sresult);
       model.addAttribute("pagesu", getSearchPageSu(bean));
       model.addAttribute("page", spage);
      return "../templates/faq/faqadminsearch";
   }
   
   @GetMapping("faq")
   public String listProcess(@RequestParam("page")int page, Model model) {
      int spage = page;
      if (page <= 0) spage = 1;
      
      ArrayList<FaqDto> list = (ArrayList<FaqDto>)faqDao.listFaq();
      ArrayList<FaqDto> result = getListData(list, spage);
      
      model.addAttribute("faq", result);
      model.addAttribute("pagesu", getPageSu());
      model.addAttribute("page", spage);
      
      return "../templates/faq/faq";
   }
   
   @GetMapping("faquser")
   public String listProcessUser(@RequestParam("page")int page, Model model) {
      int spage = page;
      if (page <= 0) spage = 1;
      
      ArrayList<FaqDto> list = (ArrayList<FaqDto>)faqDao.listFaq();
      ArrayList<FaqDto> result = getListData(list, spage);
      
      model.addAttribute("faq", result);
      model.addAttribute("pagesu", getPageSu());
      model.addAttribute("page", spage);
      
      return "../templates/faq/faquser";
   }
   
   @GetMapping("faqowner")
   public String listProcessOwner(@RequestParam("page")int page, Model model) {
      int spage = page;
      if (page <= 0) spage = 1;
      
      ArrayList<FaqDto> list = (ArrayList<FaqDto>)faqDao.listFaq();
      ArrayList<FaqDto> result = getListData(list, spage);
      
      model.addAttribute("faq", result);
      model.addAttribute("pagesu", getPageSu());
      model.addAttribute("page", spage);
      
      return "../templates/faq/faqowner";
   }
   
   @GetMapping("faqadmin")
   public String listProcessadmin(@RequestParam("page")int page, Model model) {
      int spage = page;
      if (page <= 0) spage = 1;
      
      ArrayList<FaqDto> list = (ArrayList<FaqDto>)faqDao.listFaq();
      ArrayList<FaqDto> result = getListData(list, spage);
      
      model.addAttribute("faqadmin", result);
      model.addAttribute("pagesu", getPageSu());
      model.addAttribute("page", spage);
      
      return "../templates/faq/faqadmin";
   }
   
   @GetMapping("faqinsert")
   public String faqinsert() {
      return "../templates/faq/faqinsert";
   }
   
   @PostMapping("faqinsert")
   public String faqinsertSubmit(FaqBean bean) {
      boolean b = faqDao.insertFaq(bean);
      
      if(b) {
         return "redirect:/faqadmin?page=1"; // 추가 후 목록 보기
      } else {
         return "error";
      }
   }
   
   @PostMapping("faqupdate")
   public String faqupdate(FaqBean bean) {
      boolean b = faqDao.updateFaq(bean);
      
      if(b) {
         return "redirect:/faqadmin?page=1"; // 수정 후 목록 보기
      } else {
         return "error";
      }
   }
   
   @PostMapping("faqdelete")
   public String faqdelete(@RequestParam("faq_no")String faq_no) {
      boolean b = faqDao.deleteFaq(faq_no);
      if(b) {
         return "redirect:/faqadmin?page=1"; // 삭제 후 목록 보기
      } else {
         return "error";
      }
   }
   
   @GetMapping("faqdetail")
   public String faqdetail(@RequestParam("faq_no")int faq_no, Model model) {
      FaqDto detail = faqDao.detailFaq(faq_no);
      model.addAttribute("detail", detail);
      return "../templates/faq/faqdetail";
   }
}
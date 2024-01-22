package pack.controller.container;

import java.io.File;
import java.io.FileOutputStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import pack.model.booking.bookingDTO;
import pack.model.container.ContainDao;
import pack.model.container.ContainerDto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper; // Jackson 라이브러리 추가

@Controller
@RequestMapping(value = "owner")
public class ContainerController {

// ******* 민혁 ************

  @Autowired
  private ContainDao containDao;

  @GetMapping("/ownerMain")
  public String main() {
    return "container/ownerMain";
  }

  @GetMapping("/paid")
  public String cont_pay() {
    return "container/container_paid";
  }

  @GetMapping("/register")
  public String cont_regs() {
    return "container/container_register";
  }

  @GetMapping("/list")
  public String cont_mgr(Model model, HttpSession session) {
    // 창고관리 페이지로 매핑해주는 메소드, 리스트값을 달고 가서 반복문을 통해 테이블에 값들을 밀어넣어줌
    String business_num = (String) session.getAttribute("business_num");
    // 로그인한 사업자가 등록한 창고정보만 출력될 수 있도록 세션을 통해 사업자번호를 받아옴
    ArrayList<ContainerDto> clist = (ArrayList<ContainerDto>) containDao.getDataAll(business_num);

    model.addAttribute("datas", clist);
    // datas라는 키값에 clist담아 키값을 통해 clist에 담긴 데이터를 불러올수 있게 해줌
    return "container/container_list";
    // 창고목록 페이지로 리턴해줌.
  }

  @GetMapping("/reserve")
  public String cont_reserve(Model model, HttpSession session) {
    // 창고관리 페이지로 매핑해주는 메소드, 리스트값을 달고 가서 반복문을 통해 테이블에 값들을 밀어넣어줌
    String business_num = (String) session.getAttribute("business_num");
    // 로그인한 사업자가 등록한 창고정보만 출력될 수 있도록 세션을 통해 사업자번호를 받아옴
    ArrayList<ContainerDto> rlist = (ArrayList<ContainerDto>) containDao.getDataReserve(business_num);
    model.addAttribute("datas", rlist);
    return "container/container_reserve";
  }

  // ---------------------------
  private double[] getCoordinatesFromAddress(String address) {
    double[] coordinates = new double[2]; // 배열 초기화

    try {
      // Google Geocoding API를 호출하여 주소를 위도와 경도로 변환
      String apiKey = "AIzaSyDzGKmDfbyNTWo-0WqNSdQlQSlxc6Wjna4";
      String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + apiKey;

      URL url = new URL(apiUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();

      // JSON 응답에서 위도와 경도 추출
      Map<String, Object> data = new ObjectMapper().readValue(response.toString(), Map.class);
      if (data.containsKey("results")) {
        Map<String, Object> result = ((List<Map<String, Object>>) data.get("results")).get(0);
        if (result.containsKey("geometry")) {
          Map<String, Object> geometry = (Map<String, Object>) result.get("geometry");
          if (geometry.containsKey("location")) {
            Map<String, Double> location = (Map<String, Double>) geometry.get("location");
            coordinates[0] = location.get("lat");
            coordinates[1] = location.get("lng");
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return coordinates; // 배열 반환
  }

  // ---------------------------
  private boolean isAllowedExtension(String filename) {
    String[] allowedExtensions = {".jpg", ".jpeg", ".png"};
    for (String extension : allowedExtensions) {
      if (filename.toLowerCase().endsWith(extension)) {
        return true;
      }
    }
    return false;
  }

  @PostMapping("insert")
  public String insertSubmit(FormBean bean, UploadFile uploadFile, BindingResult result, HttpSession session) {

    String business_num = (String) session.getAttribute("business_num");

    InputStream inputStream = null;

    OutputStream outputStream = null;

    MultipartFile file = uploadFile.getFile();

    String originalFilename = file.getOriginalFilename();
    String randomFilename = UUID.randomUUID().toString();
    ;

    if (!isAllowedExtension(originalFilename)) {
      return "container/errorExtension"; // 확장자가 허용되지 않으면 오류 페이지로 리턴합니다.
    }

    String fileExtension = "";

    if (originalFilename != null && originalFilename.contains(".")) {
      fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    // 파일명에 랜덤 문자열을 추가하여 새 파일명 생성
    if (originalFilename != null && !originalFilename.isEmpty()) {
      // 파일명에 기존 파일명과 랜덤 값을 결합합니다.
      randomFilename = originalFilename.substring(0, originalFilename.lastIndexOf('.')) + "-" + randomFilename + fileExtension;
    } else {
      randomFilename += fileExtension;
    }

    // 작성자가 업로드한 파일명 > 서버 내부에서 관리하는 파일명
    if (result.hasErrors()) { // 에러가 있으면 트루야
      return "container/errorFile"; // 에러발생 (파일을 선택하지 않은 경우)시 수행
    }

    try {
      inputStream = file.getInputStream();

      // 프로젝트 루트 기준으로 파일 저장 경로 설정
      String fileSavePath = "static/" + randomFilename;

      File newFile = new File(fileSavePath);
      if (!newFile.getParentFile().exists()) {
        newFile.getParentFile().mkdirs(); // 상위 디렉토리가 없으면 생성
      }

      outputStream = new FileOutputStream(newFile);
      int read = 0;
      byte[] bytes = new byte[1024];
      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }
      bean.setCont_image(randomFilename);

    } catch (Exception e) {
      System.out.println("file submit err : " + e);
      return "container/error";
    } finally {
      try {
        inputStream.close();
        outputStream.close();
      } catch (Exception e2) {
        // TODO: handle exception
      }
    }

    boolean b = containDao.insertContainer(bean);


    String address = bean.getCont_addr();
    double[] coordinates = getCoordinatesFromAddress(address);
    bean.setCont_we(coordinates[0]);
    bean.setCont_kyung(coordinates[1]);

    if (b) {
      return "redirect:/owner/list";

    } else {
      return "container/error";
    }
  }


  @GetMapping("/detail")
  public String conDetail(@RequestParam("cont_no") String cont_no, Model model) {
    ContainerDto conDto = containDao.conDetail(cont_no);
    model.addAttribute("conDto", conDto);

    return "container/container_detail";
  }


  @GetMapping("/goUpdate")
  public String cont_update(@RequestParam("cont_no") String cont_no, Model model) {
    ContainerDto conDto = containDao.conDetail(cont_no);
    model.addAttribute("conDto", conDto);
    return "container/container_update";
  }

  @PostMapping("update")
  public String update(FormBean bean) {
    boolean b = containDao.update(bean);
    if (b)
      return "redirect:/owner/list"; // 수정 후 목록보기
    else
      return "container/error";
  }


  @GetMapping("delete")
  public String delete(@RequestParam("cont_no") String cont_no) {
    boolean b = containDao.delete(cont_no);
    if (b)
      return "redirect:/owner/list"; // 수정 후 목록보기
    else
      return "container/error";
  }
}
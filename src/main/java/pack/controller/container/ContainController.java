package pack.controller.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pack.model.DataDao;
import pack.model.container.ContainDao;
import pack.model.container.ContainDtoMap;
// 재민
@Controller
public class ContainController {
	@Autowired
	private ContainDao containDao;
	
	@GetMapping("conta")
	@ResponseBody
	public Map<String, Object> sangpumProcess(){
	List<Map<String, String>> list = new ArrayList<>();
	Map<String, String> data = null;
	
	for(ContainDtoMap c:containDao.getcontainAll()) { // 전체자료 읽음
		data = new HashMap<String, String>();
		data.put("con_no", c.getCon_no());
		data.put("con_name", c.getCon_name());
		data.put("con_addr", c.getCon_addr());
		data.put("we", c.getWe());
		data.put("kyung", c.getKyung());
		data.put("con_area", c.getCon_area());
		list.add(data); //작업을 반복한다
	}
	System.out.println(list);
	
	Map<String, Object> sanglist = new HashMap<>();
	sanglist.put("datas", list);
	return sanglist;
	}
}

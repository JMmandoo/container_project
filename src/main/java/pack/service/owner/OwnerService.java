package pack.service.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pack.model.owner.OwnerDao;
import pack.model.owner.OwnerDto;

@Service
public class OwnerService {

  @Autowired
  private OwnerDao ownerDao;

  public boolean ownerInsertData(OwnerDto ownerDto) {
    return ownerDao.ownerinsertData(ownerDto);
  }

  public OwnerDto ownerLoginProcess(String business_num, String owner_pwd) {
    return ownerDao.ownerloginProcess(business_num, owner_pwd);
  }

  public boolean ownerUpdate(OwnerDto ownerDto) {
    return ownerDao.ownerupdate(ownerDto);
  }

  public boolean ownerDelete(OwnerDto ownerDto) {
    return ownerDao.ownerdelete(ownerDto);
  }

  // 여기에 필요한 다른 메소드들을 추가할 수 있습니다.
}

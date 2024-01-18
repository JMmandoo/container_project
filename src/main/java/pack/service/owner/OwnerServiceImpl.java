package pack.service.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pack.model.owner.OwnerDao;
import pack.model.owner.OwnerDto;

@Service
public class OwnerServiceImpl implements OwnerService {

  @Autowired
  private OwnerDao ownerDao;

  @Override
  public boolean ownerInsertData(OwnerDto ownerDto) {
    return ownerDao.ownerinsertData(ownerDto);
  }

  @Override
  public OwnerDto ownerLoginProcess(String businessNum, String ownerPwd) {
    return ownerDao.ownerloginProcess(businessNum, ownerPwd);
  }

  @Override
  public boolean ownerUpdate(OwnerDto ownerDto) {
    return ownerDao.ownerupdate(ownerDto);
  }

  @Override
  public boolean ownerDelete(OwnerDto ownerDto) {
    return ownerDao.ownerdelete(ownerDto);
  }
}

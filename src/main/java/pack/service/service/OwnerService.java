package pack.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pack.model.owner.OwnerDao;
import pack.model.owner.OwnerDto;

@Service
public class OwnerService {

  @Autowired
  private OwnerDao ownerDao;

  public boolean registerOwner(OwnerDto ownerDto) {
    return ownerDao.ownerinsertData(ownerDto);
  }

  public OwnerDto loginOwner(String businessNum, String password) {
    return ownerDao.ownerloginProcess(businessNum, password);
  }

  public boolean updateOwner(OwnerDto ownerDto) {
    return ownerDao.ownerupdate(ownerDto);
  }

  public boolean deleteOwner(OwnerDto ownerDto) {
    return ownerDao.ownerdelete(ownerDto);
  }
}


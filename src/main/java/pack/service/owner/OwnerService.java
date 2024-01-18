package pack.service.owner;

import pack.model.owner.OwnerDto;

public interface OwnerService {

  boolean ownerInsertData(OwnerDto ownerDto);

  OwnerDto ownerLoginProcess(String businessNum, String ownerPwd);

  boolean ownerUpdate(OwnerDto ownerDto);

  boolean ownerDelete(OwnerDto ownerDto);
}

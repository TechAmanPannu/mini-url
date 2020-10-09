package com.miniurl.validator;

import com.miniurl.dao.AccountDao;
import com.miniurl.dao.DomainDao;
import com.miniurl.entity.Account;
import com.miniurl.entity.Domain;
import com.miniurl.enums.DomainConfigStatus;
import com.miniurl.model.request.DomainCreateRequest;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;

public class DomainValidator {

    @Autowired
    private DomainDao domainDao;

    @Autowired
    private AccountDao accountDao;

    public void validateUpdate(String id, DomainConfigStatus status){

        Preconditions.checkArgument(ObjUtil.isBlank(id), "Invalid domain id to update");
        Preconditions.checkArgument(status == null, "Invalid status to update domain");

        Domain domain = domainDao.get(id);

        Preconditions.checkArgument(domain == null, "Invalid domain");
        Preconditions.checkArgument(status == DomainConfigStatus.CONFIGURED && (domain.getStatus() != DomainConfigStatus.PROCESSING
                && domain.getStatus() != DomainConfigStatus.REQUESTED), "domain is not requested or processsing to be configured");

        Preconditions.checkArgument(status == DomainConfigStatus.PROCESSING && (domain.getStatus() != DomainConfigStatus.REQUESTED), "domain is not requested to start processing");

        Preconditions.checkArgument(status == DomainConfigStatus.CANCELLED && (domain.getStatus() != DomainConfigStatus.PROCESSING
                && domain.getStatus() != DomainConfigStatus.REQUESTED), "domain is not requested or processsing to be cancelled");


    }

    public void validateCreate(String acctId, DomainCreateRequest domainCreateRequest){

        Preconditions.checkArgument(ObjUtil.isBlank(acctId), "Invalid accountId to create domain");
        Preconditions.checkArgument(domainCreateRequest == null, "Invalid domain create request");
        Preconditions.checkArgument(ObjUtil.isBlank(domainCreateRequest.getDomain()), "Invalid domain name to create domain");


        Domain domain = domainDao.get(domainCreateRequest.getDomain());
        Preconditions.checkArgument(domain != null,  "domain already exist, please try with different domain");

        Account account = accountDao.get(acctId);
        Preconditions.checkArgument(account == null, "Invalid account to create domain");

    }
}

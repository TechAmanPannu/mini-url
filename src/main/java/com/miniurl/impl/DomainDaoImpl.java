package com.miniurl.impl;

import com.miniurl.dao.AccountDao;
import com.miniurl.dao.DomainDao;
import com.miniurl.entity.Account;
import com.miniurl.entity.Domain;
import com.miniurl.enums.DomainConfigStatus;
import com.miniurl.model.request.DomainCreateRequest;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import com.miniurl.validator.DomainValidator;
import dev.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainDaoImpl extends DomainValidator implements DomainDao {

    @Autowired
    private Datastore datastore;

    @Autowired
    private AccountDao accountDao;

    @Override
    public Domain get(String id, DomainConfigStatus status){

        Preconditions.checkArgument(ObjUtil.isBlank(id), "Invalid id to fetch domain");

        return datastore.createQuery(Domain.class)
                .field("id").equal(id)
                .field("status").equal(status)
                .first();

    }

    @Override
    public Domain get(String id){

        Preconditions.checkArgument(ObjUtil.isBlank(id), "Invalid id to fetch domain");

        return datastore.createQuery(Domain.class)
                .field("id").equal(id)
                .first();

    }

    @Override
    public Domain create(String acctId, DomainCreateRequest domainCreateRequest) {

        super.validateCreate(acctId, domainCreateRequest);

        Domain domain = new Domain(domainCreateRequest.getDomain());
        domain.setStatus(DomainConfigStatus.REQUESTED);
        domain = save(domain);

        accountDao.addDomain(acctId, domainCreateRequest.getDomain());

        return domain;
    }

    @Override
    public Domain save(Domain domain){

        Preconditions.checkArgument(domain == null, "Invalid domain to save");

       return datastore.save(domain) != null ? domain : null;
    }

    @Override
    public Domain update(String id, DomainConfigStatus status) {

        super.validateUpdate(id, status);

        Domain domain = get(id);
        domain.setStatus(status);
        return save(domain);

    }
}

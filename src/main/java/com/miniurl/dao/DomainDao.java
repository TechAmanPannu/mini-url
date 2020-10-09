package com.miniurl.dao;

import com.miniurl.entity.Domain;
import com.miniurl.enums.DomainConfigStatus;
import com.miniurl.model.request.DomainCreateRequest;
import org.springframework.stereotype.Service;

@Service
public interface DomainDao {
    Domain get(String id, DomainConfigStatus status);

    Domain get(String id);

    Domain create(String acctId, DomainCreateRequest domainCreateRequest);

    Domain save(Domain domain);

    Domain update(String id, DomainConfigStatus status);
}

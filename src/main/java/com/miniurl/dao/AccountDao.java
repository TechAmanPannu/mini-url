package com.miniurl.dao;

import com.miniurl.entity.Account;
import com.miniurl.exception.EntityException;
import com.miniurl.model.request.AccountCreateRequest;
import org.springframework.stereotype.Service;

@Service
public interface AccountDao {

    Account create(AccountCreateRequest accountCreateRequest) throws EntityException;

    Account getByDomain(String domain);

    Account get(String id);

    Account save(Account account);

    void addDomain(String acctId, String domain);
}

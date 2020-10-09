package com.miniurl.impl;

import com.miniurl.dao.AccountDao;
import com.miniurl.dao.UserDao;
import com.miniurl.dao.UserRoleDao;
import com.miniurl.entity.Account;
import com.miniurl.entity.Domain;
import com.miniurl.entity.User;
import com.miniurl.entity.UserRole;
import com.miniurl.enums.EntityStatusType;
import com.miniurl.enums.RoleResourceType;
import com.miniurl.enums.RoleType;
import com.miniurl.exception.EntityException;
import com.miniurl.exception.enums.EntityErrorCode;
import com.miniurl.model.request.AccountCreateRequest;
import com.miniurl.model.request.UserCreateRequest;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import dev.morphia.Datastore;
import dev.morphia.Key;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private Datastore datastore;

    @Override
    public Account create(AccountCreateRequest accountCreateRequest) throws EntityException {

        Preconditions.checkArgument(accountCreateRequest == null, "Invalid account create request");
        Preconditions.checkArgument(ObjUtil.isNullOrEmpty(accountCreateRequest.getAccountName()), "Invalid account name");

        User user = userDao.create(accountCreateRequest.getUser());

        if (user == null)
            throw new EntityException(EntityErrorCode.CREATE_FAILED, "failed to create user");

        Account account = new Account(user.getAcctId(), accountCreateRequest.getAccountName(), user.getId(), EntityStatusType.ACTIVE);

        account = save(account);

        if (account == null)
            throw new EntityException(EntityErrorCode.CREATE_FAILED, "failed to create account");

        UserRole userRole = userRoleDao.createRole(RoleResourceType.ACCOUNT, account.getId(), user.getId(), RoleType.OWNER);

        if (userRole == null)
            throw new EntityException(EntityErrorCode.CREATE_FAILED, "Failed to create user role");

        return account;

    }

    @Override
    public Account getByDomain(String domain) {

        Preconditions.checkArgument(ObjUtil.isBlank(domain), "Invalid domain to get account");
        return datastore.createQuery(Account.class)
                .disableValidation()
                .field("domains").equal(new Key<>(Domain.class, "Domain", domain))
                .first();
    }

    @Override
    public Account get(String id) {

        Preconditions.checkArgument(ObjUtil.isBlank(id), "Invalid id to get account");
        return datastore.createQuery(Account.class)
                .field("id").equal(id).first();
    }

    @Override
    public Account save(Account account) {

        return datastore.save(account) != null ? account : null;
    }

    @Override
    public void addDomain(String acctId, String domain) {

        Preconditions.checkArgument(ObjUtil.isBlank(acctId), "Invalid acctId to add domain");

        Account account = get(acctId);
        Preconditions.checkArgument(account == null, "Invalid account to add domain");
        account.addDomain(domain);
        save(account);
    }

}

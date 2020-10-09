package com.miniurl.validator;

import com.miniurl.dao.AccountDao;
import com.miniurl.dao.DomainDao;
import com.miniurl.dao.UrlDao;
import com.miniurl.dao.UserDao;
import com.miniurl.entity.Account;
import com.miniurl.entity.Domain;
import com.miniurl.entity.Url;
import com.miniurl.entity.User;
import com.miniurl.enums.DomainConfigStatus;
import com.miniurl.enums.UrlAccessType;
import com.miniurl.model.request.UrlCreateRequest;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;

public class UrlValidator {

    @Autowired
    private DomainDao domainDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UrlDao urlDao;

    public void validate(UrlCreateRequest urlCreateRequest) {

        Preconditions.checkArgument(urlCreateRequest == null, "Invalid url to save");
        Preconditions.checkArgument(ObjUtil.isBlank(urlCreateRequest.getUrl()), "Invalid url string to create url");
        Preconditions.checkArgument(urlCreateRequest.getAccessType() == UrlAccessType.PRIVATE && ObjUtil.isBlank(urlCreateRequest.getCreatedBy()), "private url should contains created_by");
        Preconditions.checkArgument(ObjUtil.isBlank(urlCreateRequest.getCreatedBy()), "Invalid createdBy to create url");
        User user = userDao.get(urlCreateRequest.getCreatedBy());
        Preconditions.checkArgument(user == null, "Invalid user to create url");

        if (ObjUtil.isBlank(urlCreateRequest.getDomain())) {
            if (!ObjUtil.isBlank(urlCreateRequest.getCustomLinkId())) {
                Url url = urlDao.get(null, urlCreateRequest.getCustomLinkId());
                Preconditions.checkArgument(url != null, "url already exists with custom link Id : " + urlCreateRequest.getCustomLinkId());
            }
        } else if (!ObjUtil.isBlank(urlCreateRequest.getDomain())) {

            Domain domain = domainDao.get(urlCreateRequest.getDomain(), DomainConfigStatus.CONFIGURED);
            Preconditions.checkArgument(domain == null, "Invalid domain to create url");

            Account account = accountDao.getByDomain(domain.getId());
            Preconditions.checkArgument(account == null, "domain does not configured to any account");
            Preconditions.checkArgument(!user.getAcctId().equals(account.getId()), "domain does not belong to user account");


            if (!ObjUtil.isBlank(urlCreateRequest.getCustomLinkId())) {
                Url url = urlDao.get(domain.getId(), urlCreateRequest.getCustomLinkId());
                Preconditions.checkArgument(url != null, "url already exists with domain : " + domain + " and custom link Id : " + urlCreateRequest.getCustomLinkId());
            }
        }
    }
}

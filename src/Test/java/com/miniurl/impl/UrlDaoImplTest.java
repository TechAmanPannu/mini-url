package com.miniurl.impl;

import com.miniurl.AbstractMongoDBTest;
import com.miniurl.entity.Account;
import com.miniurl.entity.Domain;
import com.miniurl.entity.Url;
import com.miniurl.enums.DomainConfigStatus;
import com.miniurl.enums.UrlAccessType;
import com.miniurl.utils.ObjUtil;
import dev.morphia.Datastore;
import dev.morphia.Key;
import dev.morphia.Morphia;
import dev.morphia.query.FindOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UrlDaoImplTest extends AbstractMongoDBTest {


    private Datastore datastore;

    @Before
    public void before() throws Exception {
        setUp();

        Morphia morphia  = new Morphia();
        morphia.mapPackage("com.miniurl.entity");

        datastore = morphia.createDatastore(getMongo(), "test");

    }


    @After
    public void afterSetup() throws Exception {
        super.tearDown();
    }

    @Test
    public void testDown()
    {


        System.out.println("output : "+new UrlDaoImpl().generateId("es.pa", "home"));

//        Account account = new Account();
//        account.setId("123");
//        account.addDomain("hub.domain");
//
//        Domain domain = new Domain();
//        domain.setStatus(DomainConfigStatus.REQUESTED);
//        domain.setId("hub.domain");
//
//        datastore.save(domain);
//        datastore.save(account);
//
//
//        System.out.println("output:"+ObjUtil.getJson(datastore.createQuery(Account.class)
//                .disableValidation()
//                .field("domains").equal(new Key(Domain.class, "Domain", "hub.domain")).find().toList()));
//



    }

}
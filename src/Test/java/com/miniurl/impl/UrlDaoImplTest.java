package com.miniurl.impl;


import com.devskiller.friendly_id.FriendlyId;
import com.miniurl.entity.contact.Contact;
import com.miniurl.model.Text;
import com.miniurl.utils.EncodeUtil;
import com.miniurl.utils.ObjUtil;

class UrlDaoImplTest {

    public static void main(String[] arg) {

        Text  text = new Text("1234");

        Contact contact = new Contact();
        contact.setEmail(null);


        System.out.println(text.getValue());
        System.out.println(ObjUtil.getJson(contact));
        System.out.println(EncodeUtil.Base62.encode(100000));
    }
}

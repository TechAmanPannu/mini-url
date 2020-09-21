package com.miniurl.dao;

import com.miniurl.entity.UrlKeyCounter;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlKeyCounterDao {

    UrlKeyCounter incrementByOne();

    UrlKeyCounter
}

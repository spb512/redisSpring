package com.ac.pt.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.ac.pt.model.Account;

public class AccountService {
	@Cacheable(value="commonCache")// 使用了一个缓存名叫 accountCache
    public Account getAccountByName(String userName) {
        // 方法内部实现不考虑缓存逻辑，直接实现业务
        System.out.println("real query account."+userName);
        return getFromDB(userName);
    }
 
    private Account getFromDB(String acctName) {
        System.out.println("real querying db..."+acctName);
        account.setName(acctName);
        account.setId(1);
        return account;
    }
    @CacheEvict(value="commonCache",key="#account.getName()")// 清空accountCache 缓存
    public void updateAccount1(Account account) {
       updateDB(account);
    }
}

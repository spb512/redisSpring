package com.ac.pt.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.ac.pt.model.Account;

public class AccountService {
	@Cacheable(value="commonCache")// ʹ����һ���������� accountCache
    public Account getAccountByName(String userName) {
        // �����ڲ�ʵ�ֲ����ǻ����߼���ֱ��ʵ��ҵ��
        System.out.println("real query account."+userName);
        return getFromDB(userName);
    }
 
    private Account getFromDB(String acctName) {
        System.out.println("real querying db..."+acctName);
        account.setName(acctName);
        account.setId(1);
        return account;
    }
    @CacheEvict(value="commonCache",key="#account.getName()")// ���accountCache ����
    public void updateAccount1(Account account) {
       updateDB(account);
    }
}

package com.kwkim.demospringsecurityform.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {
    //authentication 을 요청할때 데이터 엑세스 오브젝트를 사용해서 인증을할때 사용하는 인터페이스
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    //인터페이스 조건: param1에있는 유저 네임을 가지고있는 유저정보 데이터를 가지고 UserDetails 타입으로 만들어서 반환
    //유저정보를 제공하는 역할을 하는것 인메모리 유저이든 다른 방식의 유저이든 상관없이
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        } else {
            return User.builder().username(account.getUsername()).password(account.getPassword()).roles(account.getRole()).build();
        }
    }

    public Account createNewAccount(Account account) {
        account.encodePassword(passwordEncoder);
        return accountRepository.save(account);
    }
}

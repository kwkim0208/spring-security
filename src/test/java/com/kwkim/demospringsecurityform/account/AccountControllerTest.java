package com.kwkim.demospringsecurityform.account;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//트랜잭션을 설정해줘야 같은 데이터를 쓰는 테스트가 영향 없이 서로 독립성을 보장 받을수있음 해당 어노테이션은 클래스 위에 써도 되고 메서드 위에 적어주면 된다.
class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;

    @Test
    public void index_anonymous() throws Exception {
        mockMvc.perform(get("/").with(anonymous())).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void index_user() throws Exception {
//        user("kwkim").roles("USER"))  유저가 존재한다는 가정을 해서 하는거임 디비에 실데이터를 확인하는 과정은 아님 말그대로의 목킹
        mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "kwkim", roles = "USER")
    public void admin_user() throws Exception {
//        user("kwkim").roles("USER"))  유저가 존재한다는 가정을 해서 하는거임 디비에 실데이터를 확인하는 과정은 아님 말그대로의 목킹
        mockMvc.perform(get("/admin")).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    public void admin_admin() throws Exception {
//        user("kwkim").roles("USER"))  유저가 존재한다는 가정을 해서 하는거임 디비에 실데이터를 확인하는 과정은 아님 말그대로의 목킹
        mockMvc.perform(get("/admin").with(user("kwkim").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
    }


    @Test
    public void login_success() throws Exception {
        String username="kwkim";
        String password="1234";
        Account user=this.createUser(username,password);
        mockMvc.perform(formLogin().user(user.getUsername()).password("1234"))
                .andExpect(authenticated());
    }
    @Test
    public void login_fail() throws  Exception{
        String username="kwkim";
        String password="1234";
        Account user=this.createUser(username,password);
        mockMvc.perform(formLogin().user(user.getUsername()).password("12345")).andExpect(unauthenticated());
    }

    private Account createUser(String username,String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setRole("USER");
       return  accountService.createNewAccount(account);
    }
}
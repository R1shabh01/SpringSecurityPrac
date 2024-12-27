package com.example.Practice.Service;

import com.example.Practice.Model.UserPrincipal;
import com.example.Practice.Model.Users;
import com.example.Practice.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class myUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo repo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repo.findByUsername(username);
        System.out.println(user.getUsername() + "   "+user.getPassword());
        if(user == null){
            throw new UsernameNotFoundException("No User Found By this Name");
        }else{
            return new UserPrincipal(user);
        }
    }


}

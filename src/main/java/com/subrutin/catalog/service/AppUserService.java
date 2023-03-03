package com.subrutin.catalog.service;

import com.subrutin.catalog.dto.UserDetailResponseDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {

    public UserDetailResponseDTO findUserDetail();


}

package com.example.auth_service.Auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.auth_service.excep.AlreadyExistException;
import com.example.auth_service.model.dto.req.AuthReq;
import com.example.auth_service.model.dto.res.AuthRes;
import com.example.auth_service.model.entity.Auth;
import com.example.auth_service.model.entity.Role;
import com.example.auth_service.model.enums.ROLE;
import com.example.auth_service.repo.AuthRepo;
import com.example.auth_service.repo.RoleRepo;
import com.example.auth_service.service.AuthServ;
import com.example.auth_service.service.JwtServ;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthRepo authRepo;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtServ jwtServ;

    @InjectMocks
    private AuthServ authService;

    @Test
    public void register_ShouldThrowAlreadyExistException_WhenUsernameExists() {
        AuthReq req = new AuthReq();
        req.setUsername("user1");
        req.setEmail("user@example.com");

        // Giả lập user đã tồn tại với cùng username
        when(authRepo.findOne((Specification<Auth>) any())).thenReturn(Optional.of(new Auth()));

        // Giả lập role tồn tại
        Role userRole = new Role();
        userRole.setName(ROLE.USER);
        when(roleRepo.findByName(ROLE.USER)).thenReturn(Optional.of(userRole));

        AlreadyExistException thrown = assertThrows(AlreadyExistException.class, () -> {
            authService.register(req);
        });

        assertEquals("User already exist", thrown.getMessage());
    }

    @Test
    public void register_ShouldThrowAlreadyExistException_WhenEmailExists() {
        AuthReq req = new AuthReq();
        req.setUsername("newuser");
        req.setEmail("user@example.com");

        // Giả lập: username chưa tồn tại, nhưng email đã tồn tại
        when(authRepo.findOne((Specification<Auth>) any()))
                .thenReturn(Optional.empty()) // Chưa tồn tại username
                .thenReturn(Optional.of(new Auth())); // Tồn tại email

        AlreadyExistException thrown = assertThrows(AlreadyExistException.class, () -> {
            authService.register(req);
        });

        assertEquals("Email already exist", thrown.getMessage());
    }

    @Test
    public void register_ShouldReturnAuthRes_WhenValidRequest() {
        AuthReq req = new AuthReq();
        req.setUsername("newuser");
        req.setEmail("user@example.com");
        req.setPassword("password123");

        // Tạo role user
        Role userRole = new Role();
        userRole.setName(ROLE.USER);

        // Giả lập các hành vi
        when(authRepo.findOne((Specification<Auth>) any())).thenReturn(Optional.empty());
        when(roleRepo.findByName(ROLE.USER)).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(any())).thenReturn("encodedPwd");
        when(jwtServ.accessToken(any())).thenReturn("access-token");
        when(jwtServ.refreshToken(any())).thenReturn("refresh-token");

        // Gọi method register từ service
        AuthRes res = authService.register(req);

        // Kiểm tra kết quả trả về
        assertEquals("access-token", res.getAccess());
        assertEquals("refresh-token", res.getRefresh());
    }
}

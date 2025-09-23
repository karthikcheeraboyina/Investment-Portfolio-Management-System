package com.example.ipmsBackend;

import com.example.ipmsBackend.entity.User;
import com.example.ipmsBackend.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IpmsBackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserServiceImpl userService;

	@Autowired
	private ObjectMapper objectMapper;

	private User sampleUser;


	@Test
	void contextLoads() {

	}

	@BeforeEach
	void setUp() {
		sampleUser = new User();
		sampleUser.setId(1L);
		sampleUser.setUsername("testuser");
		sampleUser.setEmail("test@example.com");
		sampleUser.setPassword("password");
		sampleUser.setRole(User.UserRole.INVESTOR);
	}

	@Test
	void testRegisterUser_Success() throws Exception {

		when(userService.registerUser(any(User.class))).thenReturn(sampleUser);

		mockMvc.perform(post("/api/user/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(sampleUser)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.username", is(sampleUser.getUsername())));
	}

	@Test
	void testLoginUser_Success() throws Exception {
		when(userService.loginUser("testuser", "password")).thenReturn(Optional.of(sampleUser));

		mockMvc.perform(post("/api/user/login")
						.param("username", "testuser")
						.param("password", "password"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is("testuser")));
	}

	@Test
	void testLoginUser_Unauthorized() throws Exception {
		when(userService.loginUser(anyString(), anyString())).thenReturn(Optional.empty());

		mockMvc.perform(post("/api/user/login")
						.param("username", "wronguser")
						.param("password", "wrongpass"))
				.andExpect(status().isUnauthorized());
	}


	@Test
	void testGetUserById_NotFound() throws Exception {
		when(userService.getUserById(anyLong())).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/user/{userId}", 99L))
				.andExpect(status().isNotFound());
	}

	@Test
	void testDeleteUser() throws Exception {
		doNothing().when(userService).deleteUser(1L);

		mockMvc.perform(delete("/api/user/{userId}", 1L))
				.andExpect(status().isNoContent());

		verify(userService, times(1)).deleteUser(1L);
	}
}
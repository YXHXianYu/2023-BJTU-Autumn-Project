package com.yxhxianyu.peerlearningsystem;

import com.yxhxianyu.peerlearningsystem.dao.ProblemDao;
import com.yxhxianyu.peerlearningsystem.dao.UserDao;
import com.yxhxianyu.peerlearningsystem.pojo.UserPojo;
import com.yxhxianyu.peerlearningsystem.service.ProblemService;
import com.yxhxianyu.peerlearningsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PeerLearningSystemApplicationTests {

	@Autowired
	UserService userService;

	@Autowired
	ProblemService problemService;

	@Test
	void userServiceTest() {

//		userService.insertUser("YXH_XianYu", "20021012", "2943003@qq.com", 2);
		userService.insertUser("alice", "123456", "", 0);

		UserPojo alice = userService.getUserByName("alice");
		userService.deleteUser(alice.getUuid());
	}

	@Test
	void userServiceDuplicateKeyException() {
		// org.springframework.dao.DuplicateKeyException
		userService.insertUser("duplicate key", "123456", "", 0);
		userService.insertUser("duplicate key", "123456", "", 0);
	}

	@Test
	void userServiceDataTooLong() {
		// org.springframework.dao.DataIntegrityViolationException
		userService.insertUser(
				"data too long",
				"0123456789012345678901234567890123456789012345678901234567890123456789",
				"",
				0);
	}

	@Test
	void userServiceNullPointerException() {
		userService.deleteUserByName("alice233");
	}

}

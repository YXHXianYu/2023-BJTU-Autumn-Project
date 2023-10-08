package com.yxhxianyu.peerlearningsystem;

import com.yxhxianyu.peerlearningsystem.dao.ProblemDao;
import com.yxhxianyu.peerlearningsystem.dao.UserDao;
import com.yxhxianyu.peerlearningsystem.pojo.GroupHomeworkPojo;
import com.yxhxianyu.peerlearningsystem.pojo.RatingPojo;
import com.yxhxianyu.peerlearningsystem.pojo.UserPojo;
import com.yxhxianyu.peerlearningsystem.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class PeerLearningSystemApplicationTests {

	@Autowired
	UserService userService;

	@Autowired
	ProblemService problemService;

	@Autowired
	GroupHomeworkService groupHomeworkService;

	@Autowired
	HomeworkService homeworkService;

	@Autowired
	RatingService ratingService;

	@Test
	void groupHomeworkTest() {

		problemService.insertProblem("Goz总力战", "请大家通关Goz总力战TOR", "6");
		problemService.insertProblem("竞技场前10", "请大家打上BA国际服竞技场前10", "6");

		groupHomeworkService.insertGroupHomework(
				"班级一的作业",
				problemService.getProblemByName("Goz总力战").getUuid(),
				Date.valueOf(LocalDate.of(2023, 10, 10)),
				Date.valueOf(LocalDate.of(2023, 10, 12))
		);

		groupHomeworkService.insertGroupHomework(
				"班级二的作业",
				problemService.getProblemByName("Goz总力战").getUuid(),
				Date.valueOf(LocalDate.of(2023, 10, 10)),
				Date.valueOf(LocalDate.of(2023, 10, 12))
		);

		groupHomeworkService.insertGroupHomework(
				"班级二的作业",
				"123",
				Date.valueOf(LocalDate.of(2023, 10, 10)),
				Date.valueOf(LocalDate.of(2023, 10, 12))
		);

		{
			System.out.println("班级作业有: ");
			List<GroupHomeworkPojo> allGroupHomeworks = groupHomeworkService.getAllGroupHomeworks();
			for(int i = 0; i < allGroupHomeworks.size(); i++) {
				System.out.println(i + ": " + allGroupHomeworks.get(i));
			}
		}

		groupHomeworkService.updateState(groupHomeworkService.getUUIDByName("班级二的作业"), GroupHomeworkService.HOMEWORK_STATE_START_RATING);

		{
			System.out.println("班级作业有: ");
			List<GroupHomeworkPojo> allGroupHomeworks = groupHomeworkService.getAllGroupHomeworks();
			for(int i = 0; i < allGroupHomeworks.size(); i++) {
				System.out.println(i + ": " + allGroupHomeworks.get(i));
			}
		}

		String yxh_hw = homeworkService.insertHomework(
				groupHomeworkService.getUUIDByName("班级二的作业"),
				userService.getUUIDByName("YXH_XianYu")
		);

		String bob_hw = homeworkService.insertHomework(
				groupHomeworkService.getUUIDByName("班级一的作业"),
				userService.getUUIDByName("bob")
		);

		ratingService.insertRating(
				yxh_hw,
				userService.getUUIDByName("YXH_XianYu"),
				5.0f
		);

		ratingService.insertRating(
				yxh_hw,
				userService.getUUIDByName("bob"),
				10.0f
		);

		ratingService.insertRating(
				bob_hw,
				userService.getUUIDByName("YXH_XianYu"),
				2.0f
		);

		{
			System.out.println("互评记录有: ");
			List<RatingPojo> allRatings = ratingService.getAllRatings();
			allRatings.forEach(System.out::println);
		}

	}

	@Test
	void userServiceTest() {

//		userService.insertUser("YXH_XianYu", "20021012", "2943003@qq.com", 2);
		userService.insertUser("alice", "123456", "", 0);

		UserPojo alice = userService.getUserByName("alice");
		userService.deleteUserByUUID(alice.getUuid());
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
	void userServiceShowAllUsers() {
		System.out.println(userService.getAllUsers());
	}

	@Test
	void userServiceNullPointerException() {
		userService.deleteUserByName("alice233");
	}

}

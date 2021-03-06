package com.climatetree.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.climatetree.user.dao.RoleDao;
import com.climatetree.user.dao.UserDao;
import com.climatetree.user.dto.Execution;
import com.climatetree.user.enums.Constants;
import com.climatetree.user.enums.ResultEnum;
import com.climatetree.user.model.Role;
import com.climatetree.user.model.User;

/**
 * The type User service.
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	public UserService() {
	}

	public UserService(UserDao dao) {
		this.userDao = dao;
	}

	/**
	 * Gets user.
	 *
	 * @param userId the user id
	 * @return the user
	 */
	public Execution<User> getUser(Long userId) {
		Execution<User> res = null;
		try {
			User user = userDao.findByUserId(userId);
			if (user != null) {
				res = new Execution<>(ResultEnum.SUCCESS, user);
			} else {
				res = new Execution<>(ResultEnum.DATABASE_ERROR);
			}
		} catch (Exception e) {
			res = new Execution<>(ResultEnum.INNER_ERROR);
		}
		return res;
	}

	/**
	 * Gets user.
	 *
	 * @param email the user id
	 * @return the user
	 */
	public Execution<User> getUserByEmail(String email) {
		Execution<User> res = null;
		try {
			User user = userDao.findByEmail(email);
			if (user != null) {
				res = new Execution<>(ResultEnum.SUCCESS, user);
			} else {
				res = new Execution<>(ResultEnum.DATABASE_ERROR);
			}
		} catch (Exception e) {
			res = new Execution<>(ResultEnum.INNER_ERROR);
		}
		return res;
	}

	/**
	 * Gets users.
	 *
	 * @return the users
	 */
	public Execution<User> findAllUsers() {
		Execution<User> res;
		try {
			List<User> users = userDao.findAll();
			res = new Execution<>(ResultEnum.SUCCESS, users);
		} catch (Exception e) {
			res = new Execution<>(ResultEnum.INNER_ERROR);
		}
		return res;
	}

	/**
	 * Insert user execution.
	 *
	 * @param user the user
	 * @return the execution
	 */
	public Execution<User> insertUser(User user) {
		Execution<User> res;
		try {
			userDao.save(user);
			res = new Execution<>(ResultEnum.SUCCESS, 1);
		} catch (Exception e) {
			res = new Execution<>(ResultEnum.INNER_ERROR);
		}
		return res;
	}

	/**
	 * Delete user execution.
	 *
	 * @param userId the user id
	 * @return the execution
	 */
	public Execution<User> deleteUser(Long userId) {
		Execution<User> res;
		try {
			User user = userDao.findByUserId(userId);
			if (user == null) {
				res = new Execution<>(ResultEnum.DATABASE_ERROR);
			} else {
				userDao.deleteById(user.getUserId());
				res = new Execution<>(ResultEnum.SUCCESS, 1);
			}
		} catch (Exception e) {
			res = new Execution<>(ResultEnum.INNER_ERROR);
		}
		return res;
	}

	/**
	 * Delete user execution.
	 *
	 * @param userId the user id
	 * @return the execution
	 */
	public Execution<User> updateUser(Long userId, Integer roleId) {
		Execution<User> res;
		try {
			User user = userDao.findByUserId(userId);
			if (user == null) {
				res = new Execution<>(ResultEnum.DATABASE_ERROR);
			} else {
				Role newRole = roleDao.findByRoleId(roleId);
				Role oldRole = user.getRole();
				if (oldRole.getRoleId() == newRole.getRoleId()) {
					res = new Execution<>(ResultEnum.FORBIDDEN);
				} else {
					user.setRole(newRole);
					userDao.save(user);
					res = new Execution<>(ResultEnum.SUCCESS, 1);
				}
			}
		} catch (Exception e) {
			res = new Execution<>(ResultEnum.INNER_ERROR);
		}
		return res;
	}

	/**
	 * Gets users by name.
	 *
	 * @param name the name
	 * @return the users by name
	 */
	public Execution<User> getUsersByName(String name) {
		Execution<User> res;
		try {
			List<User> users = userDao.findByNickname(name);
			res = new Execution<>(ResultEnum.SUCCESS, users);
		} catch (Exception e) {
			res = new Execution<>(ResultEnum.INNER_ERROR);
		}
		return res;
	}

	/**
	 * Gets users by role id.
	 *
	 * @param roleId the role id
	 * @return the users by role id
	 */
	public Execution<User> getUsersByRoleId(Integer roleId) {
		Execution<User> res;
		try {
			List<User> users = userDao.findByRoleId(roleId);
			res = new Execution<>(ResultEnum.SUCCESS, users);
		} catch (Exception e) {
			res = new Execution<>(ResultEnum.INNER_ERROR);
		}
		return res;
	}

	/**
	 * Get all users with a TRUE flag
	 * 
	 * @return a lists of flagged users
	 */
	public Execution<User> getBlacklistedUsers() {
		Execution<User> res;
		try {
			List<User> users = userDao.findByBlacklistedTrue();
			res = new Execution<>(ResultEnum.SUCCESS, users);
		} catch (Exception e) {
			res = new Execution<>(ResultEnum.INNER_ERROR);
		}
		return res;
	}

	public void setupRoles() {

		List<Role> roles = new ArrayList<>();
		roles.add(new Role(1, Constants.ADMIN.name()));
		roles.add(new Role(2, Constants.MODERATOR.name()));
		roles.add(new Role(3, Constants.REGISTERED_USERS.name()));
		roles.forEach(r -> roleDao.save(r));

	}

	public Execution<User> blacklistUser(Long userId) {
		Execution<User> res;
		try {
			User user = userDao.findByUserId(userId);
			if (user == null) {
				res = new Execution<>(ResultEnum.DATABASE_ERROR);
			} else {

				user.setBlacklisted(true);
				userDao.save(user);
				res = new Execution<>(ResultEnum.SUCCESS, 1);
			}
		} catch (Exception e) {
			res = new Execution<>(ResultEnum.INNER_ERROR);
		}
		return res;
	}


	public Execution<User> unblacklistUser(Long userId) {
		Execution<User> res;
		try {
			User user = userDao.findByUserId(userId);
			if (user == null) {
				res = new Execution<>(ResultEnum.DATABASE_ERROR);
			} else {

				user.setBlacklisted(false);
				userDao.save(user);
				res = new Execution<>(ResultEnum.SUCCESS, 1);
			}
		} catch (Exception e) {
			res = new Execution<>(ResultEnum.INNER_ERROR);
		}
		return res;
	}
}

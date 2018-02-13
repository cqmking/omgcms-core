package com.omgcms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.omgcms.model.core.User;

public interface UserRepository extends JpaSpecificationExecutor<User>, JpaRepository<User, Long> {

	// 根据 userAccount 来获取对应的 User
	public User getByUserAccount(String userAccount);
	
	/**
	 * Get user by user email
	 * @param email Email address
	 * @return User obejct
	 */
	public User getByEmail(String email);
	
	// 根据 userId 获取用户信息
	public User getByUserId(long userId);
	
}

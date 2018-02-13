package com.omgcms.security;

import com.omgcms.model.core.ResourcePermission;
import com.omgcms.model.core.Role;
import com.omgcms.model.core.User;
import com.omgcms.service.ResourcePermissionService;
import com.omgcms.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DbAuthorizingRealm extends AuthorizingRealm {

	private Logger logger = LoggerFactory.getLogger(DbAuthorizingRealm.class);

	@Autowired
	private UserService userService;
	@Autowired
	private ResourcePermissionService permissionService;

	/**
	 * 角色权限认证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		logger.debug("doGetAuthorizationInfo function is running!");
		// 获取登录时输入的用户名
		// String userAccount = (String)
		// principals.fromRealm(getName()).iterator().next(); //same as below
		User user = (User) principals.getPrimaryPrincipal();

		if (user != null) {

			logger.debug("1:Call doGetAuthorizationInfo method to check role and permission for user:" + user.getUserAccount());

			// 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

			// Set<Role> rolesSet = user.getUserRoles();
			
			// Set<String> roles = getRolesFromCollection(rolesSet);
			
			// 用户的角色集合
			// info.setRoles(roles);
			// 用户的角色对应的所有权限
			// info.setStringPermissions(permissions);
			
			return info;
		}

		return null;
	}

	/**
	 * 登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

		// UsernamePasswordToken对象用来存放提交的登录信息
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

		logger.debug("Call doGetAuthenticationInfo method to check is this a valid user:{}", token.getUsername());

		// 查出是否有此用户
		User user = userService.getByUserAccount(token.getUsername());
		if (user != null) {
			// 若存在，将此用户存放到登录认证info中

			SimpleAuthenticationInfo sai = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
			sai.setCredentialsSalt(ByteSource.Util.bytes(user.getSalt()));
			
			return sai;

		}

		return null;
	}

	/**
	 * 清除缓存，权限角色缓存
	 */
	public void clearRealmCache() {
		PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
		super.clearCache(principals);
		logger.debug("Called clearRealmCache, the roles and permissions in cache has been removed.");
	}

	private Set<String> getRolesFromCollection(Collection<Role> roleCollection) {

		Set<String> roles = new HashSet<String>();
		for (Role role : roleCollection) {
			roles.add(role.getRoleKey());
		}
		return roles;
	}

	private Set<String> getPermissionsFromCollection(Collection<ResourcePermission> permissionCollection) {

		Set<String> permissions = new HashSet<String>();
//		for (ResourcePermission permission : permissionCollection) {
//			permissions.add(permission.getPermissionKey());
//		}
		return permissions;

	}
}

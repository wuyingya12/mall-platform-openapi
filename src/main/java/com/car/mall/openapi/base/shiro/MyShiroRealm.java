package com.car.mall.openapi.base.shiro;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyShiroRealm extends AuthorizingRealm {
	private static Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);

	@Value("cinema.account.key")
	private String cinemaAccountKey;

//	@Autowired
//	private RedisUtil redisUtil;

	@Override
	public boolean supports(AuthenticationToken token) {
		// 仅支持StatelessToken类型的Token
		return token instanceof StatelessToken;
	}
	/**
	 * 此方法调用 hasRole,hasPermission的时候才会进行回调. 权限信息.(授权): 1、如果用户正常退出，缓存自动清空；
	 * 2、如果用户非正常退出，缓存自动清空； 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。
	 * （需要手动编程进行实现；放在service进行调用）
	 * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例， 调用clearCached方法；
	 * :Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
	 * 
	 * @param principals
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		/*
		 * 当没有使用缓存的时候，不断刷新页面的话，这个代码会不断执行， 当其实没有必要每次都重新设置权限信息，所以我们需要放到缓存中进行管理；
		 * 当放到缓存中时，这样的话，doGetAuthorizationInfo就只会执行一次了， 缓存过期之后会再次执行。
		 */
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

//		CinemaSessionData cinemaSessionData = (CinemaSessionData) SecurityUtils.getSubject().getPrincipal();
//		if(null == cinemaSessionData ){
//			return authorizationInfo;
//		}
//		// 根据用户ID查询权限（permission），放入到Authorization里。
//		Set<String> permList = new HashSet<String>();
//		CollectionParam<FunctionTreeModel> collectionParam = cinemaAccountService.queryMerchantMenuList(cinemaSessionData.getAccountName());
//		if(null==collectionParam || collectionParam.getStatus()!= ReturnCode.PROCESS_SUCCESS){
//			logger.error(cinemaSessionData.getAccountName()+"登录账号获取操作权限失败："+ JSONObject.toJSONString(collectionParam));
//			return authorizationInfo;
////		}
//		List<FunctionTreeModel> functionTreeList = collectionParam.getDatas();
//		if(null!=functionTreeList && functionTreeList.size()>0){
//			for (FunctionTreeModel functionTreeModel:functionTreeList) {
//				if(StringUtils.isNotEmpty(functionTreeModel.getDescription())){
//					permList.add(functionTreeModel.getDescription());
//				}
//			}
//		}
//        authorizationInfo.addStringPermissions(permList);
		return authorizationInfo;
	}

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {


		StatelessToken token = (StatelessToken)authcToken;

		logger.info("验证当前Subject时获取到token为" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));

		String username = token.getUserName();

		// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以在此判断或自定义实现
//		CinemaSessionData cinemaSessionData = new CinemaSessionData();
//		SingleParam<MerchantInfo> singleParam = cinemaAccountService.queryMerchantInfo(username);
//		if(null!=singleParam && singleParam.getStatus()==ReturnCode.PROCESS_SUCCESS){
//			MerchantInfo merchantInfo = singleParam.getData();
//			if(null != merchantInfo){
//				cinemaSessionData.setAccountName(merchantInfo.getMemberName());
//				SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(cinemaSessionData, merchantInfo.getPassword(),getName());
//				return authenticationInfo;
//			}
//		}
		return null;
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

	/**
	 * 
	 * @Title: clearAuthz
	 * @Description: TODO 清楚缓存的授权信息
	 * @return void 返回类型
	 */
	public void clearAuthz() {
		this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
	}

	/**
	 * 指定principalCollection 清除
	 */
	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principalCollection) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection, getName());
		super.clearCachedAuthorizationInfo(principals);
	}
}
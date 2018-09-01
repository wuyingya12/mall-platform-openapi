package com.car.mall.openapi.base.shiro;

import com.car.mall.openapi.base.shiro.filter.StatelessAuthcFilter;
import com.car.mall.openapi.utils.RedisUtil;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
	private static Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

	/**
	 * 为了解决使用自定义过滤器的问题
	 * 解决UnavailableSecurityManagerException问题
	 */
	@Bean
	public FilterRegistrationBean delegatingFilterProxy(){
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		DelegatingFilterProxy proxy = new DelegatingFilterProxy();
		proxy.setTargetFilterLifecycle(true);
		proxy.setTargetBeanName("shiroFilter");
		filterRegistrationBean.setFilter(proxy);
		return filterRegistrationBean;
	}
	/**
	 * ShiroFilterFactoryBean 处理拦截资源文件问题。
	 * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
	 * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
	 * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
	 * 3、部分过滤器可指定参数，如perms，roles
	 */
	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {

		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		//权限配置（anon是不验证的权限）
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		filterChainDefinitionMap.put("/api/account/login", "anon");
		filterChainDefinitionMap.put("/api/account/nologin", "anon");
		filterChainDefinitionMap.put("/api/account/logout", "logout");

		filterChainDefinitionMap.put("/swagger-resources/**", "anon");
		filterChainDefinitionMap.put("/webjars/**", "anon");
		filterChainDefinitionMap.put("/v2/api-docs", "anon");
		filterChainDefinitionMap.put("/swagger-ui*", "anon");

		Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
		filtersMap.put("stateAuthc", statelessAuthcFilter());
		shiroFilterFactoryBean.setFilters(filtersMap);

		//调用接口查询全部的权限链接
//		CollectionParam<FunctionTreeModel> collectionParam = cinemaAccountService.queryAllMenuList();
//		if(null!=collectionParam || collectionParam.getStatus()== ReturnCode.PROCESS_SUCCESS){
//			List<FunctionTreeModel> functionTreeList = collectionParam.getDatas();
//			if(null!=functionTreeList && functionTreeList.size()>0){
//				for (FunctionTreeModel functionTreeModel:functionTreeList) {
//					if(StringUtils.isNotEmpty(functionTreeModel.getChildrenUrl()) && StringUtils.isNotEmpty(functionTreeModel.getDescription())){
//						filterChainDefinitionMap.put(functionTreeModel.getChildrenUrl(), "perms["+functionTreeModel.getDescription()+"]");
//					}
//				}
//			}
//		}else{
//			logger.error("获取全部操作权限失败："+ JSONObject.toJSONString(collectionParam));
//		}

		filterChainDefinitionMap.put("/mapi/**", "stateAuthc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		shiroFilterFactoryBean.setLoginUrl("/api/account/nologin");
		shiroFilterFactoryBean.setUnauthorizedUrl("/mapi/account/unauth");
        
		return shiroFilterFactoryBean;
	}

	@Bean
	public MyShiroRealm myShiroRealm() {
		MyShiroRealm myShiroRealm = new MyShiroRealm();
		return myShiroRealm;
	}

	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(myShiroRealm());
		securityManager.setSubjectFactory(new StatelessDefaultSubjectFactory());
		securityManager.setSessionManager(sessionManager());

		return securityManager;
	}

	/**
	 * shiro session的管理
	 */
	@Bean
	public StatelessSessionManager sessionManager() {
		StatelessSessionManager sessionManager = new StatelessSessionManager();
		sessionManager.setSessionIdUrlRewritingEnabled(false);  //URL后面sessionId不显示
		sessionManager.setDeleteInvalidSessions(true); // 删除过期的session
		sessionManager.setGlobalSessionTimeout(RedisUtil.GLOBAL_SESSION_TIMEOUT);
		sessionManager.setSessionValidationInterval(RedisUtil.GLOBAL_SESSION_TIMEOUT);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setSessionDAO(redisSessionDAO());
		return sessionManager;
	}

	/**
	 * RedisSessionDAO shiro sessionDao层的实现 通过redis 使用的是shiro-redis开源插件
	 */
	@Bean
	public RedisSessionDAO redisSessionDAO() {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		return redisSessionDAO;
	}


	/**
	 * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * 配置LifecycleBeanPostProcessor生命周期管理器，会自动调用配置在IOC容器中的shiro bean的生命周期
	 * @return
	 */
	@Bean
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}
	/**
	 * 启动IOC容器中使用shiro的注解，但是必须配置在LifecycleBeanPostProcessor中才能使用
	 * @return
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
		DefaultAdvisorAutoProxyCreator creator=new DefaultAdvisorAutoProxyCreator();
		creator.setProxyTargetClass(true);
		return creator;
	}

	@Bean
	public StatelessAuthcFilter statelessAuthcFilter(){
		StatelessAuthcFilter statelessAuthcFilter = new StatelessAuthcFilter();
		return statelessAuthcFilter;
	}

}

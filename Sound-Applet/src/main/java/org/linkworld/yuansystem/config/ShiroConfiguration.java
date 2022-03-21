package org.linkworld.yuansystem.config;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/27
 */


import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class ShiroConfiguration {

 @Bean
 public StudentRealm getStudentRealm() {
  return new StudentRealm();
 }

 @Bean(name = "getManager")
 public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("getStudentRealm") StudentRealm studentRealm) {
  DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
  manager.setRealm(studentRealm);
  return manager;
 }

 @Bean
 public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getManager") DefaultWebSecurityManager manager) {
  ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
  bean.setSecurityManager(manager);
  // 登录拦截
  LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
/*  filterChainDefinitionMap.put("/applet/course/**","authc");
  filterChainDefinitionMap.put("/applet/search/**","authc");
  filterChainDefinitionMap.put("/applet/student/login","anon");
  filterChainDefinitionMap.put("/applet/student/register","anon");*/
  bean.setFilterChainDefinitionMap(filterChainDefinitionMap);

/*  // 授权
  filterChainDefinitionMap.put("/applet/course/**","perms[student]");*/

  return bean;

 }

 @Bean(name = "MD5CredentialsMatcher")
 public HashedCredentialsMatcher hashedCredentialsMatcher() {
  HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
  credentialsMatcher.setHashAlgorithmName("MD5");
  //credentialsMatcher.setHashIterations(1);
  return credentialsMatcher;
 }


}

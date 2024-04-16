/*
 * Copyright 2002-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.config.annotation.method.configuration;

import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;

import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.authorization.method.AuthorizationAdvisor;
import org.springframework.security.authorization.method.AuthorizationAdvisorProxyFactory;
import org.springframework.security.authorization.method.AuthorizeReturnObjectMethodInterceptor;
import org.springframework.security.config.Customizer;

@Configuration(proxyBeanMethods = false)
final class ReactiveAuthorizationProxyConfiguration implements AopInfrastructureBean {

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	static AuthorizationAdvisorProxyFactory authorizationProxyFactory(ObjectProvider<AuthorizationAdvisor> provider,
			ObjectProvider<Customizer<AuthorizationAdvisorProxyFactory>> customizers) {
		List<AuthorizationAdvisor> advisors = new ArrayList<>();
		provider.forEach(advisors::add);
		AuthorizationAdvisorProxyFactory factory = AuthorizationAdvisorProxyFactory.withReactiveDefaults();
		customizers.forEach((c) -> c.customize(factory));
		factory.setAdvisors(advisors);
		return factory;
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	static MethodInterceptor authorizeReturnObjectMethodInterceptor(ObjectProvider<AuthorizationAdvisor> provider,
			AuthorizationAdvisorProxyFactory authorizationProxyFactory) {
		AuthorizeReturnObjectMethodInterceptor interceptor = new AuthorizeReturnObjectMethodInterceptor(
				authorizationProxyFactory);
		List<AuthorizationAdvisor> advisors = new ArrayList<>();
		provider.forEach(advisors::add);
		advisors.add(interceptor);
		authorizationProxyFactory.setAdvisors(advisors);
		return interceptor;
	}

}

/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.beans.factory;

import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Interface to be implemented by beans that need to react once all their properties
 * have been set by a {@link BeanFactory}: e.g. to perform custom initialization,
 * or merely to check that all mandatory properties have been set.
 *
 * <p>An alternative to implementing {@code InitializingBean} is specifying a custom
 * init method, for example in an XML bean definition. For a list of all bean
 * lifecycle methods, see the {@link BeanFactory BeanFactory javadocs}.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @see DisposableBean
 * @see org.springframework.beans.factory.config.BeanDefinition#getPropertyValues()
 * @see org.springframework.beans.factory.support.AbstractBeanDefinition#getInitMethodName()
 *
 * 扩展点 自定义bean的初始化方式，可以在这里改变bean的属性
 *
 * 该方法在 BeanFactory 设置完了所有属性之后被调用
 * 该方法允许 bean 实例设置了所有 bean 属性时执行初始化工作，如果该过程出现了错误则需要抛出异常
 *
 * Spring 在完成实例化后，设置完所有属性，进行 “Aware 接口” 和 “BeanPostProcessor 前置处理”之后，
 * 会接着检测当前 bean 对象是否实现了 InitializingBean 接口，
 * 如果是，则会调用其 afterPropertiesSet() 进一步调整 bean 实例对象的状态。
 *
 * 检查并执行InitializingBean接口方法的时机
 * 	{@link AbstractAutowireCapableBeanFactory#invokeInitMethods(String, Object, RootBeanDefinition)}  }
 *
 * 注意，让业务对象实现这个接口并不好，显得代码具有侵入性，推荐通过init-method方式（可以替代InitializingBean）
 * default-init-method 可以全局指定所有bean的初始化方法
 *
 * 和init-method的联系
 * 1.afterPropertiesSet在init-method之前执行，如果 afterPropertiesSet() 中出现了异常，则 init-method 是不会执行的
 * 2.由于 init-method 采用的是反射执行的方式，所以 afterPropertiesSet() 的执行效率一般会高些
 * 3.推荐使用init-method，消除了代码对于Spring的依赖，可以通过 @PreDestory 代替xml
 */
public interface InitializingBean {

	/**
	 * Invoked by the containing {@code BeanFactory} after it has set all bean properties
	 * and satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
	 * <p>This method allows the bean instance to perform validation of its overall
	 * configuration and final initialization when all bean properties have been set.
	 * @throws Exception in the event of misconfiguration (such as failure to set an
	 * essential property) or if initialization fails for any other reason
	 */
	void afterPropertiesSet() throws Exception;

}

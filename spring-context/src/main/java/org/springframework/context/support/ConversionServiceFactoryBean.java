/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.context.support;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * A factory providing convenient access to a ConversionService configured with
 * converters appropriate for most environments. Set the
 * {@link #setConverters "converters"} property to supplement the default converters.
 *
 * <p>This implementation creates a {@link DefaultConversionService}.
 * Subclasses may override {@link #createConversionService()} in order to return
 * a {@link GenericConversionService} instance of their choosing.
 *
 * <p>Like all {@code FactoryBean} implementations, this class is suitable for
 * use when configuring a Spring application context using Spring {@code <beans>}
 * XML. When configuring the container with
 * {@link org.springframework.context.annotation.Configuration @Configuration}
 * classes, simply instantiate, configure and return the appropriate
 * {@code ConversionService} object from a {@link
 * org.springframework.context.annotation.Bean @Bean} method.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 3.0
 *
 * 可以用于注册自定义类型转换器
 */
public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {

	@Nullable
	private Set<?> converters;

	@Nullable
	private GenericConversionService conversionService;


	/**
	 * Configure the set of custom converter objects that should be added:
	 * implementing {@link org.springframework.core.convert.converter.Converter},
	 * {@link org.springframework.core.convert.converter.ConverterFactory},
	 * or {@link org.springframework.core.convert.converter.GenericConverter}.
	 */
	public void setConverters(Set<?> converters) {
		this.converters = converters;
	}

	/**
	 * 首先调用 createConversionService() 初始化 conversionService，
	 * 然后调用 ConversionServiceFactory.registerConverters() 将定义的 converters 注入到类型转换体系中。
	 * @see #createConversionService()   其实就是创建一个 DefaultConversionService 实例对象
	 * @see ConversionServiceFactory#registerConverters
	 * 	该方法是将定义的 converter 注册到目标 ConverterRegistry 中，我们知道 ConverterRegistry 是一个 Converter 注册器，他定义了一系列注册方法。
	 */
	@Override
	public void afterPropertiesSet() {
		this.conversionService = createConversionService();
		ConversionServiceFactory.registerConverters(this.converters, this.conversionService);
	}

	/**
	 * Create the ConversionService instance returned by this factory bean.
	 * <p>Creates a simple {@link GenericConversionService} instance by default.
	 * Subclasses may override to customize the ConversionService instance that
	 * gets created.
	 */
	protected GenericConversionService createConversionService() {
		return new DefaultConversionService();
	}


	// implementing FactoryBean

	@Override
	@Nullable
	public ConversionService getObject() {
		return this.conversionService;
	}

	@Override
	public Class<? extends ConversionService> getObjectType() {
		return GenericConversionService.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}

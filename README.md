# fluent-validator

使用请仔细阅读官方文档：[fluent-validator](http://neoremind.com/2016/02/java%E7%9A%84%E4%B8%9A%E5%8A%A1%E9%80%BB%E8%BE%91%E9%AA%8C%E8%AF%81%E6%A1%86%E6%9E%B6fluent-validator/)

## use

```xml
<dependency>
	<groupId>com.github.homeant</groupId>
	<artifactId>spring-boot-starter-fluent-validator</artifactId>
	<version>1.0.0-M1</version>
</dependency>
```

```java
//此处FluentValid注解
public User install(@FluentValid(UserValidator.class) User user){
	...
}
```

## spring bean

上述** @FluentValid(UserValidator.class) **交给spring进行管理
```
@Component
pubcli class UserValidator extends Validator{
	
}
```

## config

```yaml
validator:
  enable: true
```


## i18n

```java
@Bean
public IMessageService messageService() {
	return new IMessageService() {
		//jdbc or rpc
		@Override
		public List<MessageResource> getAllMessage(Object... args) {
			return null;
		}
	};
}
```

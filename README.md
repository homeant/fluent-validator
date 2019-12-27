# fluent-validator

> 针对hibernate-validator 进行增强，站在巨人的肩膀上，让程序员对数据校验更加得心应手


使用请仔细阅读官方文档：[fluent-validator](http://neoremind.com/2016/02/java%E7%9A%84%E4%B8%9A%E5%8A%A1%E9%80%BB%E8%BE%91%E9%AA%8C%E8%AF%81%E6%A1%86%E6%9E%B6fluent-validator/)

## use

```xml
<dependency>
	<groupId>com.github.homeant</groupId>
	<artifactId>spring-boot-starter-fluent-validator</artifactId>
	<version>1.0.0.M1</version>
</dependency>
```

```java
//此处FluentValid注解
public User install(@FluentValid(UserValidator.class) User user){
	...
}
```

## spring bean

上述** @FluentValid(UserValidator.class) **中的**UserValidator.class**交给spring进行管理
```
Component
pubcli class UserValidator extends Validator{
	
}
```

## config

```yaml
validator:
  enable: true ## default true
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

## use of controller 

当前版本如果对controller 进行处理需要自己定义**@RestControllerAdvice**,，异常类为**com.github.homeant.validator.exception.ValidateFailException**

```java
@RestControllerAdvice
public class ExceptionHandle {
	@ExceptionHandler(value = ValidateFailException.class)
    public ResponseEntity<Object> Handle(ValidateFailException exception){
		List<Map<String, String>> fields = new ArrayList<>();
		if(null!=exception.getErrors() && exception.getErrors().size()>0) {
			for (int i = 0; i < exception.getErrors().size(); i++) {
				ValidationError r = exception.getErrors().get(i);
				Map<String, String> field = new HashMap<>();
				field.put("field",r.getField());
				field.put("errorMsg",r.getErrorMsg());
				fields.add(field);
			}
		}
		map.put("message",exception.getMessage());
		map.put("state",416);
		map.put("fields", fields);
		return ResponseEntity.status(416).body(map);
    }
}
```

## use of service

在非controller中使用，请自行捕获异常，并处理
develop


## 版本计划

controller 层校验,将错误信息装载到**org.springframework.validation.BindingResult**中，和spring validator 使用方式统一


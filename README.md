# 寒枫的博客后端

**SpringBoot2+Maven+JDK18+Java8+JPA+PostgreSQL**，使用**IDEA**进行开发，**运行端口8090**

文档更新于2025年10月

[博客前端仓库](https://github.com/HanphoneJan/hanphone-blog-frontend)
### 项目结构

```  
├── com.example.blog2/
│   ├── dao/		  //数据访问层
│   ├── DTO/		  //数据传输对象,接口对象封装
│   ├── handle/		  //处理器
│   ├── interceptor/ //拦截器
│   ├── po/			 //实体类
│   ├── service/    //业务层
│   ├── util/    	//工具类	
│   ├── vo/			//视图对象
│   ├── web/     	//controller层
│   │   ├── admin/   //管理员controller
│   │   ├── ......
│   └── Blog2Application
```

### 开发

**IntelliJ IDEA 启动 Spring Boot 项目**自动生成完整命令启动项目

### 部署
使用maven执行package命令
启动命令示例

```bash
/www/server/java/jdk1.8.0_371/bin/java -jar  -Xmx1024M -Xms256M /xxx/xxx/blog2.jar  
```


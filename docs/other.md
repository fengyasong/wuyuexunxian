错误提示: 'packaging' with value 'jar' is invalid. Aggregator projects require 'pom' as packaging. @ line 3, column 110

问题背景:spring boot 聚合工程，多模块项目

解决：
    原来打包的是jar,但是需要 pom
    <!--    spring boot 聚合父工程中，打包类型要求设置为 pom-->
        <packaging>pom</packaging>
    
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.2.2.RELEASE</version>
            <relativePath/> <!-- lookup parent from repository -->
        </parent>
        <groupId>com.aicat</groupId>
        <artifactId>wuyuexunxian</artifactId>
        <version>1.0-SNAPSHOT</version>
        <packaging>pom</packaging>
    
        <modules>
            <module>seekfairy</module>
            <module>mybatis-plus</module>
            <module>kafka-demo</module>
            <module>influx-demo</module>
        </modules>
        
    mysql初始密码 G6pNw/XQ+brH
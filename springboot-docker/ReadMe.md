## Springboot中使用dockerfile构建镜像的四种方式

1、使用maven打包Springboot项目，在shell上，进行构建镜像
    docker build --build-arg=target/*.jar -t test/app .
    #构建完成后启动容器
    docker run -p 9090:9090 test/app

    给springboot启动时传递参数
        ARG JAR_FILE
        COPY ${JAR_FILE} app.jar
        ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar ${0} ${@}"]
    docker run -p 9090:9090 -e "JAVA_OPTS=-Ddebug -Xmx128m" app1 --server.port=9000

2、使用idea可视化管理docker，构建Springboot容器

3、使用Maven插件自动化构建Image
    https://github.com/spotify/dockerfile-maven
    简单介绍一下这个插件做啥用的
        1.通过pom配置docker构建Image过程，参数等
        2.封装了自动化build，push，run等Maven命令
        3.需要依赖Dockerfile，Dockerfile与pom.xml位于同一个目录下
        4.在pom.xml同目录下创建Dockerfile
    Dockerfile
        FROM openjdk:8-jre
        ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/myservice/app.jar"]
        ARG JAR_FILE
        ADD target/${JAR_FILE} /usr/share/myservice/app.jar
    pom
        <plugin>
             <groupId>com.spotify</groupId>
             <artifactId>dockerfile-maven-plugin</artifactId>
             <version>1.4.13</version>
             <executions>
                 <execution>
                     <id>default</id>
                     <goals>
                         <goal>build</goal>
                         <goal>push</goal>
                     </goals>
                 </execution>
             </executions>
             <configuration>
                 <!--你需要配置的地方-->

                 <!--指定仓库名/镜像名-->
                 <repository>myrep/${project.artifactId}</repository>
                 <!--指定tag -->
                 <tag>${project.version}</tag>
                 <buildArgs>
                     <!--指定参数jar-->
                     <JAR_FILE>${project.build.finalName}.jar</JAR_FILE>
                 </buildArgs>
             </configuration>
         </plugin>
    最后在Idea Maven插件Plugins点击docker:build即可。或者输入命令
        mvn com.spotify:dockerfile-maven-plugin:build
4、使用Google的Maven插件进行容器管理
    Google开源项目Jib，对比上面那个插件Jib的Start数为7.8k，dockerfile-maven 为2.4k。
    maven 插件
        <plugin>
            <groupId>com.google.cloud.tools</groupId>
            <artifactId>jib-maven-plugin</artifactId>
            <version>1.6.0</version>
            <configuration>
                <!--配置基本镜像-->
                <from>
                    <image>harbor.gc.com/test/openjdk:8u131-jre-alpine</image>
                    <auth>
                        <username>admin</username>
                        <password>Harbor12345</password>
                    </auth>
                </from>
                <!--配置最终推送的地址，仓库名，镜像名-->
                <to>
                    <image>harbor.gc.com/test/springboot-docker:${project.version}</image>
                    <auth>
                        <username>admin</username>
                        <password>Harbor12345</password>
                    </auth>
                </to>
                <!--<container>
                    <jvmFlags>
                        <jvmFlag></jvmFlag>
                    </jvmFlags>
                    <ports>
                        <port>8080</port>
                    </ports>
                </container>-->
                <allowInsecureRegistries>true</allowInsecureRegistries>
            </configuration>
            <!--绑定到maven lifecicle-->
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>build</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    配置maven docker hub账户和密码,在maven settings.xml中添加
        <server>
          <id>harbor.gc.com</id>
          <username>用户名</username>
          <password>密码</password>
        </server>
    在idea maven插件中点击或者maven命令 mvn compile jib:buildTar

    mvn clean package -DskipTests jib:build -DsendCredentialsOverHttp=true















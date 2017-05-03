V1.2.0-2017.1.16
1.JDK1.6+
2.Apache CXF版本由3.1.4更改为3.0.12，支持JDK1.6+
3.恢复mapper.xml存放位置在resource中
4.根据请求路径自动匹配jsp页面修改为请求路径中增加标记“v”以作判断，如需要打开页面/web/a/b/c.jsp,则请求路径为v/a/b/c;如果需要打开页面/web/a/b/index.jsp，则请求路径为v/a/b/c/

V1.1.0-2016.12.20
1.添加所有路径匹配，自动匹配到路径目录下的jsp页面,需要修改web.xml文件（增加default的servlet映射处理），参考demo项目
2.修改数据库语句xml文件存放路径，放在com.yo.friendis.模块.mapper.oracle/mysql目录下，需要修改application-context.xml中mapperLocations配置项
3.修正机构根ID根据数据库配置项取值
4.添加上传模块

V1.0.4-2016.11.1
1.根据用户获取，如果用户名为null，则返回null
2.修正密码修改时，判断新密码与确认密码是否一致问题
3.新增用户时，检查是否已存在
4.新增管理设置用户密码时，相同密码不需要修改提示功能
5.修改新建用户时，角色项为可选
6.修改控制层父类返回错误信息只返回错误代码,以及信息提示，隐藏报错信息。

V1.0.3-2016.8.10
1.spring data redis from 1.6.2.RELEASE to 1.7.2.RELEASE
2.spring from 4.1.6.RELEASE to 4.3.0.RELEASE

V1.0.2-2016.8.1
1.mybatis版本由3.4.1降级到3.3.1
2.mybatis-spring版本由1.3.0降级到1.2.2


V1.0.1-2016.7.20
1.通用Mapper插件版本由3.3.0升级到3.3.8,所有实体时间字段去掉@ColumnType(jdbcType = JdbcType.DATE) // 时间类型
2.mybatis版本由3.2.7升级到3.4.1
3.pagehelper版本由4.0.3升级到4.1.6
4.mybatis-spring版本由1.2.2升级到1.3.0
5.druid版本由1.0.17升级到1.0.23


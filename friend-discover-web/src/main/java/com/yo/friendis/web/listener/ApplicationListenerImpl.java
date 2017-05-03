/**
 * 
 */
package com.yo.friendis.web.listener;

import com.yo.friendis.core.hadoop.service.HadoopCfgConstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;

/**
 * Spring容器启动完成后，执行数据库表插入逻辑
 * 初始化登录表
 * @author yo
 * @date 2017-3-27
 */
@Repository
public class ApplicationListenerImpl implements ApplicationListener<ApplicationEvent> {
	@Autowired
	private HadoopCfgConstService haCfgConstService;
	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
//		if(dBService.getTableData("LoginUser")){
//			System.out.println(new java.util.Date()+"：登录表有数据，不需要初始化！");
//		}else{
//			dBService.insertLoginUser();
//			System.out.println(new java.util.Date()+"：初始化登录表完成！");
//		}
	}

}

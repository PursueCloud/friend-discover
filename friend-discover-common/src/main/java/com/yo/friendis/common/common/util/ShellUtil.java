package com.yo.friendis.common.common.util;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * shell脚本运行工具类
 * @author jay.huang
 *
 */
public class ShellUtil {
	private Connection conn;
    /** 远程机器IP */
    private String ip;
    /** 用户名 */
    private String osUsername;
    /** 密码 */
    private String password;
//    private String charset = Charset.defaultCharset().toString();

    private static final String charset = "UTF-8";
    private static final int TIME_OUT = 1000 * 5 * 60;
	public static Logger logger = LoggerFactory.getLogger(ShellUtil.class);
    /**
     * 构造函数
     * @param ip
     * @param username
     * @param password
     */
    public ShellUtil(String ip, String username, String password) {
        this.ip = ip;
        this.osUsername = username;
        this.password = password;
    }


    /**
    * 登录
    * @return
    * @throws IOException
    */
    private boolean login() throws IOException {
        conn = new Connection(ip);
        conn.connect();
        return conn.authenticateWithPassword(osUsername, password);
    }
    
    /**
     * @param is
     * @param charset
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
     private String processStream(InputStream is, String charset) throws Exception {
    	 BufferedInputStream bis = new BufferedInputStream(is);
    	 BufferedReader br = new BufferedReader(new InputStreamReader(bis, charset));
         byte[] buf = new byte[1024];
         StringBuilder sb = new StringBuilder();
         String lineStr = null;
         while ((lineStr = br.readLine()) != null) {
             sb.append(lineStr);
         }
         return sb.toString();
     }
     
    /**
    * 远程执行脚本
    * @param cmds 脚本路径及参数
    * @return
    * @throws Exception
    */
    public Map<String, String> execRemote(String cmds){
    	if(StringUtils.isBlank(cmds)) {
    		System.out.println("执行的命令不能为空！");
    		return null;
    	}
    	Map<String, String> resultMap = new HashMap<String, String>();
    	
        InputStream stdOut = null;
        InputStream stdErr = null;
        String[] cmdArray = cmds.split(" ");
        String result = "";
        String error = "";
        int ret = -1;
        try {
	        if (login()) {//登录成功
	    		System.out.println("登录远程机器“" + ip + "”成功");
	            // Open a new {@link Session} on this connection
	            Session session = conn.openSession();
	            // Execute a command on the remote machine.
	            session.execCommand(cmds);
	            
	    		System.out.println("加载shell脚本" + cmdArray[0] + "成功, 参数1：" + cmdArray[1] + "， 参数2：" + cmdArray[2]);
	            stdOut = new StreamGobbler(session.getStdout());
	            result = processStream(stdOut, charset);
	            
	            stdErr = new StreamGobbler(session.getStderr());
	            error = processStream(stdErr, charset);
	            
	            session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
	            ret = session.getExitStatus();
	            
	            System.out.println("shell脚步运行结果为：" + (StringUtils.isNotBlank(result) ? result + error : error));
	            if( ret > -1 ) {
		            resultMap.put("result", !"".equals(result) ? result + error : error);
	            } else {
	            	resultMap.put("result", error);
	            }
	        } else {//登录失败
	    		System.out.println("登录远程机器“" + ip + "”失败或执行脚步过程出错！");
	        	return resultMap;
	        } 
        } catch(Exception e) {
    		System.out.println("登录远程机器“" + ip + "”失败或执行脚步过程出错，错误信息如下：");
        	e.printStackTrace();
			resultMap.put("result", e.toString());
        	return resultMap;
        }finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return resultMap;
    }

	/**
	 * 远程执行命令
	 * @param cmds 命令名
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> execRemoteCmd(String cmds){
		if(StringUtils.isBlank(cmds)) {
			System.out.println("执行的命令不能为空！");
			return null;
		}
		Map<String, String> resultMap = new HashMap<String, String>();

		InputStream stdOut = null;
		InputStream stdErr = null;
		String[] cmdArray = cmds.split(" ");
		String result = "";
		String error = "";
		int ret = -1;
		try {
			if (login()) {//登录成功
				System.out.println("登录远程机器“" + ip + "”成功");
				// Open a new {@link Session} on this connection
				Session session = conn.openSession();
				// Execute a command on the remote machine.
				session.execCommand(cmds);

				System.out.println("正在执行远程命令：" + cmds);
				stdOut = new StreamGobbler(session.getStdout());
				result = processStream(stdOut, charset);

				stdErr = new StreamGobbler(session.getStderr());
				error = processStream(stdErr, charset);

				session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
				ret = session.getExitStatus();

				logger.debug("debug------远程命令执行结果为：" + (StringUtils.isNotBlank(result) ? result + error : error));
				if( ret > -1) {
					resultMap.put("success", "true");
					resultMap.put("result", !"".equals(result) ? result + error : error);
				} else {
					resultMap.put("success", "false");
					resultMap.put("result", error);
				}
			} else {//登录失败
				logger.error("登录远程机器“" + ip + "”失败或执行命令" + cmds + "过程出错！");
				return resultMap;
			}
		} catch(Exception e) {
//			System.out.println("登录远程机器“" + ip + "”失败或执行脚步过程出错，错误信息如下：");
			logger.error(e.getMessage(), e);
			resultMap.put("result", e.toString());
			return resultMap;
		}finally {
			if (conn != null) {
				conn.close();
			}
			IOUtils.closeQuietly(stdOut);
			IOUtils.closeQuietly(stdErr);
		}
		return resultMap;
	}
    /**
     * 远程执行脚本
     * @param cmds 脚本路径及参数
     * @return
     * @throws Exception
     */
     public Map<String, String> execRemote(String cmds, boolean isSCOptimize){
     	if(StringUtils.isBlank(cmds)) {
     		System.out.println("执行的命令不能为空！");
     		return null;
     	}
     	Map<String, String> resultMap = new HashMap<String, String>();
     	
         InputStream stdOut = null;
         InputStream stdErr = null;
         String[] cmdArray = cmds.split(" ");
         String result = "";
         String error = "";
         int ret = -1;
         try {
 	        if (login()) {//登录成功
 	    		System.out.println("登录远程机器“" + ip + "”成功");
 	            // Open a new {@link Session} on this connection
 	            Session session = conn.openSession();
 	            // Execute a command on the remote machine.
 	            session.execCommand(cmds);
 	            
 	    		System.out.println("加载shell脚本" + cmdArray[0] + "成功, 参数：" + cmds);
 	            stdOut = new StreamGobbler(session.getStdout());
 	            result = processStream(stdOut, charset);
 	            
 	            stdErr = new StreamGobbler(session.getStderr());
 	            error = processStream(stdErr, charset);
 	            
 	            session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
 	            ret = session.getExitStatus();
 	            
 	            logger.info("shell脚步运行结果为：" + result);
 	            if( ret > -1) {
 		            String[] resultArray = result.split(",");//如果返回的结果有多个（以逗号分隔（英文逗号）
 		        	result = resultArray[0];
 		        	if(resultArray.length >= 2) {
 		        		String optimizeBatch = resultArray[1];
 		            	if(!isSCOptimize) {//不是拆分组合关键字整合优化
 				    	 		resultMap.put("optimizeBatch", optimizeBatch);
 		            	} else {//是拆分组合关键字整合优化
 				            	String optimizeId = resultArray[1];
 				    	 		resultMap.put("optimizeId", optimizeId);
 		            	}
 		        	}
 		        	
 		        	resultMap.put("result", !"".equals(result) ? result : error);
 	            } else {
 	            	resultMap.put("result", error);
 	            }
 	        } else {//登录失败
 	    		logger.error("登录远程机器“" + ip + "”失败或执行命令" + cmds + "过程出错！");
 	        	return resultMap;
 	        } 
         } catch(Exception e) {
//     		System.out.println("登录远程机器“" + ip + "”失败或执行脚步过程出错，错误信息如下：");
			 logger.error(e.getMessage(), e);
 			resultMap.put("result", e.toString());
         	return resultMap;
         }finally {
             if (conn != null) {
                 conn.close();
             }
             IOUtils.closeQuietly(stdOut);
             IOUtils.closeQuietly(stdErr);
         }
         return resultMap;
     }
	/**
	 * 重载的方法，传入1个参数，运行指定路径的shell脚本，，返回一个Map,含有一个key值“result”success或者fail(如果出现IO异常，仍然返回输出结果，将输出结果放到map中)
	 * @param shellFileUrl shell文件路径
	 * @param batchId 批次ID
	 * @return
	 */
	public static Map<String, String> execLocalShell(String shellFileUrl,String batchId){
		if(StringUtils.isBlank(shellFileUrl) || StringUtils.isBlank(batchId)) {
			return null;
		}
 		Map<String, String> resultMap = new HashMap<String, String>();
 		
	   // 定义传入shell脚本的参数，将参数放入字符串数组里  
        String cmds[] = new String[3];  
        cmds[0] = shellFileUrl;//"/home/test-shell/return.sh";  
        cmds[1] = batchId; // 参数1
        
		try {
	        // 执行shell脚本  
	        Process pcs = Runtime.getRuntime().exec(cmds);
	        System.out.println("加载shell脚本成功！脚本名：" + shellFileUrl + ", 参数1：" + batchId);
	        // 定义shell返回值  
	        String result = "";  
	  
	        // 获取shell返回流  
	        BufferedInputStream in = new BufferedInputStream(pcs.getInputStream());  
	        // 字符流转换字节流  
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));  
	        // 这里也可以输出文本日志  
	  
	        String lineStr = "";  
	        while ((lineStr = br.readLine()) != null) {  
	        	if(StringUtils.isNotBlank(lineStr)) {
	        		result += lineStr;  
	        	} 
	        }  
	        // 关闭输入流  
	        br.close();  
	        in.close();  

            System.out.println("shell脚步运行结果为：" + result);
	        //System.out.print(shellFile + "--" + param1 + "--" + param2);  
	 		resultMap.put("result", result);
	 		return resultMap;
		} catch (IOException e) {
			System.out.println("读取shell文件失败，failed to read shell file");
			e.printStackTrace();//如果出现异常，仍然返回输出结果，将输出结果放到map中
			resultMap.put("result", e.toString());
			return resultMap;
		} 
	}
	
	/**
	 * 重载的方法，传入2个参数，运行指定路径的shell脚本，，返回一个Map,含有一个key值“result”success或者fail(如果出现IO异常，仍然返回输出结果，将输出结果放到map中)
	 * @param shellFileUrl shell文件路径
	 * @param param1 参数1
	 * @param param2 参数2
	 * @return
	 */
	public static Map<String, String> execLocalShell(String shellFileUrl,String param1,String param2){
		if(StringUtils.isBlank(shellFileUrl) || StringUtils.isBlank(param1) || StringUtils.isBlank(param2)) {
			return null;
		}
 		Map<String, String> resultMap = new HashMap<String, String>();

	   // 定义传入shell脚本的参数，将参数放入字符串数组里  
        String cmds[] = new String[3];  
        cmds[0] = shellFileUrl;//"/home/test-shell/return.sh";  
        cmds[1] = param1; // 参数1
        cmds[2] = param2;// 参数2
        
		try {
	        // 执行shell脚本  
	        Process pcs = Runtime.getRuntime().exec(cmds);
	        System.out.println("加载shell脚本成功！脚本名：" + shellFileUrl + ", 参数1：" + param1 + ", 参数2：" + param2);
	        // 定义shell返回值  
	        String result = "";  
	  
	        // 获取shell返回流  
	        BufferedInputStream in = new BufferedInputStream(pcs.getInputStream());  
	        // 字符流转换字节流  
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));  
	        // 这里也可以输出文本日志  
	  
	        String lineStr = "";  
	        while ((lineStr = br.readLine()) != null) {  
	        	if(StringUtils.isNotBlank(lineStr)) {
	        		result += lineStr;  
	        	}
	        }  
	        // 关闭输入流  
	        br.close();  
	        in.close();  

            System.out.println("shell脚步运行结果为：" + result);
	        //System.out.print(shellFile + "--" + param1 + "--" + param2);  
	 		resultMap.put("result", result);
	 		return resultMap;
		} catch (IOException e) {
			System.out.println("读取shell文件失败，failed to read shell file");
			e.printStackTrace();//如果出现异常，仍然返回输出结果，将输出结果放到map中
			resultMap.put("result", e.toString());
			return resultMap;
		} 
	}
	/**
	 * 重载的方法，传入2个参数，运行指定路径的shell脚本，，返回一个Map,含有一个key值“result”success或者fail(如果出现IO异常，仍然返回输出结果，将输出结果放到map中)
	 * @param shellFileUrl shell文件路径
	 * @param param1 参数1
	 * @param param2 参数2
	 * @return
	 */
	public static Map<String, String> execLocalShell(String shellFileUrl,String param1,String param2, boolean isSCOptimize){
		if(StringUtils.isBlank(shellFileUrl) || StringUtils.isBlank(param1) || StringUtils.isBlank(param2)) {
			return null;
		}
 		Map<String, String> resultMap = new HashMap<String, String>();

	   // 定义传入shell脚本的参数，将参数放入字符串数组里  
        String cmds[] = new String[3];  
        cmds[0] = shellFileUrl;//"/home/test-shell/return.sh";  
        cmds[1] = param1; // 参数1
        cmds[2] = param2;// 参数2
        
		try {
	        // 执行shell脚本  
	        Process pcs = Runtime.getRuntime().exec(cmds);
	        System.out.println("加载shell脚本成功！脚本名：" + shellFileUrl + ", 参数1：" + param1 + ", 参数2：" + param2);
	        // 定义shell返回值  
	        String result = "";  
	  
	        // 获取shell返回流  
	        BufferedInputStream in = new BufferedInputStream(pcs.getInputStream());  
	        // 字符流转换字节流  
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));  
	        // 这里也可以输出文本日志  
	  
	        String lineStr = "";  
	        while ((lineStr = br.readLine()) != null) {  
	        	if(StringUtils.isNotBlank(lineStr)) {
	        		result += lineStr;  
	        	}
	        }  
	        // 关闭输入流  
	        br.close();  
	        in.close();  

            System.out.println("shell脚步运行结果为：" + result);
	        //System.out.print(shellFile + "--" + param1 + "--" + param2);  
        	String[] resultArray = result.split(",");//如果返回的结果有多个（以逗号分隔（英文逗号）
        	if(resultArray.length >= 2) {
        		result = resultArray[0];
        		String optimizeBatch = resultArray[1];
            	if(!isSCOptimize) {//不是拆分组合关键字整合优化
		    	 	resultMap.put("optimizeBatch", optimizeBatch);
            	} else {//是拆分组合关键字整合优化
		            String optimizeId = resultArray[1];
		    	 	resultMap.put("optimizeId", optimizeId);
            	}
        	}
	 		resultMap.put("result", result);
	 		return resultMap;
		} catch (IOException e) {
			System.out.println("读取shell文件失败，failed to read shell file");
			e.printStackTrace();//如果出现异常，仍然返回输出结果，将输出结果放到map中
			resultMap.put("result", e.toString());
			return resultMap;
		} 
	}
	
	/**
	 * 重载的方法，传入2个参数，运行指定路径的shell脚本，，返回一个Map,含有一个key值“result”success或者fail(如果出现IO异常，仍然返回输出结果，将输出结果放到map中)
	 * @param shellFileUrl shell文件路径
	 * @param param1 参数1
	 * @param param2 参数2
	 * @param param3 参数3
	 * @return
	 */
	public static Map<String, String> execLocalShell(String shellFileUrl,String param1,String param2, Double param3, boolean isSCOptimize){
		if(StringUtils.isBlank(shellFileUrl) || StringUtils.isBlank(param1) || StringUtils.isBlank(param2)) {
			return null;
		}
 		Map<String, String> resultMap = new HashMap<String, String>();

		if(param3 == null) {
			param3 = 0.5;
		} else if(param3 < 0.0) {
			param3 = 0.0;
		} else if(param3 > 1.0) {
			param3 = 1.0;
		}
	   // 定义传入shell脚本的参数，将参数放入字符串数组里  
        String cmds[] = new String[4];  
        cmds[0] = shellFileUrl;//"/home/test-shell/return.sh";  
        cmds[1] = param1; // 参数1
        cmds[2] = param2;// 参数2
        cmds[3] = param3.toString();// 参数2
        
		try {
	        // 执行shell脚本  
	        Process pcs = Runtime.getRuntime().exec(cmds);
	        System.out.println("加载shell脚本成功！脚本名：" + shellFileUrl + ", 参数1：" + param1 + ", 参数2：" + param2 + ", 参数3：" + param3);
	        // 定义shell返回值  
	        String result = "";  
	  
	        // 获取shell返回流  
	        BufferedInputStream in = new BufferedInputStream(pcs.getInputStream());  
	        // 字符流转换字节流  
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));  
	        // 这里也可以输出文本日志  
	  
	        String lineStr = "";  
	        while ((lineStr = br.readLine()) != null) {  
	        	if(StringUtils.isNotBlank(lineStr)) {
	        		result += lineStr;  
	        	}
	        }  
	        // 关闭输入流  
	        br.close();  
	        in.close();  

            System.out.println("shell脚步运行结果为：" + result);
	        //System.out.print(shellFile + "--" + param1 + "--" + param2);  
        	String[] resultArray = result.split(",");//如果返回的结果有多个（以逗号分隔（英文逗号）
        	if(resultArray.length >= 2) {
        		result = resultArray[0];
        		String optimizeBatch = resultArray[1];
            	if(!isSCOptimize) {//不是拆分组合关键字整合优化
		    	 	resultMap.put("optimizeBatch", optimizeBatch);
            	} else {//是拆分组合关键字整合优化
		            String optimizeId = resultArray[1];
		    	 	resultMap.put("optimizeId", optimizeId);
            	}
        	}
	 		resultMap.put("result", result);
	 		return resultMap;
		} catch (IOException e) {
			System.out.println("读取shell文件失败，failed to read shell file");
			e.printStackTrace();//如果出现异常，仍然返回输出结果，将输出结果放到map中
			resultMap.put("result", e.toString());
			return resultMap;
		} 
	}
	
	/**
	 * 重载的方法，传入一个字符串数据作为参数，并运行指定路径下的shell脚本，返回一个Map,含有一个key值“result”success或者fail(如果出现IO异常，仍然返回输出结果，将输出结果放到map中)
	 * @param shellFileUrl 要执行的shell文件路径
	 * @param params 参数数组
	 * @return
	 */
	public static Map<String, String> execLocalShell(String shellFileUrl,String[] params){
		if(StringUtils.isBlank(shellFileUrl) || params == null || params.length == 0) {
			return null;
		}
 		Map<String, String> resultMap = new HashMap<String, String>();

	   // 定义传入shell脚本的参数，将参数放入字符串数组里  
        String cmds[] = new String[params.length + 1];  
        cmds[0] = shellFileUrl;//"/home/test-shell/return.sh";  
        //构造参数数组
        for(int i=0; i<params.length; i++) {
        	cmds[i+1] = params[i];
        }
        
		try {
	        // 执行shell脚本  
	        Process pcs = Runtime.getRuntime().exec(cmds);
	        System.out.println("加载shell脚本成功！脚本名：" + shellFileUrl + ", 参数1：" + params[0] + ", 参数2：" + params[1]);
	        // 定义shell返回值  
	        String result = "";  
	  
	        // 获取shell返回流  
	        BufferedInputStream in = new BufferedInputStream(pcs.getInputStream());  
	        // 字符流转换字节流  
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));  
	        // 这里也可以输出文本日志  
	  
	        String lineStr = "";  
	        while ((lineStr = br.readLine()) != null) {  
	        	if(StringUtils.isNotBlank(lineStr)) {
	        		result += lineStr;  
	        	}  
	        }  
	        // 关闭输入流  
	        br.close();  
	        in.close();  

            System.out.println("shell脚步运行结果为：" + result);
	        //System.out.print(shellFile + "--" + param1 + "--" + param2);  
	 		resultMap.put("result", result);
	 		return resultMap;
		} catch (IOException e) {
			System.out.println("读取shell文件失败，failed to read shell file");
			e.printStackTrace();//如果出现异常，仍然返回输出结果，将输出结果放到map中
			resultMap.put("result", e.toString());
			return resultMap;
		} 
	}
	
	/**
	 * 远程执行服务器上面的shell脚本
	 * @param ip 远程服务器IP地址
	 * @param username 用户名
	 * @param password 密码
	 * @param shellFileUrl 要执行的shell脚本路径
	 * @param args shell脚本要传的参数
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> execRemoteShell(String ip, String username, String password, 
			String shellFileUrl, String args){
		ShellUtil executor = new ShellUtil(ip, username, password);
		return executor.execRemote(shellFileUrl + " " + args);
	}
	
	/**
	 * 远程执行服务器上面的shell脚本
	 * @param ip 远程服务器IP地址
	 * @param username 用户名
	 * @param password 密码
	 * @param shellFileUrl 要执行的shell脚本路径
	 * @param args shell脚本要传的参数
	 * @param isSCOptimize 是否拆分关键字组整合优化（是的话返回值含optimizeId，否则返回值含optimizeBatch
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> execRemoteShell(String ip, String username, String password, 
			String shellFileUrl, String args, boolean isSCOptimize){
		ShellUtil executor = new ShellUtil(ip, username, password);
		return executor.execRemote(shellFileUrl + " " + args, isSCOptimize);
	}

	/**
	 * @param ip 远程服务器IP地址
	 * @param username 用户名
	 * @param password 密码
	 * @param cmds 要执行的命令
     * @return
     */
	public static Map<String, String> execRemoteCmd(String ip, String username, String password, String cmds){
		ShellUtil executor = new ShellUtil(ip, username, password);
		return executor.execRemoteCmd(cmds);
	}
}

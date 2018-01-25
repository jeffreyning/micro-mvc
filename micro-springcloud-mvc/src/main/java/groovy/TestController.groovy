package groovy;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.nh.micro.rule.engine.core.plugin.MicroAop;
import com.nh.micro.rule.engine.core.plugin.MicroDefaultLogProxy;
import com.nh.micro.service.InjectGroovy;
import com.nh.micro.template.MicroDbProxy;
import com.nh.micro.template.MicroTMProxy;

import foo.service.TestService;

@MicroAop(name=[MicroDefaultLogProxy.class,MicroTMProxy.class,MicroDbProxy.class], property=["","",""])
class TestController  {  
	@Resource
	public TestService testService;
	

	public Map echo(String str,HttpServletRequest httpRequest) {
		System.out.println("this is controller proxy");
		testService.test("111");
		Map retMap=new HashMap();
		retMap.put("status", "0");
		return retMap;
	}

}

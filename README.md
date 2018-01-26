micro-mvc��ܣ�������springmvc��springcloud����,ʹ���е�controller��servicebean��dao��sqlҵ���߼����붼֧���Ȳ��𷽱㿪����Ա��ʽ����������


**��springmvc���Ϲ���**

**��дController�ӿ�**

���Ϻ�Springmvc��controllerֻ��д�ӿڣ��������Ʊ�����RequestParamע�⡣
ʹ��InjectGroovyע���ڽӿ���������Ӧ��groovyʵ�����ơ�
�����봫ͳspringmvc��controller���졣


```
package foo.web;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.nh.micro.service.InjectGroovy;

@Controller
@RequestMapping("test")
@InjectGroovy(name="TestController")
public interface TestController {
@RequestMapping("echo")
@ResponseBody
public  Map echo(@RequestParam(value="str") String str,HttpServletRequest httpRequest);

}
```


**��дController���ʵ��groovy**


```
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
```


**����controller���ɨ��**

ʹ��GroovyBeanScannerConfigurer����context:component-scan��controller����ɨ�衣


```
	<bean class="com.nh.micro.service.GroovyBeanScannerConfigurer">
		<property name="scanPath" value="foo.web"></property>
	</bean>
<!-- <context:component-scan base-package="foo.web" /> -->
```

**Service��**

**��дServiceBean�ӿ�**

�����а�ɨ�裬ʹcontroller���ܹ���resource���ص�ServiceBean�������ע���ڽӿ���ʹ��@InjectGroovyע��������Ӧ��ʵ��ҵ���߼���groovy�ļ�����
��дServiceBean�ӿ�

```
package foo.service;
import com.nh.micro.service.InjectGroovy;
@InjectGroovy(name="TestService")
public interface TestService {
	public void test(String id);
}
```

**Service��groovyʵ��**

```
package groovy.service;
import javax.annotation.Resource;
import foo.dao.TestDao;
import foo.dto.MicroTestDto;
import foo.repository.*;
import groovy.json.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import com.nh.micro.dao.mapper.DefaultPageInfo;
import com.nh.micro.dao.mapper.InjectDao;
import com.nh.micro.rule.engine.core.plugin.MicroAop;
import com.nh.micro.rule.engine.core.plugin.MicroDefaultLogProxy;
import com.nh.micro.template.MicroDbProxy;
import com.nh.micro.template.MicroServiceTemplateSupport;
import com.nh.micro.template.MicroTMProxy;

@MicroAop(name=[MicroDefaultLogProxy.class,MicroTMProxy.class,MicroDbProxy.class], property=["","",""])
class TestService  {  
	
	@Resource
	public TestDao testDao;

	public void test(String id){
		
		Map paramMap=new HashMap();
		paramMap.put("id", id);
		MicroTestDto microTestDto=testDao.queryInfoById(paramMap);

		List<MicroTestDto> list=testDao.getInfoListAllMapper(microTestDto, ""); 
		
		DefaultPageInfo pageInfo=new DefaultPageInfo();
		pageInfo.setPageNo(1);
		List<MicroTestDto> retList=testDao.queryInfosByPage(paramMap, pageInfo);
		Long total=pageInfo.getTotal();
		System.out.println("total="+total);

	}

}
```

**Service���ɨ������**

```
	<bean class="com.nh.micro.service.GroovyBeanScannerConfigurer">
		<property name="scanPath" value="foo.service"></property>
	</bean>
```

**Dao��**

ʹ��micro-daoģ�飬��Ҫ��дdto�࣬dao�ӿں�sql�ļ���ͨ��ɨ��sql�ļ���dao�ӿڣ�ʹservice���ܹ�ʹ��Resourceע�����dao�������

**��дdto������**

ʹ��@MicroTableNameӳ�������б�����ʹ��@MicroMappingAnnoӳ�����������

```
package foo.dto;

import java.util.Date;
import com.nh.micro.orm.MicroMappingAnno;
import com.nh.micro.orm.MicroTableName;

@MicroTableName(name="micro_test")
public class MicroTestDto {
	@MicroMappingAnno(name="id")
	private String id;
	@MicroMappingAnno(name="meta_key")
	private String metaKey;
	@MicroMappingAnno(name="meta_name")
	private String metaName;
	@MicroMappingAnno(name="meta_type")
	private String metaType;
	@MicroMappingAnno(name="create_time")
	private Date createTime;
	
	@MicroMappingAnno(name="meta_num")
	private Integer metaNum;

	public Integer getMetaNum() {
		return metaNum;
	}

	public void setMetaNum(Integer metaNum) {
		this.metaNum = metaNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMetaKey() {
		return metaKey;
	}

	public void setMetaKey(String metaKey) {
		this.metaKey = metaKey;
	}

	public String getMetaName() {
		return metaName;
	}

	public void setMetaName(String metaName) {
		this.metaName = metaName;
	}

	public String getMetaType() {
		return metaType;
	}

	public void setMetaType(String metaType) {
		this.metaType = metaType;
	}
}
```

**��дDao�ӿ�**

```
package foo.dao;
import java.util.List;
import java.util.Map;
import com.nh.micro.dao.mapper.ListInnerClass;
import com.nh.micro.dao.mapper.MicroCommonMapper;
import com.nh.micro.dao.mapper.MicroPageInfo;
import com.nh.micro.orm.MicroDbName;
import foo.dto.MicroTestDto;

@MicroDbName
public interface TestDao extends MicroCommonMapper<MicroTestDto> {
		public int updateInfo(Map paramMap);
		public int insertInfo(Map paramMap);
		@ListInnerClass(name=MicroTestDto.class)
	public List<MicroTestDto> queryInfosByPage(Map paramMap,MicroPageInfo pageInfo);	
	public MicroTestDto queryInfoById(Map paramMap);	
}
```

**Dao���ɨ������**

```
	<bean id="daoScan" class="com.nh.micro.dao.mapper.scan.BeanScannerConfigurer">
		<property name="scanPath" value="foo.dao"></property>
	</bean>
```

**��дsqlģ��**

����jsp���﷨��дsql��֧��#��$�����滻������������select����update��ͳһ��<sql id=��xxx��>��ǩ��id��dao�ӿڷ�����һ�£�sql�ļ�����dao�ӿ���һ�¡�
```
<%! <sql id="queryInfoById"> %>
select * from micro_test where 1=1 
<% if(paramArray[0].get("id")!=null){ %>		
	and id = #{paramArray[0].id} 			
<% 	} %>
<%! </sql> %>

<%! <sql id="queryInfosByPage"> %>
select * from micro_test 
<%! </sql> %>

<%! <sql id="insertInfo"> %>
insert into micro_test(id,meta_key) values( 
?			
<% repList.add(paramArray[0].get("id"));%>
,?		
<% repList.add(paramArray[0].get("meta_key"));%>	
	)
<%! </sql> %>
 
<%! <sql id="updateInfo"> %>
update micro_test set  
			
<% if(paramArray[0].get("meta_key")!=null){%>
,meta_key=#{paramArray[0].get("meta_key")}	
<% } %>	
<% if(paramArray[0].get("meta_name")!=null){%>
,meta_name=#{paramArray[0].get("meta_name")}	
<% } %>	

where id=#{paramArray[0].get("id")}
<%! </sql> %>
```

**Sql�ļ�ɨ������**

```
	<!-- micro-dao sql�ļ����� -->
	<bean class="com.nh.micro.nhs.NhsInitUtil"
		init-method="initGroovyAndThread" lazy-init="false">
		<property name="fileList">
			<list>
				<bean class="com.nh.micro.rule.engine.core.GFileBean">
					<property name="ruleStamp" value="true"></property>
					<property name="jarFileFlag" value="true"></property>
					<property name="dirFlag" value="true"></property>
					<property name="rulePath" value="/groovy/"></property>
				</bean>
			</list>
		</property>
	</bean>
```

**MicroDao˵��**

ͬʱ֧��mysql��oracle
MicroDao���mybatis���ŵ㣺
1��sql�ű�֧���޸ĺ��Ȳ���ʵʱ��Ч��
2��bean�����ݿ��ֶ�ӳ���ϵ��ͨ��ע�����õ�bean�У�������sql�ű������֡�
3��sql�ű�֧������jsp��д�����Ҳ�������select��updateʹ�ò�ͬ��ǩ��������
4������Ҫʹ�ò��������֧�������ҳ��
5������Ҫʹ�ò��������֧�����bean�ı�׼��ɾ�Ĳ鹦�ܡ�
6������Ҫʹ�ò��������֧�ֶ�д���룬�ֿ�ֱ�
7�����mysql5.7֧�ֶ�̬�ֶΡ�
֧��mapper��template����orm����ģʽ֧��ҵ��ϵͳ
1��mapperָ��ͨ��ɨ��ӿڣ�����ʱ�Զ�����daoʵ����
2��templateָ��ͨ���̳�template��׼���࣬����dao���ࣻ
3����ormָ��ֱ��ʹ��microDaoʵ��������ִ�з�orm���������ݿ������

**��������**

����serviceBean�ӿڶ���ͨ����ɨ�跢��Ϊspringbean�������Կ���ʹ��ԭ�е�spring������ơ�

```
    <!-- ����platform transaction manager-->
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean> 

    <!-- ����ʽ������������������advice-->
     <tx:advice id="txAdvice" transaction-manager="txManager">
        <tx:attributes>

            <tx:method name="get*" read-only="true"/>
<tx:method name="test*" propagation="REQUIRED"/>
            <tx:method name="add*" propagation="REQUIRED"/>
            <tx:method name="insert*" propagation="REQUIRED"/>
            <tx:method name="update*" propagation="REQUIRED"/>
            <tx:method name="*"/>
        </tx:attributes>
    </tx:advice> 

    <!-- �����������advice���÷�Χ����������-->
    <aop:config>
        <aop:pointcut id="serviceLayerTransaction" expression="execution( * foo.service.*..*(..))"/>
        <aop:advisor pointcut-ref="serviceLayerTransaction" advice-ref="txAdvice"/>
    </aop:config>
```

**SpringCloud����**

SpringCloud����controller��service��dao����springmvc���Ͼ�һ�¡�
����鿴demo��Ŀ
Micro-springcloud-mvc


**Nhmicro��Aop����**

����groovyʱ�ṩaop������ƣ�Ĭ���ṩ����aop������Դ�л�aop
����aop�����ڼ���ʱ��ʶ��Transactionalע�⣬ʵ��������ơ�
�����б�д�ض����ܴ���
����������Ҫ��groovy����������MicroAopע��ָ��������

```
@MicroAop(name=[MicroDefaultLogProxy.class,MicroTMProxy.class,MicroDbProxy.class], property=["","",""])
```
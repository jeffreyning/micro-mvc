package foo.service;

import com.nh.micro.service.InjectGroovy;

/**
 * 
 * @author ninghao
 *
 */
@InjectGroovy(name="TestService")
public interface TestService {
	public void test(String id);
}

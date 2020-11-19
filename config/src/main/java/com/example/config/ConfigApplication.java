package com.example.config;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author csl
 */
@SpringBootApplication
@RestController
@RefreshScope//支持动态更新配置
public class ConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
	}

		@Value("${user.name}")
		private String title;

		@GetMapping("/test")
		public String hello() {
			return title;
		}


		@NacosInjected // 使用nacos client的NacosInjected注解注入服务
		private NamingService namingService;


		@GetMapping("/set")
		public String set(String serviceName) {
			try {
				namingService.registerInstance(serviceName, "127.0.0.1", 8848); // 注册中心的地址
				return "OK";
			} catch (NacosException e) {
				e.printStackTrace();

				return "ERROR";
			}
		}

		@GetMapping("/get")
		public List<Instance> get(String serviceName) throws NacosException {
			return namingService.getAllInstances(serviceName);

	}
}

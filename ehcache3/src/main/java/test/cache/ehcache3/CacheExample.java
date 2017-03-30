package test.cache.ehcache3;

import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.slf4j.LoggerFactory.getLogger;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

/**
 * Hello world!
 *
 */
public class CacheExample {

	private static final Logger LOGGER = getLogger(CacheExample.class);

	public static void main(String[] args) throws Exception {
		LOGGER.info("Creating cache manager via XML resource");
		CacheExample basicCache = new CacheExample();

		// basicCache.simpleCacheCode();
		// basicCache.simpleConfigCache();
		 //basicCache.annotationCache();
		//basicCache.annotationCacheRemove();
		 basicCache.annotationCacheMultiple();

	}

	void simpleCacheCode() {
		System.out.println("Runing Simple Cache Code....");
		CachingProvider provider = Caching.getCachingProvider();
		CacheManager cacheManager = provider.getCacheManager();
		MutableConfiguration<Long, String> configuration = new MutableConfiguration<Long, String>()
				.setTypes(Long.class, String.class).setStoreByValue(false)
				.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));
		Cache<Long, String> cache = cacheManager.createCache("jCache", configuration);
		cache.put(1L, "one");
		String value = cache.get(1L);
		System.out.println("From cache...." + value);

	}

	void simpleConfigCache() throws Exception {
		System.out.println("Runing Simple Configuration(XML) based Cache...");
		CachingProvider provider = Caching.getCachingProvider();
		CacheManager manager = provider.getCacheManager(getClass().getResource("/ehcache-simple.xml").toURI(),
				getClass().getClassLoader());
		Cache<Long, String> readyCache = manager.getCache("jCache1", Long.class, String.class);
		readyCache.put(1L, "one");
		String value = readyCache.get(1L);
		System.out.println("From cache...." + value);

	}

	void annotationCache() throws Exception {
		System.out.println("Running Simple Annotation based (Spring + XML) based cache...");

		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring-cache-config.xml");
		RefData service = (RefData) context.getBean("refDataService");
		long start = System.currentTimeMillis();
		String cacheData = service.getMyData();
		long end = System.currentTimeMillis();
		System.out.println("Time taken for first execution...." + (end - start) + ":" + cacheData);
		
		for (int i = 0; i < 50; i++) {
			start = System.currentTimeMillis();
			cacheData = service.getMyData();
			end = System.currentTimeMillis();
			System.out.println("Time taken : " + (end - start) + ", for itr :" +i+", Data:"+ cacheData);
			Thread.sleep(50);
		}

	}

	void annotationCache2() throws Exception {
		System.out.println("Running Simple Annotation based (Spring + XML) based cache...");

		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring-cache-config.xml");
		RefData service = (RefData) context.getBean("refDataService");
		long start = System.currentTimeMillis();
		String cacheData = service.getMyData("a");
		long end = System.currentTimeMillis();
		System.out.println("Time taken for first execution...." + (end - start) + ":" + cacheData);

		start = System.currentTimeMillis();
		cacheData = service.getMyData("a");
		end = System.currentTimeMillis();
		System.out.println("Time taken : " + (end - start) + ", Data:"+ cacheData);
		Thread.sleep(50);

		start = System.currentTimeMillis();
		cacheData = service.getMyData("b");
		end = System.currentTimeMillis();
		System.out.println("Time taken : " + (end - start) + ", Data:"+ cacheData);
		Thread.sleep(50);

		start = System.currentTimeMillis();
		cacheData = service.getMyData("b");
		end = System.currentTimeMillis();
		System.out.println("Time taken : " + (end - start) + ", Data:"+ cacheData);
		Thread.sleep(50);
		
		start = System.currentTimeMillis();
		cacheData = service.getMyData("a");
		end = System.currentTimeMillis();
		System.out.println("Time taken : " + (end - start) + ", Data:"+ cacheData);
		Thread.sleep(50);

		start = System.currentTimeMillis();
		cacheData = service.getMyData("b");
		end = System.currentTimeMillis();
		System.out.println("Time taken : " + (end - start) + ", Data:"+ cacheData);
		Thread.sleep(50);

	}

	void annotationCacheRemove() throws Exception {
		System.out.println("Running Simple Annotation based (Spring + XML) based cache...");

		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring-cache-config.xml");
		RefData service = (RefData) context.getBean("refDataService");
		long start = System.currentTimeMillis();
		service.getMyData("a");
		service.getMyData("b");
		long end = System.currentTimeMillis();
		System.out.println("Time taken for first execution...." + (end - start));
		
		service.removeAllData();
		String adata = service.getMyData("a");
		System.out.println("a: "+adata);
		String bdata = service.getMyData("b");
		System.out.println("b: "+bdata);
	}
	
	void annotationCacheMultiple() throws Exception {
		System.out.println("Running Simple Annotation based (Spring + XML) based cache...");

		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring-cache-config.xml");
		RefData service = (RefData) context.getBean("refDataService");
		long start = System.currentTimeMillis();
		service.getMyData("a");
		service.getMyData("b");
		long end = System.currentTimeMillis();
		System.out.println("Time taken for first execution...." + (end - start));
		
		service.removeAllData();
		String adata = service.getMyData("a");
		System.out.println("a: "+adata);
		service.loadData("","");
		service.loadData("","");
		adata = service.getMyData("a");
		System.out.println("a: "+adata);
		String bdata = service.getMyData("b");
		System.out.println("b: "+bdata);
		adata = service.getMyData("a");
		System.out.println("a: "+adata);
		bdata = service.getMyData("b");
		System.out.println("b: "+bdata);
		
	}
	
}

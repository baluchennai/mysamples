package test.cache.ehcache3;

import java.util.HashMap;
import java.util.Map;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;

import org.springframework.stereotype.Repository;

//Annotate with @Repository and provide bean definition in spring-beans xml
@Repository("refDataService")
//If CacheDefaults used, there is no need of putting cachename for all individual methods.
@CacheDefaults(cacheName="refData")
public class RefData {
	
	private Map<String, String> dataMap;
	
	public RefData(){
		dataMap = new HashMap<>();
		dataMap.put("a","Apple");
		dataMap.put("b","Banana");
	}

	@CachePut
	public void loadData(@CacheKey String key, @CacheValue String value) throws Exception {
		System.out.println("RefData is being built....");
		dataMap = new HashMap<>();
		dataMap.put("a","Apple");
		dataMap.put("b","Banana");
	}

	//Method HAVE to be declard as public to make cache work !
	@CacheResult
	//@CacheResult(cacheName="refData")
	public String getMyData() throws Exception {
		System.out.println("RefData is being retrieved....");
		StringBuffer buffer = new StringBuffer();
		buffer.append("Hello...");
		for (int i = 0; i < 100; i++) {
			Thread.sleep(1);
			buffer.append(i + ",");
		}
		return buffer.toString();
	}

	//Method HAVE to be declard as public to make cache work !
	@CacheResult
	//@CacheResult(cacheName="keyRefData")
	public String getMyData(String key) throws Exception {
		System.out.println("RefData is being fetched....");
		Thread.sleep(1000);
		return dataMap.get(key);
	}
	
	//With same cache name, items are immediately removed, 
	//where as with different name allows cache to remain until expiry
	@CacheRemove
	//@CacheRemove(cacheName="keyRefData")
	public void removeMyData(String key) throws Exception {
		System.out.println("RefData is being removed....");
		dataMap.remove(key);
		//Thread.sleep(1000);
	}
	
	
	@CacheRemoveAll
	public void removeAllData(){
		System.out.println("All RefData is being removed....");
		dataMap.clear();
	}
	
}

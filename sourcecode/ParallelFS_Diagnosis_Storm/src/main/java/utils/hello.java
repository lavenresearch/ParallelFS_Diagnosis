package utils;
import java.lang.System;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class hello {
	Map<String,List<String>> a = new HashMap<String,List<String>>();
	public Float ReturnFloat(float a){
		return (Float) a;
	}
	public void listtest(Map<String,List<String>> p){
		List<String> pl = p.get("key");
		pl.add("add by pl");
	}
	public void main1() {
		// TODO Auto-generated method stub
		System.out.println("hello");
//		float a = 1;
//		hello b = new hello();
//		Float c = (Float) a;
//		Float d = b.ReturnFloat(a);
//		System.out.println(c+d);
//		Long time1 = 458795468452L;
//		for(int i = 0 ; i<=33 ; i++){
//			Long stime1 = (time1+i)/32 * 32;
//			System.out.println(i);
//			System.out.println(time1+i);
//			System.out.println(stime1);
//		}
		List<String> la = new ArrayList<String>();
		la.add("add by la");
		a.put("key", la);
		System.out.println(a.get("key").size());
		System.out.println(a.get("key").get(0));
		List<String> lb = new ArrayList<String>();
		lb = la;
		System.out.println(lb.get(0));
		lb.add("add by lb");
		System.out.println(la.size());
		System.out.println(la.get(0));
		System.out.println(la.get(1));
		System.out.println(a.get("key").size());
		System.out.println(a.get("key").get(0));
		System.out.println(a.get("key").get(1));
		listtest(a);
		System.out.println(a.get("key").size());
		System.out.println(a.get("key").get(0));
		System.out.println(a.get("key").get(1));
		System.out.println(a.get("key").get(2));
	}
	public static void main(String[] args){
		hello h = new hello();
		h.main1();
	}
}

package utils;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;
import java.lang.Math;

public class PDF {
//	probability distribution function
	public Map<Long,Float> pdf = new HashMap<Long,Float>();
	public List<Long> standardized_values = new ArrayList<Long>();
	public Float avg = (float) 0;
	public PDF(Queue<Float> values){
		Long val = 0L;
		standardized_values.clear();
		Long sum = 0L;
		for(Float value:values){
			val = value.longValue();
			standardized_values.add(val);
			sum += val;
		}
		avg =(float) sum/standardized_values.size();
		CalculatePDF();
	}
	public PDF(String pdf_string){
		String[] pdf_stringArray = pdf_string.split(";");
		pdf.clear();
		avg = (float) 0;
		for(int i=0; i<pdf_stringArray.length; i++){
			String[] keyValue = pdf_stringArray[i].split(" ");
			Long key = Long.valueOf(keyValue[0]);
			Float value = Float.valueOf(keyValue[1]);
			pdf.put(key, value);
			avg = key*value;
		}
	}
	private void CalculatePDF(){
		int total = standardized_values.size();
		Map<Long,Integer> pdf_counts = new HashMap<Long,Integer>();
		for(Long value:standardized_values){
			Integer count = pdf_counts.get(value);
			if( count == null){
				count = 0;
			}
			count++;
			pdf_counts.put(value, count);
		}
		pdf.clear();
		for(Long key:pdf_counts.keySet()){
			int count = pdf_counts.get(key);
			Float percentage = (float) count/total; 
			pdf.put(key, percentage);			
		}
	}
	public String ToString(){
		String pdf_string = "";
		for(Long key:pdf.keySet()){
			Float percentage = pdf.get(key);
			pdf_string = pdf_string + key.toString() + " " + percentage.toString() + ";";			
		}
		return pdf_string;
	}
	public Float CalDistance(PDF pdf2){
		Float distance = (float) 0;
//		Set<Long> pdfKeys = pdf.keySet();
//		Set<Long> pdf2Keys = pdf2.pdf.keySet();
//		pdfKeys.addAll(pdf2Keys);
//		double sum = 0;
//		for(Long key:pdfKeys){
//			Float P = pdf.get(key);
//			Float Q = pdf2.pdf.get(key);
//			double PQ = 0;
//			double QP = 0;
//		}
		distance = Math.abs(avg - pdf2.avg);
		return distance;
	}
}

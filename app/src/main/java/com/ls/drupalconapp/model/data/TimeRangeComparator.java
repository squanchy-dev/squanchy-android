package com.ls.drupalconapp.model.data;

import java.util.Calendar;
import java.util.Comparator;

public class TimeRangeComparator implements Comparator<TimeRange> {

	@Override
	public int compare(TimeRange first, TimeRange second) {

		if (first == null || second == null) {
			return -1;
		}

		Calendar fFromTime = first.getFromTime();
		Calendar fToTime = first.getToTime();

		Calendar sFromTime = second.getFromTime();
		Calendar sToTime = second.getToTime();
		
		if(fFromTime == null && fToTime == null && sFromTime == null && sToTime== null){
			return 0;
		} else if (fFromTime == null && fToTime == null) {
			return 1;
		} else if (sFromTime == null && sToTime == null){
			return -1;
		} else if (fFromTime == null || fToTime == null) {
			return 1;
		} else if (sFromTime == null || sToTime == null){
			return -1;
		}

		long fFromMillis = fFromTime.getTimeInMillis();
		long fToMillis = fToTime.getTimeInMillis();
		
		long sFromMillis = sFromTime.getTimeInMillis();
		long sToMillis = sToTime.getTimeInMillis();
		
		if(fFromMillis == sFromMillis && fToMillis == sToMillis){
			return 0;
		} 
		
		else if (fFromMillis <= sFromMillis && fToMillis<= sToMillis){
			return -1;
		} 
		
		else if (fFromMillis <= sFromMillis && fToMillis >= sToMillis){
			return -1;
		} 
		
		else if(fFromMillis >= sFromMillis && fToMillis >= sToMillis){
			return 1;
		}
		
		else if(fFromMillis >= sFromMillis && fToMillis <= sToMillis){
			return 1;
		}
		
		return 0;
	}

}

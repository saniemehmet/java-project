package uni.project.mydocapp.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import uni.project.mydocapp.entities.WorkingScheduleEntity;

public class ScheduleDateComparator implements Comparator<WorkingScheduleEntity>{

	@Override
	public int compare(WorkingScheduleEntity o1, WorkingScheduleEntity o2) {
		Date date1 = null;
		Date date2 = null;
		try {
			date1=new SimpleDateFormat("dd/MM/yyyy").parse(o1.getDate());
			date2=new SimpleDateFormat("dd/MM/yyyy").parse(o2.getDate());
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		
		return date2.compareTo(date1);
	}

}

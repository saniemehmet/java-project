package uni.project.mydocapp.comparators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import uni.project.mydocapp.entities.AppointmentEntity;

public class AppointmentDateComparator implements Comparator<AppointmentEntity>{

	@Override
	public int compare(AppointmentEntity o1, AppointmentEntity o2) {
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

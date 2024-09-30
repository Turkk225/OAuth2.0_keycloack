package com.kse.core.authkeycloak.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class DateTools {
	
	/*public Date DateTimeStamp() {
		Date date = new Date();
		Timestamp ts = new Timestamp(date.getTime());
		return ts;

	}*/
	
	/**
	 * 
	 * @return the current date in timestamp
	 * 
	 */
	public Timestamp DateTimeStamp() {
		Date today = new Date();
		return new Timestamp(today.getTime());
	}
	public Date ConvertDate(Date date){

	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    String s = df.format(date);
	    String result = s;
	    try {
	        date=df.parse(result);
	    } catch (ParseException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    return date;
	  }
	
	public Timestamp ConvertToTimestamp(String value) throws ParseException {
		String dateFormat="";
		if(value.length() == 16) {
			 dateFormat = "yyyy-MM-dd HH:mm";
		}else {
			dateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
		}
		log.debug(" dateFormat "+dateFormat);

		
        SimpleDateFormat formatter6=new SimpleDateFormat(dateFormat);
        Date date = formatter6.parse(value);
    	Timestamp ts= new Timestamp(date.getTime());  
    	return ts;

	}
	
	public long CalculNbDays(Date datedebut, Date datefin) {
		long diff = datefin.getTime() - datedebut.getTime();
	    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);		
	}
	
	public Date CalculDateFin(Date datedebut, long nbDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(datedebut);
		cal.add(Calendar.DATE, (int) nbDays);	
		
		Date newdate = cal.getTime();
		Date datefin = this.ConvertDate(newdate);
		return datefin;
	}
	
	
	public String GetDate() {
		Date dt = new Date();
	    DateFormat df = new SimpleDateFormat("yyMMddHHmmssSSS");
	    String date = df.format(dt);
	    return date;

	}
	public String GetTodayDate() {
		Date dt = new Date();
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	    String date = df.format(dt);
	    return date;

	}
	
	 public LocalDateTime convertToEntityAttribute(Timestamp ts) {
	    	if(ts!=null){
	    		   return ts.toLocalDateTime();
	    	}
	    	return null;
	    }
	
		public LocalDateTime convertToLocalDateTimeViaSqlTimestamp(Date dateToConvert) {
		    return new Timestamp(dateToConvert.getTime()).toLocalDateTime();
		 }


	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd - HH:mm:ss");


	/**
	 * @return the dateFormat
	 */
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String genDbFormatDateTime() {
		setDateFormat(new SimpleDateFormat("yyyyMMddHHmmss"));
		return getDateFormat().format(new Date());
	}
}

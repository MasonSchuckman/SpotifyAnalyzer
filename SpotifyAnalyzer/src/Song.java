

import java.util.Calendar;
import java.util.Locale;

public class Song implements Comparable{
	private String artist,name;
	private int ms=0;
	private String date;
	private String month, day, year;
	
	private int week = 0;
	
	public Song(String art,String songName,String datePlayed,int ms) {
		name=songName;
		artist=art;
		this.ms=ms;
		date=datePlayed;
		setDate();
		Calendar calendar = Calendar.getInstance(Locale.US); 
		calendar.set(getIntYear(), getIntMonth(), getIntDay()); 
		week = calendar.get(Calendar.WEEK_OF_YEAR);
		//System.out.println(weekOfYear);
		
	}
	public String toString() {
		return name+"  By "+artist;
	}
	public int getTime() {
		return ms;
	}
	public String getArtist() {
		return artist;
	}
	public String getName() {
		return name;
	}
	
	
	public int getIntMonth() {
		return Integer.parseInt(month);
	}
	
	public int getIntDay() {
		return Integer.parseInt(day);
	}
	
	public int getIntYear() {
		return Integer.parseInt(year);
	}
	public int getIntWeek() {
		return week;
	}
	
	
	public String getMonth() {
		return month;
	}
	private void setDate() {
		//all these " date.indexOf("-") " is based off of the "YYYY-MM-DD HH:MM" time formatting spotify uses. 
		//"-" will always appear in the date first before any "-"'s in the song name.
		year = date.substring(date.indexOf("-")-4, date.indexOf("-"));
		month = date.substring(date.indexOf("-")+1, date.indexOf("-")+3);
		day = date.substring(date.indexOf("-")+4, date.indexOf("-")+6);
		
		//System.out.println(month + " " + day + " " + year);
	}
	
	
	@Override
	public int compareTo(Object song2) {
		return ((Song) song2).getArtist().compareTo(getArtist());		
	}
	
}

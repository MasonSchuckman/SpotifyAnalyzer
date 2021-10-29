

public class UniqueSong implements Comparable<Object>{
	private String name, artist;
	private int time;
	public UniqueSong(String fullTitle,String artist) {
		name=fullTitle;
		this.artist = artist;
	}
	
	public UniqueSong(Song song) {
		name = song.getName();
		artist = song.getArtist();
	}
	
	public void add(int ms) {
		time+=ms;
	}
	
	public String getID() {
		return artist + " " + name;
	}
	
	public int getTime() {
		return time;
	}
	public String toString() {
		return name+", time played: "+time/1000/60+" minutes";
	}
	@Override
	public int compareTo(Object song2) {
		return ((UniqueSong) song2).getID().compareTo(getID());
	}
}

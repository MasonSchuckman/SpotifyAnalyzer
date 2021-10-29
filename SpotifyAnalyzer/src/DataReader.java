

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class DataReader {
	public static void main(String [] args) throws IOException {
		//String path1="M:/MyData/his0.json";
		//String alicia="M:/MyData/alicia.json";
		ArrayList<Song> songs = new ArrayList<Song>();


		//read all the files in the directory and add the song data to the songs array list
		File directory = new File("M:\\my_spotify_data");	//file dir
		File [] listOfFiles = directory.listFiles();			//all the files in the dir

		for(File file : listOfFiles) {						//iterate through all the files
			if(file.isFile()) {
				System.out.println(file.toString());		//print the file name we're trying to use
				songs.addAll(getSongs(file.toString()));	//add the songs in the file
			}
		}



		System.out.println(songs.size() + " Total songs loaded!");		
		//System.out.println(songs.get(0).getIntMonth());
		Map<String, Integer> topSongs=getTopSongs(songs);
		Map<String, Integer> topArtists=getTopArtists(songs);

		String [] favSongs = topSongs.keySet().toArray(new String[0]); //sorted by minutes. 
		String [] favArtists = topArtists.keySet().toArray(new String[0]); //sorted by minutes. 

		getListeningHistory(songs, 2020, favSongs, 100);


		int [] songTimes=convertToArray(topSongs);
		int [] artistTimes=convertToArray(topArtists);
		//prints fav artists
		System.out.println("*** Favorite artists: ***");
		for(int i = 0; i < 50; i++) {
			System.out.println("Number " + (i + 1) + " was: " + favArtists[i] + ", with a total of: " + artistTimes[i] + " minutes!");
		}
		System.out.println();

		System.out.println("*** Favorite songs: ***");
		for(int i = 0; i < 50; i++) {
			System.out.println("Number " + (i + 1) + " was: " + favSongs[i] + "," + songTimes[i] + " minutes,");
		}

		System.out.println("\n\n***Monthly Breakdowns***");

		int startingYear = 2020;
		int endingYear = 2020;
		int startingMonth = 1; 		//Number of starting month
		int endingMonth = 12;		//Number of ending month
		int howMany = 5; 		 	//Top <howMany> songs of each month is printed.
		//Ex: if howMany = 3, it will print top 3 songs per month	
		System.out.println("\nTop " + howMany + " Songs Each Month:");
		for(int year = startingYear; year <= endingYear; year++) {
			for(int month = startingMonth; month <= endingMonth; month++) {

				System.out.println(year + "/" + month + "\n");
				Map<String, Integer> topMonthSongs=getTopMonthSongs(songs, year, month);
				String[] favMonthSongs = topMonthSongs.keySet().toArray(new String[0]);

				int[] songMonthTimes=convertToArray(topMonthSongs);

				for(int j = 0; j < Math.min(howMany, favMonthSongs.length); j++) {
					System.out.print("Number "+(j+1)+" was: " +favMonthSongs[j]+", with a total of: "+songMonthTimes[j]+" minutes!\n");
				} 
				System.out.println("________________________________________________________________________\n");			
			}
		}
		System.out.println("\nTop " + howMany + " Artists Each Month:");
		for(int year = startingYear; year <= endingYear; year++) {

			for(int month = startingMonth; month <= endingMonth; month++) {

				System.out.println(year + "/" + month + "\n");
				Map<String, Integer> topMonthArtists=getTopMonthArtists(songs, year, month);
				String[] favMonthArtists = topMonthArtists.keySet().toArray(new String[0]);

				int[] artistMonthTimes=convertToArray(topMonthArtists);
				for(int j = 0; j < Math.min(howMany, favMonthArtists.length); j++) {
					System.out.print("Number "+(j+1)+" was: " +favMonthArtists[j]+", with a total of: "+artistMonthTimes[j]+" minutes!\n");
				} 
				System.out.println("________________________________________________________________________\n");			
			}
		}

		//prints fav songs and formats the output
		for(int i = 0; i < 50; i++) {
			String s = favSongs[i];
			while(s.indexOf("By: ")<20) {
				s = insertString(s, " ", s.indexOf("By: ")-1);
			}
			favSongs[i] = s;

			System.out.println(favSongs[i]);
		}

	}

	//this method will return the weekly breakdown for all the songs in the "top songs" array.
	private static void getListeningHistory(ArrayList<Song> songs, int startYear, String[] topSongs, int topXSongs) {		
		//organizes listening by week.
		HashMap<String, int[]> timePerWeek = new HashMap<String, int[]>();
		TreeSet<String> allSongs = new TreeSet<String>();
		TreeSet<String> topSongsSet = new TreeSet<String>();

		for(int i = 0; i < topXSongs; i++) {
			topSongsSet.add(topSongs[i]);
		}


		int weeksDone = 0;
		for(int year = startYear; year < startYear + 2; year++) {
			for(int week = 1; week <= 52; week++) {
				ArrayList<Song> songsInWeek=getWeekSongs(songs, year, week);
				System.out.println(week + " " + songsInWeek.size());

				for(int i = 0; i < songsInWeek.size(); i++) {
					String song = songsInWeek.get(i).toString();
					allSongs.add(song);
					if(timePerWeek.containsKey(song)) {
						timePerWeek.get(song)[weeksDone] += songsInWeek.get(i).getTime();

					}else {
						timePerWeek.put(song, new int[52 * 2]);
						timePerWeek.get(song)[weeksDone] += songsInWeek.get(i).getTime();
					}
				}
				weeksDone++;
			}
		}

		for(String song : allSongs) {
			int [] timePlayedForSong = timePerWeek.get(song);
			if(timePlayedForSong != null && topSongsSet.contains(song)) {
				System.out.print(song + ",");
				for(int i : timePlayedForSong) {
					System.out.print(i + ",");
				}
				System.out.println();
			}
		}



	}

	public static String insertString( 
			String originalString, 
			String stringToBeInserted, 
			int index) 
	{ 

		// Create a new StringBuffer 
		StringBuffer newString = new StringBuffer(originalString); 

		// Insert the strings to be inserted 
		// using insert() method 
		newString.insert(index + 1, stringToBeInserted); 

		// return the modified String 
		return newString.toString(); 
	} 

	private static int[] convertToArray(Map<String, Integer> topSongs) {
		Collection<Integer>vals = topSongs.values();
		Object [] times = vals.toArray();

		int time [] = new int[vals.size()];
		for(int i = 0; i<vals.size(); i++) {
			time[i] = ((Integer) times[i]).intValue() / 1000 / 60;			
		}
		return time;
	}
	private static Map<String, Integer> getTopMonthSongs(ArrayList<Song> songs, int year, int month) {
		ArrayList<Song> songsInMonth = getMonthSongs(songs, year, month);
		Map<String, Integer> topMonthSongs = getTopSongs(songsInMonth);
		return topMonthSongs;
	}

	private static Map<String, Integer> getTopMonthArtists(ArrayList<Song> songs, int year, int month) {
		ArrayList<Song> songsInMonth = getMonthSongs(songs, year, month);
		Map<String, Integer> topMonthArtists = getTopArtists(songsInMonth);
		return topMonthArtists;
	}


	private static ArrayList<Song> getMonthSongs(ArrayList<Song> songs,int year, int month) {
		ArrayList<Song> songsInMonth = new ArrayList<Song>();
		for(Song song : songs) {
			if(song.getIntMonth() == month && song.getIntYear() == year) 
				songsInMonth.add(song);
		}
		return songsInMonth;
	}

	private static ArrayList<Song> getWeekSongs(ArrayList<Song> songs, int year, int week) {
		ArrayList<Song> songsInWeek = new ArrayList<Song>();
		for(Song song : songs) {
			if(song.getIntWeek() == week && song.getIntYear() == year) 
				songsInWeek.add(song);
		}
		return songsInWeek;
	}

	private static Map<String, Integer> getTopArtists(ArrayList<Song> songs) {
		Map<String,Integer> artistMap = new TreeMap<String,Integer>();  

		for(Song song : songs) {		
			String s = song.getArtist();
			if(!artistMap.containsKey(s)) {
				artistMap.put(s, song.getTime());

			}else {				
				int currentTime = artistMap.get(s);
				artistMap.put(s, currentTime+=song.getTime());
			}
		}

		final Map<String, Integer> sortedByCount = artistMap.entrySet()
				.stream()
				.sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		return sortedByCount;			
	}

	private static Map<String, Integer> getTopSongs(ArrayList<Song> songs) {
		Map<String,Integer> songMap=new TreeMap<String,Integer>();

		for(Song song : songs) {		
			String s=song.toString();
			if(!songMap.containsKey(s)) {
				songMap.put(s, song.getTime());

			}else {				
				int currentTime=songMap.get(s);
				songMap.put(s, currentTime+=song.getTime());
			}
		}

		final Map<String, Integer> sortedByCount = songMap.entrySet()
				.stream()
				.sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		return sortedByCount;		
	}

	public static String prettyPrintMap(Map<String, Integer> map) {
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, Integer>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			//System.out.println("dad");
			Entry<String, Integer> entry = iter.next();
			sb.append(entry.getKey());
			sb.append('=').append('"');
			sb.append(entry.getValue());
			sb.append('"');
			if (iter.hasNext()) {
				sb.append(',').append(' ');
			}
		}
		return sb.toString();

	}



	private static ArrayList<Song> getSongs(String path) throws IOException{
		File file=new File(path);
		Scanner sc=new Scanner(file);
		ArrayList<Song> songs=new ArrayList<Song>();

		FileInputStream inputStream = null;		
		try {
			inputStream = new FileInputStream(file);
			sc = new Scanner(inputStream, "UTF-8");
			while (sc.hasNextLine()) {

				if(sc.nextLine().endsWith("{")) {
					String date=sc.nextLine();				
					String artist=sc.nextLine();
					artist=artist.substring(artist.indexOf(":")+2).replaceAll(",", "").replaceAll("\"", "");

					String song=sc.nextLine();
					song=song.substring(song.indexOf(":")+2).replaceAll(",", "");
					//System.out.println(song);
					String timePlayed=sc.nextLine();
					int time=Integer.parseInt(timePlayed.substring(timePlayed.indexOf(":")+2));

					Song created=new Song(artist,song,date,time);
					songs.add(created);

					//compare songs based on name and artist
					//compare songs based on ms played
					//sort songs by time, and artist
					//sort songs in months					
				}
			}

			// note that Scanner suppresses exceptions
			if (sc.ioException() != null) {
				throw sc.ioException();
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (sc != null) {
				sc.close();
			}
		}

		return songs;
	}
}


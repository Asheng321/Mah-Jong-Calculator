package ch.ysdc.mahjongcalculator.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import android.util.Log;

public class FileManager {

	private static String TAG = "FileManager";
	private File appFolder;
	
	public FileManager(File f){
		appFolder = f;
	}
	

	/****************************************************************************
	 * Store an hashmap in the file system
	 ****************************************************************************/
	public void saveHashMap(HashMap<String,Integer> t, String filename){
		Log.d(TAG, "savePlayerTiles: " + t.size());
		ObjectOutputStream outputStream = null;
		try {
				File file = new File(appFolder,filename);
				outputStream = new ObjectOutputStream(new FileOutputStream(file));
				outputStream.writeObject(t);
				outputStream.flush();
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "Exception during savePlayerTiles", e);
			}
	}

	/****************************************************************************
	 * Read an hashmap in the file system
	 ****************************************************************************/
	@SuppressWarnings("unchecked")
	public HashMap<String,Integer> readHashMap(String filename){
		Log.d(TAG, "readPlayerTiles");
		ObjectInputStream inputStream = null;
		HashMap<String,Integer> t = null;
		try {
			File file = new File(appFolder,filename);
			if(!file.exists()){
				return new HashMap<String,Integer>();
			}
			inputStream = new ObjectInputStream(new FileInputStream(file));
			t = (HashMap<String,Integer>)inputStream.readObject();
			inputStream.close();
			Log.d(TAG, "tiles: " + (t != null ? t.size() : "null"));
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Exception during readPlayerTiles", e);
		}

		return (t == null ? new HashMap<String,Integer>() : t);
	}

	/****************************************************************************
	 * Store a possibility in the file system
	 ****************************************************************************/
	public void saveHashMap(HashMap<String,Integer> t, String filename){
		Log.d(TAG, "savePlayerTiles: " + t.size());
		ObjectOutputStream outputStream = null;
		try {
				File file = new File(appFolder,filename);
				outputStream = new ObjectOutputStream(new FileOutputStream(file));
				outputStream.writeObject(t);
				outputStream.flush();
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "Exception during savePlayerTiles", e);
			}
	}

	/****************************************************************************
	 * Read an hashmap in the file system
	 ****************************************************************************/
	@SuppressWarnings("unchecked")
	public HashMap<String,Integer> readHashMap(String filename){
		Log.d(TAG, "readPlayerTiles");
		ObjectInputStream inputStream = null;
		HashMap<String,Integer> t = null;
		try {
			File file = new File(appFolder,filename);
			if(!file.exists()){
				return new HashMap<String,Integer>();
			}
			inputStream = new ObjectInputStream(new FileInputStream(file));
			t = (HashMap<String,Integer>)inputStream.readObject();
			inputStream.close();
			Log.d(TAG, "tiles: " + (t != null ? t.size() : "null"));
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Exception during readPlayerTiles", e);
		}

		return (t == null ? new HashMap<String,Integer>() : t);
	}
//	private void loadGame(){
//		Log.d(TAG, "loadGame");
//		ObjectInputStream inputStream = null;
//		try {
//			File file = new File(getFilesDir(),GAME_FILENAME);
//			if(file.exists()){
//				inputStream = new ObjectInputStream(new FileInputStream(file));
//				currentGame = (Game)inputStream.readObject();
//				inputStream.close();
//				Log.d(TAG, "Loaded Game: " + currentGame + "," + currentGame.getHand());
//			}else{
//				Log.d(TAG, "No game to load");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.e(TAG, "Exception during loadGame", e);
//		}
//	}
//	private void saveGame(){
//		Log.d(TAG, "saveGame: " + currentGame + "," + currentGame.getHand());
//		ObjectOutputStream outputStream = null;
//		try {
//				File file = new File(getFilesDir(),GAME_FILENAME);
//				outputStream = new ObjectOutputStream(new FileOutputStream(file));
//				outputStream.writeObject(currentGame);
//				outputStream.flush();
//				outputStream.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//				Log.e(TAG, "Exception during saveGame", e);
//			}
//	}
}

package ch.ysdc.mahjongcalculator.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import android.util.Log;
import ch.ysdc.mahjongcalculator.model.Hand;

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
	 * Store a hand in the file system
	 ****************************************************************************/
	public void saveHand(Hand hand, String filename){
		Log.d(TAG, "saveHand: " + hand);
		ObjectOutputStream outputStream = null;
		try {
				File file = new File(appFolder,filename);
				outputStream = new ObjectOutputStream(new FileOutputStream(file));
				outputStream.writeObject(hand);
				outputStream.flush();
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "Exception during saveHand", e);
			}
	}

	/****************************************************************************
	 * Read an hand in the file system
	 ****************************************************************************/
	public Hand readHand(String filename){
		Log.d(TAG, "readHand");
		ObjectInputStream inputStream = null;
		Hand hand = null;
		try {
			File file = new File(appFolder,filename);
			if(!file.exists()){
				return null;
			}
			inputStream = new ObjectInputStream(new FileInputStream(file));
			hand = (Hand)inputStream.readObject();
			inputStream.close();
			Log.d(TAG, "hand: " + hand);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Exception during readHand", e);
		}
		return (hand != null ? hand : new Hand());
	}

}

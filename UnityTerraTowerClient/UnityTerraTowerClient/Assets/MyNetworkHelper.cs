
using System; 
using UnityEngine;
using System.Collections; 
using System.Collections.Generic;
using MiniJSON;

/** This file is 
 * VERSION: 1.3 6/10/2014 0:00
 */
public class MyNetworkHelper : MonoBehaviour {

	/* This is the game server for the final project */
	string REST_URL = "http://djp3-pc2.ics.uci.edu:9021";
	//string REST_URL = "http://localhost:9021";
	//string REST_URL = "http://128.195.59.193:9021";

	/* This is a generic helper class for a 3D point */
	private class Point{
		public double lat;
		public double lng;
		public double alt;
		
		public Point(double lat, double lng,double alt){
			this.lat = lat;
			this.lng = lng;
			this.alt = alt;
		}

		public double getLat(){
			return lat;
		}

		public double getLng(){
			return lng;
		}

		public double getAlt(){
			return alt;
		}

		
		public Dictionary<string,string> toDictionary(){
			Dictionary<string,string> ret = new Dictionary<string,string> ();
			ret.Add ("lat", this.lat + "");
			ret.Add ("lng", this.lng + "");
			ret.Add ("alt", this.alt + "");
			return ret;
		}
	}
	
	/* These are variable internal to MyNetworkHelper.  Access them through getters and setters */
	private Point towerPoint = null;
	private Point bombPoint = null;

	/* This is how you store a point at which you'd like to build a tower.  This doesn't upload that point */
	public void buildTowerPoint(double lat, double lng, double alt){
		Debug.Log ("buildTowerPoint called");
		towerPoint = new Point (lat, lng, alt);
	}

	/* This tells you if you have a tower point to be uploaded */
	public bool isTowerSet(){
		return towerPoint != null;
	}
	
	public double getTowerLat(){
		return towerPoint.getLat();
	}
	
	public double getTowerLng(){
		return towerPoint.getLng();
	}
	
	public double getTowerAlt(){
		return towerPoint.getAlt();
	}

	/* This is how you store a point at which you'd like to drop a bomb.  This doesn't upload that point */
	public void dropBombPoint(double lat, double lng, double alt){
		Debug.Log ("addTowerPoint called");
		bombPoint = new Point (lat, lng, alt);
	}

	/* This tells you if you have a bomb point to be uploaded */
	public bool isBombSet(){
		return bombPoint != null;
	}
	
	public double getBombLat(){
		return bombPoint.getLat();
	}
	
	public double getBombLng(){
		return bombPoint.getLng();
	}
	
	public double getBombAlt(){
		return bombPoint.getAlt();
	}


	/* This executes an upload of the already stored Tower point,  the tower point will be cleared after success.  The callback is executed with the response,
	 mostly so that debug messages can be captured */
	public void uploadTowerPoint(string worldName,string worldPassword,string playerName,string playerPassword,Action<Dictionary<string,object>> callback){
		WWW www;

		if (towerPoint != null) {				
			string u = REST_URL + "/build_tower";
			u += "?world_name=" + WWW.EscapeURL (worldName);
			u += "&world_password=" + WWW.EscapeURL (worldPassword);
			u += "&player_name=" + WWW.EscapeURL (playerName);
			u += "&player_password=" + WWW.EscapeURL (playerPassword);
			u += "&lat=" + WWW.EscapeURL (towerPoint.lat + "");
			u += "&lng=" + WWW.EscapeURL (towerPoint.lng + "");
			u += "&alt=" + WWW.EscapeURL (towerPoint.alt + "");
			www = new WWW (u);
			StartCoroutine (WaitForTowerRequest (www, callback));
		} else {
			var ret = new Dictionary<string,object> ();
			ret.Add ("error", "false");
			callback(ret);
		}
	}


	/* This executes an upload of the already stored bomb point,  the bomb point will be cleared after successful upload.  The callback is executed with the response,
	 mostly so that debug messages can be captured */
	public void uploadBombPoint(string worldName,string worldPassword,string playerName,string playerPassword,Action<Dictionary<string,object>> callback){
		WWW www;

		if (bombPoint != null) {				
			string u = REST_URL + "/drop_bomb";
			u += "?world_name=" + WWW.EscapeURL (worldName);
			u += "&world_password=" + WWW.EscapeURL (worldPassword);
			u += "&player_name=" + WWW.EscapeURL (playerName);
			u += "&player_password=" + WWW.EscapeURL (playerPassword);
			u += "&lat=" + WWW.EscapeURL (bombPoint.lat + "");
			u += "&lng=" + WWW.EscapeURL (bombPoint.lng + "");
			u += "&alt=" + WWW.EscapeURL (bombPoint.alt + "");
			www = new WWW (u);
			StartCoroutine (WaitForBombRequest (www, callback));

		} else {
			var ret = new Dictionary<string,object> ();
			ret.Add ("error", "false");
			callback(ret);
		}
	}



	/* This executes an upload of the power up code. The callback is executed with the response,
	 mostly so that debug messages can be captured */
	public void uploadCode(string worldName,string worldPassword,string playerName,string playerPassword,string code,Action<Dictionary<string,object>> callback){
		WWW www;
		
		if (code != null) {				
			string u = REST_URL + "/redeem_power_up";
			u += "?world_name=" + WWW.EscapeURL (worldName);
			u += "&world_password=" + WWW.EscapeURL (worldPassword);
			u += "&player_name=" + WWW.EscapeURL (playerName);
			u += "&player_password=" + WWW.EscapeURL (playerPassword);
			u += "&code=" + WWW.EscapeURL (code);
			www = new WWW (u);
			StartCoroutine (WaitForRequest (www, callback));
		} else {
			var ret = new Dictionary<string,object> ();
			ret.Add ("error", "false");
			callback(ret);
		}
	}


	/* This requests the state of the game world from the server.  It requires a callback that will be called when the data arrives.
	 * The signature of the callback function should be like:
	 * 	private void callback(Dictionary<string,object> state){}
	 * when that function is called back it will receive a Dictionary with data about the world map. */
	public void refreshGameState(string worldName,string worldPassword,string playerName,string playerPassword,Action<Dictionary<string,object>> callback){
		WWW www;
		
					
		string u = REST_URL+"/get_game_state";
		u += "?world_name=" + WWW.EscapeURL (worldName);
		u += "&world_password=" + WWW.EscapeURL (worldPassword);
		u += "&player_name=" + WWW.EscapeURL (playerName);
		u += "&player_password=" + WWW.EscapeURL (playerPassword);
		www = new WWW (u);
		StartCoroutine(WaitForRequest (www,callback));

	}


	public void requestLeaderBoard(string worldName,string worldPassword,Action<Dictionary<string,object>> callback){
		WWW www;
		
		
		string u = REST_URL+"/get_leader_board";
		u += "?world_name=" + WWW.EscapeURL (worldName);
		u += "&world_password=" + WWW.EscapeURL (worldPassword);
		www = new WWW (u);
		StartCoroutine(WaitForRequest (www,callback));
		
	}




	
	
	private IEnumerator WaitForRequest (WWW www,Action<Dictionary<string,object>> callback)
	{
		yield return www;
		
		// check for errors
		if (www.error == null) {
			var dict = Json.Deserialize(www.text) as Dictionary<string,object>;
			callback(dict);
		} else {
			var dict = new Dictionary<string,object>();
			dict.Add("errors","Network error?");
			dict.Add("error","true");
			callback(dict);
		}    
	}

	private IEnumerator WaitForBombRequest (WWW www,Action<Dictionary<string,object>> callback)
	{
		yield return www;
		
		// check for errors
		if (www.error == null) {
			var dict = Json.Deserialize(www.text) as Dictionary<string,object>;
			object _result;
			dict.TryGetValue("error",out _result);
			if(_result.ToString().Equals ("false")){
				bombPoint = null;
			}
			callback(dict);

		} else {
			var dict = new Dictionary<string,object>();
			dict.Add("errors","bomb: Network error?");
			dict.Add("error","true");
			callback(dict);
		}    
	}

	private IEnumerator WaitForTowerRequest (WWW www,Action<Dictionary<string,object>> callback)
	{
		yield return www;
		
		// check for errors
		if (www.error == null) {
			var dict = Json.Deserialize(www.text) as Dictionary<string,object>;
			object _result;
			dict.TryGetValue("error",out _result);
			if(_result.ToString().Equals ("false")){
				towerPoint = null;
			}
			callback(dict);
			
		} else {
			var dict = new Dictionary<string,object>();
			dict.Add("errors","tower: Network error?");
			dict.Add("error","true");
			callback(dict);
		}    
	}
}

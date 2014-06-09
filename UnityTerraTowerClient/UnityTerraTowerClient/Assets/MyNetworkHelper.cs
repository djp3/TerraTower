
using System; 
using UnityEngine;
using System.Collections; 
using System.Collections.Generic;
using MiniJSON;

public class MyNetworkHelper : MonoBehaviour {

	
	//string REST_URL = "http://djp3-pc2.ics.uci.edu:9021";
	//string REST_URL = "http://localhost:9021";
	string REST_URL = "http://128.195.59.193:9021";

	

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
	

	Point towerPoint = null;
	Point bombPoint = null;
	string storedCode = null;

	
	public void buildTowerPoint(double lat, double lng, double alt){
		Debug.Log ("buildTowerPoint called");
		towerPoint = new Point (lat, lng, alt);
	}

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

	public void dropBombPoint(double lat, double lng, double alt){
		Debug.Log ("addTowerPoint called");
		bombPoint = new Point (lat, lng, alt);
	}

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

	public void enterCode(string code){
		Debug.Log ("enterCode called");
		storedCode = code;
	}


	public Dictionary<string,object> uploadTowerPoint(string worldName,string worldPassword,string playerName,string playerPassword){
		WWW www;

		if (towerPoint != null){				
			string u = REST_URL+"/build_tower";
			u += "?world_name=" + WWW.EscapeURL (worldName);
			u += "&world_password=" + WWW.EscapeURL (worldPassword);
			u += "&player_name=" + WWW.EscapeURL (playerName);
			u += "&player_password=" + WWW.EscapeURL (playerPassword);
			u += "&lat=" + WWW.EscapeURL (towerPoint.lat+"");
			u += "&lng=" + WWW.EscapeURL (towerPoint.lng+"");
			u += "&alt=" + WWW.EscapeURL (towerPoint.alt+"");
			www = new WWW (u);
			StartCoroutine (WaitForRequest (www));
			if(www.error == null){
				//Everything went ok
				towerPoint = null;
				var dict = Json.Deserialize(www.text) as Dictionary<string,object>;
				return dict;
			}
			else{
				var dict = new Dictionary<string,object>();
				dict.Add("error","true");
				return dict;
			}
		}
		var ret = new Dictionary<string,object>();
		ret.Add("error","false");
		return ret;
	}

	public Dictionary<string,object> uploadBombPoint(string worldName,string worldPassword,string playerName,string playerPassword){
		WWW www;

		if (bombPoint != null){				
			string u = REST_URL+"/drop_bomb";
			u += "?world_name=" + WWW.EscapeURL (worldName);
			u += "&world_password=" + WWW.EscapeURL (worldPassword);
			u += "&player_name=" + WWW.EscapeURL (playerName);
			u += "&player_password=" + WWW.EscapeURL (playerPassword);
			u += "&lat=" + WWW.EscapeURL (bombPoint.lat+"");
			u += "&lng=" + WWW.EscapeURL (bombPoint.lng+"");
			u += "&alt=" + WWW.EscapeURL (bombPoint.alt+"");
			www = new WWW (u);
			StartCoroutine (WaitForRequest (www));
			if(www.error == null){
				//Everything went ok
				bombPoint = null;
				var dict = Json.Deserialize(www.text) as Dictionary<string,object>;
				return dict;
			}
			else{
				var dict = new Dictionary<string,object>();
				dict.Add("error","true");
				return dict;
			}
		}
		var ret = new Dictionary<string,object>();
		ret.Add("error","false");
		return ret;
	}

	public Dictionary<string,object> uploadCode(string worldName,string worldPassword,string playerName,string playerPassword){
		WWW www;
		
		if (storedCode != null){				
			string u = REST_URL+"/redeem_power_up";
			u += "?world_name=" + WWW.EscapeURL (worldName);
			u += "&world_password=" + WWW.EscapeURL (worldPassword);
			u += "&player_name=" + WWW.EscapeURL (playerName);
			u += "&player_password=" + WWW.EscapeURL (playerPassword);
			u += "&code=" + WWW.EscapeURL (storedCode);
			www = new WWW (u);
			StartCoroutine (WaitForRequest (www));
			if(www.error == null){
				//Everything went ok
				storedCode = null;
				var dict = Json.Deserialize(www.text) as Dictionary<string,object>;
				return dict;
			}
			else{
				var dict = new Dictionary<string,object>();
				dict.Add("error","true");
				return dict;
			}
		}
		var ret = new Dictionary<string,object>();
		ret.Add("error","false");
		return ret;
	}

	public void refreshGameState(string worldName,string worldPassword,string playerName,string playerPassword,Action<Dictionary<string,object>> callback){
		WWW www;
		
					
		string u = REST_URL+"/get_game_state";
		u += "?world_name=" + WWW.EscapeURL (worldName);
		u += "&world_password=" + WWW.EscapeURL (worldPassword);
		u += "&player_name=" + WWW.EscapeURL (playerName);
		u += "&player_password=" + WWW.EscapeURL (playerPassword);
		www = new WWW (u);
		StartCoroutine(WaitForGameStateRequest (www,callback));

	}

	private IEnumerator WaitForGameStateRequest (WWW www,Action<Dictionary<string,object>> callback)
	{
		yield return www;

		
		// check for errors
		if (www.error == null) {
			Debug.Log ("WWW Ok!: " + www.text);
			//Everything went ok
			var dict = Json.Deserialize(www.text) as Dictionary<string,object>;
			callback(dict);
		} else {
			Debug.Log ("WWW Error: " + www.error);
			var dict = new Dictionary<string,object>();
			dict.Add("error","true");
			callback(dict);
		}   


	}

	
	
	private IEnumerator WaitForRequest (WWW www)
	{
		yield return www;
		
		// check for errors
		if (www.error == null) {
			Debug.Log ("WWW Ok!: " + www.text);
		} else {
			Debug.Log ("WWW Error: " + www.error);
		}    
	}
}

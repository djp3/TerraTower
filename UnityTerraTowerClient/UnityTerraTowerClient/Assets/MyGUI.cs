using UnityEngine;
using System.Collections;
using System.Collections.Generic;

	
/* Version 1.1 17:04pm */
public class MyGUI : MonoBehaviour
{		
	/********************  Things that are attached from the Unity GUI * *****************/

	/*This is so that we can adjust the look and feel of our elements using a
	 * GUISkin. It is assigned from the ui.*/
	public GUISkin myGUISkin;
	/* This is a prefab of what our towers are going to look like when we create
	 * them in the code. It is assigned from the ui. */
	public GameObject tower;
	/* This is a prefab of what our bombs are going to look like when we create
	 * them in the code. It is assigned from the ui. */
	public GameObject bomb;
	/* This is a here so that a special material that makes it easy to color the ground
	 * according to who owns the territory can be added to this script.  With
	 * the correct material assigned the color of the ground can be controlled
	 * by coloring the vertices of the mesh.*/
	public Material meshMaterial;
	/* If this is true, the location is determined by the camera */
	public bool firstPersonShooter;

	/********************  Private hooks to other parts of the environment *****************/
	/* This is a private hook to the camera that is used to control the view */
	private GameObject camera;
	private MyNetworkHelper myNetworkHelper;
	private MyLocation myLocation;
	private GyroController myGyroController;


	/********************  Variables that store the value from the GUI ****************/
	/* This is a toggle that is used to expand the GUI menu */
	private bool expandGUI;
	private string worldName;
	private string worldPassword;
	private string playerName;
	private string playerPassword;
	private string secretcode;

	/* This is helpful for debugging */
	private List<string> errors;

	/********************  These are variables that help to smoothly move the camera ****************/
	private Vector3 origin, destination;
	private float fracJourney;
	private float lastLat;
	private float lastLng;
	private float lastAlt;

	/********************  These are variables that are used to construct the game world ****************/
	/* These are set after the first call to get_game_state */
	float originX;
	float originY;
	float stepXMeters;
	float stepYMeters;
	int numXSplits;
	int numYSplits;

	/* These are used to draw and color a floor */
	Mesh myMesh = null;			//This is a mesh that is colored
	Vector3[] vertices;
	Color[] colors;
	
	/* These are used to keep track of GameObjects that we make dynamically,
	 * world is persistent objects, tempWorld are objects that come and go (like
	 * towers and bombs */
	List<Object> world = new List<Object>();
	List<Object> tempWorld = new List<Object>();

	/* This is a translation from owner name to color */
	Dictionary<string,Color> ownerColors = new Dictionary<string,Color>();

	/* These are the functions that set the hooks to other things in the environment */
		
	void checkNetworkHelper ()
	{
		if (myNetworkHelper == null) {
			myNetworkHelper = GameObject.Find ("MyNetworkHelper").GetComponentInChildren<MyNetworkHelper>();
		}
	}

	void checkMyLocation ()
	{
		if (myLocation == null) {
			myLocation = GameObject.Find ("MyLocation").GetComponentInChildren<MyLocation>();
		}
	}

	void checkMyGyroController ()
	{
		if (myGyroController == null) {
			myGyroController = GameObject.Find ("Main Camera").GetComponentInChildren<GyroController>();
		}
	}
		
		
	void Start ()
	{
		/* Initialize the error list */
		errors = new List<string>();

		/* Make sure the GUI Skin is set */
		if (this.myGUISkin == null) {  
			addDebug ("Please assign a GUIskin on the editor!");  
			this.enabled = false;  
			return;  
		} 
			
		/* Set the U/I fields to defaults */
		worldName = "earth2";
		worldPassword = "earth2";
		playerName = "w";
		playerPassword = "w21";
		secretcode = "dii4!dotty";
		expandGUI = true;
		lastExpandGUI = true;

		/* Get the hooks to the other elements of the environment */
		checkNetworkHelper ();
		checkMyLocation ();
		checkMyGyroController ();
		camera = GameObject.Find ("Main Camera");


		/* get an initial location */
		lastLat = myLocation.getLat();
		lastLng = myLocation.getLng();
		lastAlt = myLocation.getAlt();

		/* Set the camera components to initial values */
		fracJourney = 0.0f;
		destination = new Vector3 (lastLng,  lastAlt + 20, lastLat);
		origin = new Vector3 (camera.transform.position.x, camera.transform.position.y, camera.transform.position.z);


		/* Add a few default colors */
		ownerColors.Add ("barbarian",new Color(1.0f,1.0f,1.0f,0.0f));
		ownerColors.Add ("Theo",new Color(1.0f,0.0f,0.0f));
		ownerColors.Add ("Dad",new Color(1.0f,1.0f,0.0f));
	}

	/* Return a color for a given territory owner.  If we don't have a color,
	 * then pick one randomly */	
	public Color getOwnerColor(string name){
		Color color;
		//Debug.Log(name);
		if (!ownerColors.TryGetValue (name, out color)) {
			color = new HSBColor(Random.value,Random.Range(0.5f,1.0f),Random.Range(0.8f,1.0f)).ToColor();
			ownerColors.Add (name, color);

		}
		return color;
	}


	/* Add a debug message to errors so we can display it in the GUI */
	public void addDebug(string e){
		errors.Add (e);
		Debug.Log (e);
	}


	/* When we get our first game state we need to make the mesh of polygons
	 * that we want to display in the game world.  This does that.  The
	 * assumption is that 1 unit in the game world is 1 meter in the real world
	 * */
	void makeBaseMesh(int maxX,int maxY,float stepXMeters,float stepYMeters){

		//New mesh and game object
		GameObject go = new GameObject();
		go.name = "Ground";
		
		//Components
		MeshFilter MF = (MeshFilter) go.AddComponent("MeshFilter");
		MeshRenderer MR = (MeshRenderer) go.AddComponent("MeshRenderer");

		//Allocate space for the elements of the grid 
		vertices = new Vector3[maxX*maxY];
		Vector2[] verticesuv = new Vector2[maxX*maxY];
		colors = new Color[maxX*maxY];
		int[] triangles = new int[2*3*2 * (maxX-1) * (maxY - 1)]; // 2 sides, 3 points, 2*maxX-1*maxY-1 trangles

		//Assign the elements of the grid to defaults.  Notice that x mean
		//"longitude" and is x in the game world.  y means "latitude" and is *z*
		//in the game world.  altitude isn't used much and is *y* in the game
		//world.
		for (int y=0; y < maxY; y++) {
			for (int x=0; x < maxX; x++) {
				vertices [y*maxX+x] = new Vector3 (x * stepXMeters, 0, y * stepYMeters);
				verticesuv [y*maxX+x] = new Vector2(x * stepXMeters, y*stepYMeters);
				colors[y*maxX+x]=Color.blue;
			}
		}
		for (int y=0; y < maxY-1; y++) {
			for (int x=0; x < maxX-1; x++) {
				//First triangle front
				triangles [y*(maxX-1)*12+(x*12) + 2 ] = y*maxX+x;
				triangles [y*(maxX-1)*12+(x*12) + 1 ] = y*maxX+(x+1);
				triangles [y*(maxX-1)*12+(x*12) + 0 ] = (y+1)*maxX+x;
				//First triangle back
				triangles [y*(maxX-1)*12+(x*12) + 3 ] = y*maxX+x;
				triangles [y*(maxX-1)*12+(x*12) + 4 ] = y*maxX+(x+1);
				triangles [y*(maxX-1)*12+(x*12) + 5 ] = (y+1)*maxX+x;
				//Second triangle front
				triangles [y*(maxX-1)*12+(x*12) + 6 ] = (y+1)*maxX+(x+1);
				triangles [y*(maxX-1)*12+(x*12) + 7 ] = y*maxX+(x+1);
				triangles [y*(maxX-1)*12+(x*12) + 8 ] = (y+1)*maxX+x;
				//Second triangle back
				triangles [y*(maxX-1)*12+(x*12) + 11 ] = (y+1)*maxX+(x+1);
				triangles [y*(maxX-1)*12+(x*12) + 10 ] = y*maxX+(x+1);
				triangles [y*(maxX-1)*12+(x*12) + 9 ] = (y+1)*maxX+x;
			}
		}
		
		// Build the mesh
		myMesh = MF.sharedMesh;
		if (myMesh == null){
			MF.mesh = new Mesh();
			myMesh = MF.sharedMesh;
		}
		myMesh.Clear();
		myMesh.vertices = vertices;
		myMesh.uv = verticesuv;
		myMesh.triangles = triangles;
		myMesh.colors = colors;
		
		myMesh.RecalculateNormals();
		myMesh.RecalculateBounds();
		myMesh.Optimize();
		
		//Assign materials
		MR.material = meshMaterial;
		
		//Assign mesh to game object
		MF.mesh = myMesh;
		world.Add (go);
		

	}



	/* This is the callback function that is called when we get the game state back */
	private void refreshGameStateInGUI(Dictionary<string,object> state){

		object x;
		if(state.TryGetValue ("error", out x)){  // Make sure there isn't an error
			if(x.ToString().Equals("false")){
				if(state.TryGetValue ("data",out x)){ //Get the data component
					Dictionary<string,object> d = (Dictionary<string,object>) x;
					if(myMesh == null){  //If this is the first time we've gotten a game state make a mesh
						object _originX,_originY,_stepXMeters,_stepYMeters,_numXSplits,_numYSplits;
						d.TryGetValue ("origin_x", out _originX);
						d.TryGetValue ("origin_y", out _originY);
						d.TryGetValue ("num_x_splits", out _numXSplits);
						d.TryGetValue ("num_y_splits", out _numYSplits);
						d.TryGetValue ("step_x_meters", out _stepXMeters);
						d.TryGetValue ("step_y_meters", out _stepYMeters);
						originX = float.Parse(_originX.ToString());
						originY = float.Parse(_originY.ToString());
						stepXMeters = float.Parse(_stepXMeters.ToString());
						stepYMeters = float.Parse(_stepYMeters.ToString());
						numXSplits = int.Parse(_numXSplits.ToString());
						numYSplits = int.Parse(_numYSplits.ToString());

						makeBaseMesh(numXSplits,numYSplits,stepXMeters,stepYMeters);
					}

					//destroy the towers and bombs - this could be done better,
					//but oh well
					foreach(Object go in tempWorld){
						Destroy(go);
					}


					// Iterate through all the grid elements in the response
					object grids;
					d.TryGetValue("result", out grids);
					foreach(object y in (List<object>)grids){
						Dictionary<string,object> z = (Dictionary<string,object>)y;
						object _indexX,_indexY,_alt,_landOwner,_landClaim,_tower,_bomb;
						z.TryGetValue("index_x",out _indexX);
						z.TryGetValue("index_y",out _indexY);
						z.TryGetValue("alt",out _alt);
						z.TryGetValue("land_owner",out _landOwner);
						z.TryGetValue("land_claim",out _landClaim);
						z.TryGetValue("tower",out _tower);
						z.TryGetValue("bomb",out _bomb);
						int indexX = int.Parse(_indexX.ToString());
						int indexY = int.Parse(_indexY.ToString());
						float landClaim = float.Parse (_landClaim.ToString());
						float alt = float.Parse(_alt.ToString());

						// Set the territory color based on the owner indicated
						vertices [indexY*numXSplits+indexX].y = landClaim;
						colors[indexY*numXSplits+indexX] = getOwnerColor(_landOwner.ToString ());

						if(float.IsNaN(alt)){
							alt = 0.0f;
						}

						//If there is a tower, then create one
						if(_tower.ToString().Equals ("true")){
							Object thing = Instantiate(tower,new Vector3(indexX*stepXMeters,landClaim,indexY*stepYMeters),Quaternion.Euler(270,0,0));
							((GameObject)thing).transform.localScale = new Vector3(0.05f,0.05f,0.1f);
							tempWorld.Add (thing);
							Debug.Log("Got a tower"+indexX*stepXMeters+" "+alt+" "+indexY*stepYMeters);
						}

						//If there is a bomb, then create one
						if(_bomb.ToString().Equals ("true")){
							Object thing = Instantiate(bomb,new Vector3(indexX*stepXMeters,5.0f+landClaim,indexY*stepYMeters),Quaternion.identity);
							((GameObject)thing).transform.localScale = new Vector3(5.0f,5.0f,5.0f);
							tempWorld.Add (thing);
							Debug.Log("Got a bomb"+indexX*stepXMeters+" "+alt+" "+indexY*stepYMeters);

						}


					}

					//Reset the mesh
					myMesh.vertices = vertices;
					myMesh.colors=colors;

					myMesh.RecalculateNormals();
					myMesh.RecalculateBounds();
					myMesh.Optimize();
				}
			}
			else{
				if (state.TryGetValue ("errors", out x)) {
					foreach(object y in (List<object>)x){
						addDebug (y.ToString());
					}
				}
			}
		}
	}

	/* this callback does something with errors but nothing else */
	private void genericCallback(Dictionary<string,object> state){
		object x;
		if (state.TryGetValue ("error", out x)) {
			if (x.ToString ().Equals ("true")) {
				if (state.TryGetValue ("errors", out x)) {
					foreach(object y in (List<object>)x){
						addDebug (y.ToString());
					}
				}
			}
		}
	}

	/* This callback puts the leader board in the "error" structure*/
	private void leaderBoardCallback(Dictionary<string,object> state){
		
		object x;
		if (state.TryGetValue ("error", out x)) {
			if (x.ToString ().Equals ("true")) {
				if (state.TryGetValue ("errors", out x)) {
					foreach(object y in (List<object>)x){
						addDebug (y.ToString());
					}
				}
			}
			else{
				if (state.TryGetValue ("result", out x)) {
					string z ="";
					foreach(KeyValuePair<string,object> y in (Dictionary<string,object>)x){
						z += y.Key+" "+y.Value.ToString()+"     ";
					}
					addDebug(z);
				}
			}
		}
	}



		

	/* This draws the gui and attaches the buttons to the appropriate functions */
	int padding = 5;
	int margin = 40;
	bool lastExpandGUI;
	void OnGUI ()
	{
		GUI.skin = this.myGUISkin;
		int height = 175;
		int y = 0;

		if (GUI.Button (new Rect (padding + margin, padding + margin + y, Screen.width/2 - 2 * (padding + margin), height-2*margin), "Fetch Status")) {
			myNetworkHelper.refreshGameState(worldName,worldPassword,playerName,playerPassword,refreshGameStateInGUI);
		}

		expandGUI = GUI.Toggle (new Rect (Screen.width/2 + padding+margin, padding + margin + y, Screen.width/2 - 2 * (padding + margin), height-2*margin), expandGUI, "Expand");
			
		foreach (string e in errors) {
			y += height;
			GUI.TextArea (new Rect (padding + margin, padding + margin + y, Screen.width-2*(padding+margin), height-2*margin), e);
		}
		if (errors.Count > 1) {
			errors.RemoveAt (0);
		}

		if (GUI.changed) {
			if (lastExpandGUI != expandGUI) {
				if (expandGUI) {
					Input.gyro.enabled = false;
				} else {
					Input.gyro.enabled = true;
					errors.Clear ();
				}
			}
			lastExpandGUI = expandGUI;
		}

		if (expandGUI) {

			int leftmod = 0;
			int width = Screen.width;
			y += height;

			y += padding;
			GUI.Box (new Rect (0, y, Screen.width, padding), "");
			y += padding;

			GUI.Box (new Rect (padding, padding + y, width - 2 * (padding), height), "Current Location");
			string x = "(" + lastLat + "," + lastLng + ") " + lastAlt;
			GUI.Label (new Rect (padding + margin, padding + margin + y, width - 2 * (padding + margin), height - 2 * margin), x);

			if (myNetworkHelper.isTowerSet ()) {
				y += height;
				GUI.Box (new Rect (leftmod + padding, padding + y, width - 2 * (padding), height), "Tower Location");

				x = "(" + myNetworkHelper.getTowerLat () + "," + myNetworkHelper.getTowerLng () + ") " + myNetworkHelper.getTowerAlt ();
			
				GUI.Label (new Rect (leftmod + padding + margin, padding + margin + y, width - 2 * (padding + margin), height - 2 * margin), x);
			}
			
			if (myNetworkHelper.isBombSet ()) {
				y += height;
				GUI.Box (new Rect (leftmod + padding, padding + y, width - 2 * (padding), height), "Bomb Location");
				x = "(" + myNetworkHelper.getBombLat () + "," + myNetworkHelper.getBombLng () + ") " + myNetworkHelper.getBombAlt ();
				GUI.Label (new Rect (leftmod + padding + margin, padding + margin + y, width - 2 * (padding + margin), height - 2 * margin), x);

			}

			width = Screen.width / 2;

			y += height;
			int secondColumn = y;

			GUI.Box (new Rect (leftmod + padding, padding + y, Screen.width / 2 - 2 * (padding), height), "World Name");
			worldName = GUI.TextField (new Rect (padding + margin, padding + margin + y, Screen.width / 2 - 2 * (padding + margin), height - 2 * (margin)), worldName);

			y += height;

			GUI.Box (new Rect (leftmod + padding, padding + y, Screen.width / 2 - 2 * (padding), height), "World Password");
			worldPassword = GUI.TextField (new Rect (padding + margin, padding + margin + y, Screen.width / 2 - 2 * (padding + margin), height - 2 * (margin)), worldPassword);

			y += height;
			GUI.Box (new Rect (leftmod + padding, padding + y, Screen.width / 2 - 2 * (padding), height), "Player Name");
			playerName = GUI.TextField (new Rect (padding + margin, padding + margin + y, Screen.width / 2 - 2 * (padding + margin), height - 2 * (margin)), playerName);
			
			y += height;
			GUI.Box (new Rect (leftmod + padding, padding + y, Screen.width / 2 - 2 * (padding), height), "Player Password");
			playerPassword = GUI.TextField (new Rect (padding + margin, padding + margin + y, Screen.width / 2 - 2 * (padding + margin), height - 2 * (margin)), playerPassword);

			y += height;
			GUI.Box (new Rect (leftmod + padding, padding + y, Screen.width / 2 - 2 * (padding), height), "Enter Code");
			secretcode = GUI.TextField (new Rect (leftmod + padding + margin, padding + margin + y, Screen.width / 2 - 2 * (padding + margin), height - 2 * (margin)), secretcode);

			leftmod = Screen.width / 2;
			y = padding + secondColumn;


			if (GUI.Button (new Rect (leftmod + padding + margin, padding + margin + y, Screen.width / 2 - 2 * (padding + margin), height - 2 * margin), "Place Tower")) {
				myNetworkHelper.buildTowerPoint (lastLat, lastLng, lastAlt);
			}

			if (myNetworkHelper.isTowerSet ()) {
			 	y += height;
				if (GUI.Button (new Rect (leftmod + padding + margin, padding + margin + y, Screen.width / 2 - 2 * (padding + margin), height - 2 * margin), "Upload Tower")) {
					myNetworkHelper.uploadTowerPoint (worldName, worldPassword, playerName, playerPassword,genericCallback);
				}
			}

			y += height;
			y += padding;
			GUI.Box (new Rect (leftmod, y, Screen.width / 2, padding), "");
			y += padding;


			if (GUI.Button (new Rect (leftmod + padding + margin, padding + margin + y, Screen.width / 2 - 2 * (padding + margin), height - 2 * margin), "Place Bomb")) {
				myNetworkHelper.dropBombPoint (lastLat, lastLng, lastAlt);
			}

			if (myNetworkHelper.isBombSet ()) {
				y += height;
				if (GUI.Button (new Rect (leftmod + padding + margin, padding + margin + y, Screen.width / 2 - 2 * (padding + margin), height - 2 * margin), "Upload Bomb")) {
					myNetworkHelper.uploadBombPoint (worldName, worldPassword, playerName, playerPassword,genericCallback);
				}
			}

			y += height;
			y += padding;
			GUI.Box (new Rect (leftmod, y, Screen.width / 2, padding), "");
			y += padding;

			if (GUI.Button (new Rect (leftmod + padding + margin, padding + margin + y, Screen.width / 2 - 2 * (padding + margin), height - 2 * margin), "Upload Code")) {
				myNetworkHelper.uploadCode(worldName,worldPassword,playerName,playerPassword,secretcode,genericCallback);
			}

			y += height;
			y += padding;
			GUI.Box (new Rect (leftmod, y, Screen.width / 2, padding), "");
			y += padding;
			
			if (GUI.Button (new Rect (leftmod + padding + margin, padding + margin + y, Screen.width / 2 - 2 * (padding + margin), height - 2 * margin),  "Leader Board")) {
				myNetworkHelper.requestLeaderBoard(worldName,worldPassword,leaderBoardCallback);
			}
		}

	}



	/** This is called by Unity in the draw loop and we use it to update the
	 * camera to correspond with the physical position */
	void Update() {
		if (firstPersonShooter) {
			if(myMesh != null){
				double[] thing = MyLocation.Haversine.ComputeLatLng2(originY,originX,camera.transform.position.x,camera.transform.position.z);
				lastLng = (float)thing[1];
				lastLat = (float)thing[0];
				lastAlt = 20.0f;
				//Debug.Log("lastLng"+lastLng);
			}

		} else {
			if ((lastLng != myLocation.getLng ()) || (lastLat != myLocation.getLat ()) || (lastAlt != myLocation.getAlt ())) {
				lastLng = myLocation.getLng ();
				lastLat = myLocation.getLat ();
				lastAlt = myLocation.getAlt ();
				if (myMesh != null) {
					double xdelta = MyLocation.Haversine.calculate (originY, originX, 0.0, originY, lastLng, 0.0);
					if (lastLng < originX) {
						xdelta = -xdelta;
					}
					double ydelta = MyLocation.Haversine.calculate (originY, originX, 0.0, lastLat, originX, 0.0);
					if (lastLat < originY) {
						ydelta = -ydelta;
					}
					destination = new Vector3 ((float)xdelta, lastAlt + 20.0f, (float)ydelta);
					origin = new Vector3 (camera.transform.position.x, camera.transform.position.y, camera.transform.position.z);
					fracJourney = 0.0f;
				}

			} else {
				if (fracJourney <= 1.0f) {
					fracJourney += 0.001f;
					camera.transform.position = Vector3.Lerp (origin, destination, fracJourney);
				}
			}
		}
	}


}

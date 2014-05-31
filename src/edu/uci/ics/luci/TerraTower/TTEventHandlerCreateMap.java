package edu.uci.ics.luci.TerraTower;


public class TTEventHandlerCreateMap {

	public static void onEvent(TTEventCreateMap event)  {   
		Map map = new Map(event.getLeft(),
							event.getRight(),
							event.getNumXSplits(),
							event.getBottom(),
							event.getTop(),
							event.getNumYSplits());
	}
}

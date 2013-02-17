package com.ramshteks.nimble.server;

import java.util.Hashtable;
import java.util.LinkedList;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class ServerUtils {
	public static class IDRange{
		private LinkedList<Integer> freeIDs;
		private LinkedList<Integer> allocated;
		private Hashtable<Integer, Boolean> idToStatus;

		private int min;
		private int max;

		public IDRange(int min, int max){
			this.min = min;
			this.max = max;
			idToStatus = new Hashtable<Integer, Boolean>();

			for(int i = min; i < max; i++){
				idToStatus.put(i, false);
				//freeIDs.push(i);
			}
		}

		public synchronized int allocateNextID(){
			return min++;//SHIT
		}

		public synchronized void freeID(int id){

		}
	}
}

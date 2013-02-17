package com.ramshteks.nimble.server;

import java.util.Hashtable;
import java.util.LinkedList;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class ServerUtils {
	public static class IDGenerator {

		private LinkedList<Integer> freeIDList;
		private Hashtable<Integer, Boolean> idToStatus;

		public IDGenerator(int min, int max){
			idToStatus = new Hashtable<Integer, Boolean>();
			freeIDList = new LinkedList<Integer>();

			for(int id = min; id < max; id++){
				updateAllocateStatus(id, false);
				freeIDList.add(id);
			}
		}

		public synchronized int nextID(){
			if(freeIDList.isEmpty()){
				throw new IndexOutOfBoundsException("No more free id.");
			}

			int id = freeIDList.pollFirst();
			updateAllocateStatus(id, true);

			return id;
		}

		public synchronized void free(int id){
			if(freeIDList.contains(id)){
				throw new RuntimeException("This id="+id+" already free");
			}

			if(!idToStatus.containsKey(id)){
				throw new RuntimeException("This id="+id+" out of bounds");
			}

			freeIDList.addLast(id);
			updateAllocateStatus(id, false);
		}

		private void updateAllocateStatus(int id, boolean allocated){
			if(idToStatus.contains(id)){
				idToStatus.remove(id);
			}
			idToStatus.put(id, allocated);
		}
	}
}

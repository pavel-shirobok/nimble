/*
 * Copyright (c) 2013, Shirobok Pavel (ramshteks@gmail.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ramshteks.nimble;

import com.ramshteks.nimble.plugins.net.IDGenerator;

import java.util.HashMap;

public class Event{
	public static enum Priority{
		HIGH, COMMON
	}

	private static HashMap<String, Integer> typeToIntegerIdentifier;
	private static IDGenerator idGenerator;

	static {
		typeToIntegerIdentifier = new HashMap<>();
		idGenerator = new IDGenerator(0, 1000);
	}

	public static int getHashCodeOfEventType(String eventType){
		return getOrCreateHashCode(eventType);
	}

	public static int getOrCreateHashCode(String eventType){
		if(!typeToIntegerIdentifier.containsKey(eventType)){
			typeToIntegerIdentifier.put(eventType, idGenerator.nextID());
		}
		return typeToIntegerIdentifier.get(eventType);
	}

	public static boolean equalHash(Event event, String eventType){
		return event.eventTypeHashCode() == getHashCodeOfEventType(eventType);
	}

	private String eventType;
	private int hashCode;
	private Priority priority = Priority.COMMON;

	public Event(String eventType){
		this.eventType = eventType;
		hashCode = getOrCreateHashCode(eventType);
	}

	public final Priority priority(){
		return priority;
	}

	public void setHighPriority(){
		priority = Priority.HIGH;
	}

	@SuppressWarnings("UnusedDeclaration")
	public final String eventType() {
		return eventType;
	}

	public final int eventTypeHashCode(){
		return hashCode;
	}
}
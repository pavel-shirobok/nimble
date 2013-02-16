/*
 * Copyright (C) 2011 Shirobok Pavel
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation,
 * Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * Contact information:
 * email: ramshteks@gmail.com
 */

package com.ramshteks.nimble.core;

/**
 * Enum for error's description
 */
public enum SDError {
	ACCEPT_SECURITY("security manager exists and its checkAccept method doesn't allow the operation"),
	ACCEPT_ILLEGAL_BLOCKING("this socket has an associated channel, the channel is in non-blocking mode, and there is no connection ready to be accepted"),
	ACCEPT_IO("I/O error occurs when waiting for a connection"),
	ACCEPT_TIMEOUT("timeout was previously set with setSoTimeout and the timeout has been reached"),
	ACCEPT_RECV_NULL_SOCKET("After accept socket = null")/*,
	CONNECTION_AVAILABLE("Catch IOError when try get available bytes count in Connection.reading()"),
	CONNECTION_READ("Catch IOError when try read data from input stream in Connection.reading()"),
	CONNECTION_WRITE("Catch IOError when try write data in output stream in Connection.writing()"),
	CONNECTION_RECEIVED_NULL_PACKET("Received nulled IPacket object")*/;

	private String description;

	SDError(String description) {
		this.description = description;
	}


	@Override
	public String toString() {
		return this.name() + " \"" + description + "\"";
	}
}
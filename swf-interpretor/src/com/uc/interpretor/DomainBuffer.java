package com.uc.interpretor;

import java.util.HashMap;

public class DomainBuffer {
	private int offset;
	private byte[] buffer;
	private HashMap<Integer, DomainBuffer> bufferMap;

	DomainBuffer(int size) {
		offset = 0;
		buffer = new byte[size + 1];
		bufferMap = new HashMap<Integer, DomainBuffer>();
	}

	private DomainBuffer(int offset, DomainBuffer other) {
		this.offset = offset;
		this.buffer = other.buffer;
		this.bufferMap = other.bufferMap;
	}

	public DomainBuffer add(int offset) {
		return new DomainBuffer(this.offset + offset, this);
	}

	public DomainBuffer sub(int offset) {
		return new DomainBuffer(this.offset - offset, this);
	}

	public DomainBuffer and(int offset) {
		return new DomainBuffer(this.offset & offset, this);
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public int getOffset() {
		return offset;
	}

	void writeBytes(byte[] bytes) {
		if (bytes.length > this.buffer.length) {
			throw new IllegalArgumentException(
					"Should never go beyone this.allocate_start");
		}
		System.arraycopy(bytes, 0, this.buffer, 0, bytes.length);
	}

	Object loadAppDomain(int what) {
		switch (what) {
		case 8:
			return buffer[offset];
		case 32:
			// fist find in the hash map
			DomainBuffer found = this.bufferMap.get(offset);
			if(found != null)
				return found;
			// always little endian
			int i0 = buffer[offset] & 0xff;
			int i1 = buffer[offset + 1] & 0xff;
			int i2 = buffer[offset + 2] & 0xff;
			int i3 = buffer[offset + 3] & 0xff;
			int ret = 0;
			ret |= i0;
			ret |= i1 << 8;
			ret |= i2 << 16;
			ret |= i3 << 24;
			return ret;
		}
		throw new IllegalStateException("loadAppDomain fails to serve!");
	}

	void setAppDomain(Object value, int what) {
		switch (what) {
		case 8:
			if (value instanceof Integer)
				buffer[offset] = (byte) ((Integer) value & 0xff);
			else if (value instanceof Byte)
				buffer[offset] = (byte) value;
			else
				throw new IllegalStateException(
						"setAppDomain with what 8 should pass an integer value");
			return;
		case 32:
			if(value instanceof DomainBuffer) {
				this.bufferMap.put(offset, (DomainBuffer)value);
				return;
			}
			// always little endian
			int ivalue = (Integer) value;
			byte i0 = (byte) (ivalue & 0xff);
			byte i1 = (byte) ((ivalue & 0xff00) >> 8);
			byte i2 = (byte) ((ivalue & 0xff0000) >> 16);
			byte i3 = (byte) ((ivalue & 0xff000000) >> 24);
			buffer[offset] = i0;
			buffer[offset + 1] = i1;
			buffer[offset + 2] = i2;
			buffer[offset + 3] = i3;
			return;
		}
		throw new IllegalStateException("setAppDomain fails to serve!");
	}
}

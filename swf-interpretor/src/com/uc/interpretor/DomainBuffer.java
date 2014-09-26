package com.uc.interpretor;

import java.util.HashMap;

public class DomainBuffer {
	private final int offset;
	private final byte[] buffer;
	private final HashMap<Integer, DomainBuffer> bufferMap;

	DomainBuffer(int size) {
		offset = 0;
		buffer = new byte[size + 1];
		bufferMap = new HashMap<Integer, DomainBuffer>();
	}

	@Override
	public String toString() {
		return "DomainBuffer: offset = " + offset + ", buffer.length = " + buffer.length;
	}

	private DomainBuffer(int offset, DomainBuffer other) {
		this.offset = offset;
		this.buffer = other.buffer;
		this.bufferMap = other.bufferMap;
	}

	public DomainBuffer add(int offset) {
		return new DomainBuffer(this.offset + offset, this);
	}

	public DomainBuffer add(Object offset) {
		if (offset instanceof Integer)
			return new DomainBuffer(this.offset + (Integer)offset, this);
		else if (offset instanceof DomainBuffer) {
			DomainBuffer other = (DomainBuffer) offset;
			if (!this.isCompatible(other))
				throw new IllegalStateException("other should always be compatible with this.");
			return new DomainBuffer(this.offset + other.offset, this);
		} else {
			throw new IllegalArgumentException("the type of offset should be either DomainBuffer or Integer");
		}
	}

	public DomainBuffer sub(int offset) {
		return new DomainBuffer(this.offset - offset, this);
	}

	public DomainBuffer rsb(Object offset) {
		if (offset instanceof Integer)
			return new DomainBuffer((Integer)offset - this.offset, this);
		else if (offset instanceof DomainBuffer) {
			DomainBuffer other = (DomainBuffer) offset;
			if (!this.isCompatible(other))
				throw new IllegalStateException("other should always be compatible with this.");
			return new DomainBuffer(other.offset - this.offset, this);
		} else {
			throw new IllegalArgumentException("the type of offset should be either DomainBuffer or Integer");
		}
	}

	public DomainBuffer and(int offset) {
		return new DomainBuffer(this.offset & offset, this);
	}

	public DomainBuffer or(int offset) {
		return new DomainBuffer(this.offset | offset, this);
	}

	public boolean isCompatible(DomainBuffer other) {
		if (other.buffer != this.buffer)
			return false;
		return true;
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
					"Should never go beyond this.allocate_start");
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
			this.bufferMap.remove(offset);
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

package kr.co.ibk.common.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class BlobTransport implements Blob {

	private Object source;

	private BlobTransport(Object source) {
		this.source = source;
	}

	public Object source() {
		return this.source;
	}

	public long length() throws SQLException {
		return blob().length();
	}

	public byte[] getBytes(long pos, int length) throws SQLException {
		return blob().getBytes(pos, length);
	}

	public InputStream getBinaryStream() throws SQLException {
		return blob().getBinaryStream();
	}

	public long position(byte[] pattern, long start) throws SQLException {
		return blob().position(pattern, start);
	}

	public long position(Blob pattern, long start) throws SQLException {
		return blob().position(pattern, start);
	}

	public int setBytes(long pos, byte[] bytes) throws SQLException {
		return blob().setBytes(pos, bytes);
	}

	public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
		return blob().setBytes(pos, bytes, offset, len);
	}

	public OutputStream setBinaryStream(long pos) throws SQLException {
		return blob().setBinaryStream(pos);
	}

	public void truncate(long len) throws SQLException {
		blob().truncate(len);
	}

	public void free() throws SQLException {
		blob().free();
	}

	public InputStream getBinaryStream(long pos, long length) throws SQLException {
		return blob().getBinaryStream(pos, length);
	}

	private Blob blob() {
		if (!(this.source instanceof Blob)) {
			throw new UnsupportedOperationException();
		}
		return (Blob)this.source;
	}

	public static Blob wrap(Object source) {
		if ((source instanceof BlobTransport)) {
			return (Blob)source;
		}
		return new BlobTransport(source);
	}

}

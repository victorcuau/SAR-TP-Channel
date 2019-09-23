
public class Channel {

	public byte			buffer[];		// Buffer in which the process have to read
	public int			read_head;	// Index of the next byte to read
	public int			write_head;	// Index of the next byte to write
	public int			bufSize;		// Maximum number of bytes store in the buffer
	public int			nbElem;			// Number of bytes currently store in the buffer
	public boolean	close;			// True if the buffer is close (can't write more)
	public Process	p;					// The Process on the other side
	public int			port;				// The port used on the other side

	protected Channel(Process p, int port) {
		this.read_head = 0;
		this.write_head = 0;
		this.bufSize = 10;
		this.nbElem = 0;
		this.close = false;
		buffer = new byte[bufSize];
		this.p = p;
		this.port = port;
	}

	
	/*
	 * Reads at most "length" bytes, in the given byte array, starting at the given
	 * offset. Blocks if there are no available bytes. Otherwise, returns the number
	 * of bytes read.
	 */
	public synchronized int read(byte[] bytes, int offset, int length) throws InterruptedException {
		while (!(this.nbElem > 0)) {
			wait();
		}

		int counter = 0;
		while (counter < length) {
			
			byte msg = this.buffer[this.read_head++];
			bytes[offset++] = msg;
			System.out.println("READ : " + msg);
			
			counter++;
			this.nbElem--;
			synchronized (this.p) {
				this.p.notifyAll();
			}

			while (!(this.nbElem > 0)) {
				wait();
			}
		}

		return counter;
	}

	
	/*
	 * Writes at most "length" bytes, from the given byte array, starting at the
	 * given offset. Blocks if there is no room to write available bytes. Otherwise,
	 * returns the number of bytes written. Returns -1 if the channel is closed.
	 */
	public synchronized int write(byte[] bytes, int offset, int length) throws InterruptedException {
		// If the Channel where we want to write has been closed on the other side
		if (this.p.list.get(this.port).close) {
			return -1;
		}
		
		while (!(this.p.list.get(this.port).nbElem < this.p.list.get(this.port).bufSize)) {
			wait();
		}

		int counter = 0;
		while (counter < length) {
			
			byte msg = bytes[offset++];
			this.p.list.get(this.port).buffer[this.p.list.get(this.port).write_head++] = msg;
			System.out.println("WRITE : " + msg);
			
			counter++;
			this.p.list.get(this.port).nbElem++;
			synchronized (this.p) {
				this.p.notifyAll();
			}

			while (!(this.p.list.get(this.port).nbElem < this.p.list.get(this.port).bufSize)) {
				wait();
			}
		}

		return counter;
	}

	
	/*
	 * Closes this channel. Once closed, writes are forbidden, but reads are allowed
	 * as long as there are remaining bytes available. The closed status will
	 * propagate to the other side, forbidding the other side to write, but still
	 * allowing it to write.
	 */
	public void close() {
		this.close = true;
	}
}

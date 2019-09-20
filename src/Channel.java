
public class Channel {

	public byte			buffer[];		// Buffer in which the process have to read
	public int			read_head;	// Index of the next byte to read
	public int			write_head;	// Index of the next byte to write
	public int			bufSize;		// Maximum number of bytes store in the buffer
	public int			nbElem;			// Number of bytes currently store in the buffer
	public boolean	close;			// True if the buffer is close (can't write more)

	protected Channel() {
		this.read_head = 0;
		this.write_head = 0;
		this.bufSize = 10;
		this.nbElem = 0;
		this.close = false;
		buffer = new byte[nbElem];
	}

	
	/*
	 * Reads at most "length" bytes, in the given byte array, starting at the given
	 * offset. Blocks if there are no available bytes. Otherwise, returns the number
	 * of bytes read. Returns -1 if the channel is closed. ---> On peut lire même si
	 * la Channel est fermée, donc pourquoi ?
	 */
	public synchronized int read(byte[] bytes, int offset, int length) throws InterruptedException {
		while (!(this.nbElem > 0)) {
			wait();
		}

		int counter = 0;
		while (counter < length) {
			bytes[offset++] = this.buffer[this.read_head++];
			counter++;
			this.nbElem--;
			notifyAll();

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
	// TODO : Ajouter vérification du close sur le buffer d'en face
		while (!(this.nbElem < this.bufSize)) {
			wait();
		}

		int counter = 0;
		while (counter < length) {
			// TODO : Ecrire dans le buffer d'en face
			counter++;
			this.nbElem++;
			notifyAll();

			while (!(this.nbElem < this.bufSize)) {
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

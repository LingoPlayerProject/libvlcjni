package org.videolan.libvlc.interfaces;

public interface IVLCMediaSource {

    public interface OpendSource {
        long length();

        /**
         * read data from a custom bitstream input media.
         * @return strictly positive number of bytes read, 0 on end-of-stream,
         *          or -1 on non-recoverable error
         */
        int read(byte[] buf, int len);

        /**
         * seek a custom bitstream input media.
         * @param offset absolute byte offset to seek to
         * @return 0 on success, -1 on error.
         */
        int seek(long offset)

        void close();
    }

    OpendSource open();
}
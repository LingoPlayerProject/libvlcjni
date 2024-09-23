package org.videolan.libvlc.interfaces;

import java.io.IOException;

public interface IVLCMediaSource {

    public interface OpenedSource {
        /**
         * Total length of the file
         */
        long length() throws IOException;

        /**
         * read data from a custom bitstream input media.
         * @return strictly positive number of bytes read, 0 on end-of-stream
         */
        int read(byte[] buf, int len) throws IOException;

        /**
         * seek a custom bitstream input media.
         * @param offset absolute byte offset to seek to
         */
        void seek(long offset) throws IOException;

        void close();
    }

    /**
     * @return OpenedSource, libvlc will take care of the close of OpenedSource
     */
    OpenedSource open() throws IOException;
}
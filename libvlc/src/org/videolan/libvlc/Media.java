/*****************************************************************************
 * Media.java
 *****************************************************************************
 * Copyright © 2015 VLC authors, VideoLAN and VideoLabs
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/

package org.videolan.libvlc;

import android.net.Uri;

import java.io.FileDescriptor;

public class Media extends VLCObject {
    private final static String TAG = "LibVLC/Media";

    /**
     * libvlc_media_type_t
     */
    public static class Type {
        public static final int Unknown = 0;
        public static final int File = 1;
        public static final int Directory = 2;
        public static final int Disc = 3;
        public static final int Stream = 4;
        public static final int Playlist = 5;
    }

    /**
     * see libvlc_meta_t
     */
    public static class Meta {
        public static final int Title = 0;
        public static final int Artist = 1;
        public static final int Genre = 2;
        public static final int Copyright = 3;
        public static final int Album = 4;
        public static final int TrackNumber = 5;
        public static final int Description = 6;
        public static final int Rating = 7;
        public static final int Date = 8;
        public static final int Setting = 9;
        public static final int URL = 10;
        public static final int Language = 11;
        public static final int NowPlaying = 12;
        public static final int Publisher = 13;
        public static final int EncodedBy = 14;
        public static final int ArtworkURL = 15;
        public static final int TrackID = 16;
        public static final int TrackTotal = 17;
        public static final int Director = 18;
        public static final int Season = 19;
        public static final int Episode = 20;
        public static final int ShowName = 21;
        public static final int Actors = 22;
        public static final int AlbumArtist = 23;
        public static final int DiscNumber = 24;
        public static final int MAX = 25;
    }

    /**
     * see libvlc_state_t
     */
    public static class State {
        public static final int NothingSpecial = 0;
        public static final int Opening = 1;
        public static final int Buffering = 2;
        public static final int Playing = 3;
        public static final int Paused = 4;
        public static final int Stopped = 5;
        public static final int Ended = 6;
        public static final int Error = 7;
        public static final int MAX = 8;
    }

    /**
     * see libvlc_media_parse_flag_t
     */
    public static class Parse {
        public static final int ParseLocal   = 0;
        public static final int ParseNetwork = 0x01;
        public static final int FetchLocal   = 0x02;
        public static final int FetchNetwork = 0x04;
    }

    /**
     * see libvlc_media_track_t
     */
    public static abstract class Track {
        public static class Type {
            public static final int Unknown = -1;
            public static final int Audio = 0;
            public static final int Video = 1;
            public static final int Text = 2;
        }

        public final int type;
        public final String codec;
        public final String originalCodec;
        public final int id;
        public final int profile;
        public final int level;
        public final int bitrate;
        public final String language;
        public final String description;

        private Track(int type, String codec, String originalCodec, int id, int profile,
                int level, int bitrate, String language, String description) {
            this.type = type;
            this.codec = codec;
            this.originalCodec = originalCodec;
            this.id = id;
            this.profile = profile;
            this.level = level;
            this.bitrate = bitrate;
            this.language = language;
            this.description = description;
        }
    }

    /**
     * see libvlc_audio_track_t
     */
    public static class AudioTrack extends Track {
        public final int channels;
        public final int rate;

        private AudioTrack(String codec, String originalCodec, int id, int profile,
                int level, int bitrate, String language, String description,
                int channels, int rate) {
            super(Type.Audio, codec, originalCodec, id, profile, level, bitrate, language, description);
            this.channels = channels;
            this.rate = rate;
        }
    }

    /* Used from JNI */
    private static Track createAudioTrackFromNative(String codec, String originalCodec, int id, int profile,
            int level, int bitrate, String language, String description,
            int channels, int rate) {
        return new AudioTrack(codec, originalCodec, id, profile,
                level, bitrate, language, description,
                channels, rate);
    }

    /**
     * see libvlc_video_track_t
     */
    public static class VideoTrack extends Track {
        public final int height;
        public final int width;
        public final int sarNum;
        public final int sarDen;
        public final int frameRateNum;
        public final int frameRateDen;

        private VideoTrack(String codec, String originalCodec, int id, int profile,
                int level, int bitrate, String language, String description,
                int height, int width, int sarNum, int sarDen, int frameRateNum, int frameRateDen) {
            super(Type.Video, codec, originalCodec, id, profile, level, bitrate, language, description);
            this.height = height;
            this.width = width;
            this.sarNum = sarNum;
            this.sarDen = sarDen;
            this.frameRateNum = frameRateNum;
            this.frameRateDen = frameRateDen;
        }
    }

    /* Used from JNI */
    private static Track createVideoTrackFromNative(String codec, String originalCodec, int id, int profile,
            int level, int bitrate, String language, String description,
            int height, int width, int sarNum, int sarDen, int frameRateNum, int frameRateDen) {
        return new VideoTrack(codec, originalCodec, id, profile,
                level, bitrate, language, description,
                height, width, sarNum, sarDen, frameRateNum, frameRateDen);
    }

    /**
     * see libvlc_subtitle_track_t
     */
    public static class SubtitleTrack extends Track {
        public final String encoding;

        private SubtitleTrack(String codec, String originalCodec, int id, int profile,
                int level, int bitrate, String language, String description,
                String encoding) {
            super(Type.Text, codec, originalCodec, id, profile, level, bitrate, language, description);
            this.encoding = encoding;
        }
    }

    /* Used from JNI */
    private static Track createSubtitleTrackFromNative(String codec, String originalCodec, int id, int profile,
            int level, int bitrate, String language, String description,
            String encoding) {
        return new SubtitleTrack(codec, originalCodec, id, profile,
                level, bitrate, language, description,
                encoding);
    }

    private static final int PARSE_STATUS_INIT = 0x00;
    private static final int PARSE_STATUS_PARSING = 0x01;
    private static final int PARSE_STATUS_PARSED = 0x02;

    private String mMrl = null;
    private MediaList mSubItems = null;
    private int mParseStatus = PARSE_STATUS_INIT;
    private String mNativeMetas[] = null;
    private Track mNativeTracks[] = null;
    private long mDuration;
    private int mState = State.NothingSpecial;
    private int mType = Type.Unknown;

    /**
     * Create a Media from libVLC and a local path starting with '/'.
     *
     * @param libVLC
     * @param path
     */
    public Media(LibVLC libVLC, String path) {
        nativeNewFromPath(libVLC, path);
        mMrl = nativeGetMrl();
        mType = nativeGetType();
    }

    /**
     * Create a Media from libVLC and a Uri
     *
     * @param libVLC
     * @param uri
     */
    public Media(LibVLC libVLC, Uri uri) {
        nativeNewFromLocation(libVLC, uri.toString());
        mMrl = nativeGetMrl();
        mType = nativeGetType();
    }

    /**
     * Create a Media from libVLC and a FileDescriptor
     *
     * @param libVLC
     * @param fd
     */
    public Media(LibVLC libVLC, FileDescriptor fd) {
        nativeNewFromFD(libVLC, fd);
        mMrl = nativeGetMrl();
        mType = nativeGetType();
    }

    /**
     *
     * @param ml Should not be released
     * @param index
     */
    protected Media(MediaList ml, int index) {
        if (ml == null || ml.isReleased())
            throw new IllegalArgumentException("MediaList is null or released");
        nativeNewFromMediaList(ml, index);
        mMrl = nativeGetMrl();
        mNativeMetas = nativeGetMetas();
        mType = nativeGetType();
    }

    @Override
    protected synchronized Event onEventNative(int eventType, long arg1, long arg2) {
        switch (eventType) {
        case VLCObject.Events.MediaMetaChanged:
            // either we update all metas (if first call) or we update a specific meta
            if (mNativeMetas == null) {
                mNativeMetas = nativeGetMetas();
            } else {
                int id = (int) arg1;
                if (id >= 0 && id < Meta.MAX)
                    mNativeMetas[id] = nativeGetMeta(id);
            }
            break;
        case VLCObject.Events.MediaDurationChanged:
            mDuration = nativeGetDuration();
            break;
        case VLCObject.Events.MediaParsedChanged:
            postParse();
            break;
        case VLCObject.Events.MediaStateChanged:
            mState = nativeGetState();
            break;
        }
        return new Event(eventType);
    }

    /**
     * Get the MRL associated with the Media.
     */
    public synchronized String getMrl() {
        return mMrl;
    }

    /**
     * Get the duration of the media.
     */
    public synchronized long getDuration() {
        return mDuration;
    }

    /**
     * Get the state of the media.
     *
     * @see State
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     * Get the subItems MediaList associated with the Media. This Media should be alive (not released).
     *
     * @return subItems as a MediaList. This MediaList should be released with {@link #release()}.
     */
    public synchronized MediaList subItems() {
        if (isReleased())
            throw new IllegalStateException("Media is released");
        if (mSubItems == null)
            mSubItems = new MediaList(this);
        mSubItems.retain();
        return mSubItems;
    }

    private synchronized void postParse() {
        if (isReleased())
            throw new IllegalStateException("Media is released");
        // fetch if parsed and not fetched
        if ((mParseStatus & PARSE_STATUS_PARSING) != 0
                && (mParseStatus & PARSE_STATUS_PARSED) == 0) {
            mParseStatus &= ~PARSE_STATUS_PARSING;
            mParseStatus |= PARSE_STATUS_PARSED;
            mNativeTracks = nativeGetTracks();
            mNativeMetas = nativeGetMetas();
            if (mNativeMetas != null && mNativeMetas.length != Meta.MAX)
                throw new IllegalStateException("native metas size doesn't match");
            mDuration = nativeGetDuration();
            mState = nativeGetState();
            mType = nativeGetType();
        }
    }

    /**
     * Parse the media synchronously with a flag. This Media should be alive (not released).
     *
     * @param flags see {@link Parse}
     * @return true in case of success, false otherwise.
     */
    public synchronized boolean parse(int flags) {
        if ((mParseStatus & (PARSE_STATUS_PARSED|PARSE_STATUS_PARSING)) == 0) {
            mParseStatus |= PARSE_STATUS_PARSING;
            if (nativeParse(flags)) {
                postParse();
                return true;
            }
        }
        return false;
    }

    /**
     * Parse the media and local art synchronously. This Media should be alive (not released).
     *
     * @return true in case of success, false otherwise.
     */
    public synchronized boolean parse() {
        return parse(Parse.FetchLocal);
    }

    /**
     * Parse the media asynchronously with a flag. This Media should be alive (not released).
     *
     * To track when this is over you can listen to {@link VLCObject.Events#MediaParsedChanged}
     * event (only if this methods returned true).
     *
     * @param flags see {@link Parse}
     * @return true in case of success, false otherwise.
     */
    public synchronized boolean parseAsync(int flags) {
        if ((mParseStatus & (PARSE_STATUS_PARSED|PARSE_STATUS_PARSING)) == 0) {
            mParseStatus |= PARSE_STATUS_PARSING;
            return nativeParseAsync(flags);
        } else
            return false;
    }

    /**
     * Parse the media and local art asynchronously. This Media should be alive (not released).
     *
     * @see #parseAsync(int)
     */
    public synchronized boolean parseAsync() {
        return parseAsync(Parse.FetchLocal);
    }

    /**
     * Returns true if the media is parsed This Media should be alive (not released).
     */
    public synchronized boolean isParsed() {
        return (mParseStatus & PARSE_STATUS_PARSED) != 0;
    }

    /**
     * Get the type of the media
     *
     * @see {@link Type}
     */
    public synchronized int getType() {
        return mType;
    }

    /**
     * Get the Track count.
     */
    public synchronized int getTrackCount() {
        return mNativeTracks != null ? mNativeTracks.length : 0;
    }

    /**
     * Get a Track
     * The Track can be casted to {@link AudioTrack}, {@link VideoTrack} or {@link SubtitleTrack} in function of the {@link Track.Type}.
     *
     * @param idx
     * @return Track or null if not idx is not valid
     * @see #getTrackCount()
     */
    public synchronized Track getTrack(int idx) {
        if (mNativeTracks == null || idx < 0 || idx >= mNativeTracks.length)
            return null;
        return mNativeTracks[idx];
    }

    /**
     * Get a Meta.
     *
     * @param id see {@link Meta}
     * @return meta or null if not found
     */
    public synchronized String getMeta(int id) {
        if (id < 0 || id >= Meta.MAX)
            return null;

        return mNativeMetas != null ? mNativeMetas[id] : null;
    }

    /**
     * Add an option to this Media. This Media should be alive (not released).
     *
     * @param option ":option" or ":option=value"
     */
    public synchronized void addOption(String option) {
        nativeAddOption(option);
    }

    @Override
    protected void onReleaseNative() {
        if (mSubItems != null)
            mSubItems.release();
        nativeRelease();
    }

    /* JNI */
    private native void nativeNewFromPath(LibVLC libVLC, String path);
    private native void nativeNewFromLocation(LibVLC libVLC, String location);
    private native void nativeNewFromFD(LibVLC libVLC, FileDescriptor fd);
    private native void nativeNewFromMediaList(MediaList ml, int index);
    private native void nativeRelease();
    private native boolean nativeParseAsync(int flags);
    private native boolean nativeParse(int flags);
    private native String nativeGetMrl();
    private native int nativeGetState();
    private native String nativeGetMeta(int id);
    private native String[] nativeGetMetas();
    private native Track[] nativeGetTracks();
    private native long nativeGetDuration();
    private native int nativeGetType();
    private native void nativeAddOption(String option);
}
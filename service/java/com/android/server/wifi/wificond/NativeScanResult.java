/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.wifi.wificond;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.BitSet;

/**
 * ScanResult from wificond
 *
 * @hide
 */
public class NativeScanResult implements Parcelable {
    private static final int CAPABILITY_SIZE = 16;

    public byte[] ssid;
    public byte[] bssid;
    public byte[] infoElement;
    public int frequency;
    public int signalMbm;
    public long tsf;
    public BitSet capability;
    public boolean associated;

    /** public constructor */
    public NativeScanResult() { }

    /** copy constructor */
    public NativeScanResult(NativeScanResult source) {
        ssid = source.ssid.clone();
        bssid = source.bssid.clone();
        infoElement = source.infoElement.clone();
        frequency = source.frequency;
        signalMbm = source.signalMbm;
        tsf = source.tsf;
        capability = (BitSet) source.capability.clone();
        associated = source.associated;
    }

    /** implement Parcelable interface */
    @Override
    public int describeContents() {
        return 0;
    }

    /** implement Parcelable interface */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(ssid.length);
        out.writeByteArray(ssid);
        out.writeInt(bssid.length);
        out.writeByteArray(bssid);
        out.writeInt(infoElement.length);
        out.writeByteArray(infoElement);
        out.writeInt(frequency);
        out.writeInt(signalMbm);
        out.writeLong(tsf);
        int capabilityInt = 0;
        for (int i = 0; i < CAPABILITY_SIZE; i++) {
            if (capability.get(i)) {
                capabilityInt |= 1 << i;
            }
        }
        out.writeInt(capabilityInt);
        out.writeInt(associated ? 1 : 0);
    }

    /** implement Parcelable interface */
    public static final Parcelable.Creator<NativeScanResult> CREATOR =
            new Parcelable.Creator<NativeScanResult>() {
        @Override
        public NativeScanResult createFromParcel(Parcel in) {
            NativeScanResult result = new NativeScanResult();
            result.ssid = new byte[in.readInt()];
            in.readByteArray(result.ssid);
            result.bssid = new byte[in.readInt()];
            in.readByteArray(result.bssid);
            result.infoElement = new byte[in.readInt()];
            in.readByteArray(result.infoElement);
            result.frequency = in.readInt();
            result.signalMbm = in.readInt();
            result.tsf = in.readLong();
            int capabilityInt = in.readInt();
            result.capability = new BitSet(CAPABILITY_SIZE);
            for (int i = 0; i < CAPABILITY_SIZE; i++) {
                if ((capabilityInt & (1 << i)) != 0) {
                    result.capability.set(i);
                }
            }
            result.associated = (in.readInt() != 0);
            return result;
        }

        @Override
        public NativeScanResult[] newArray(int size) {
            return new NativeScanResult[size];
        }
    };
}
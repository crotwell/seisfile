package edu.sc.seis.seisFile.mseed;

/**
 * DataRecord.java
 * 
 * 
 * Created: Thu Apr 8 13:52:27 1999
 * 
 * @author Philip Crotwell
 * @version
 */
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import edu.iris.dmc.seedcodec.Codec;
import edu.iris.dmc.seedcodec.CodecException;
import edu.iris.dmc.seedcodec.DecompressedData;
import edu.iris.dmc.seedcodec.UnsupportedCompressionType;

public class DataRecord extends SeedRecord implements Serializable {

    public DataRecord(DataHeader header) {
        super(header);
    }

    public DataRecord(DataRecord record) {
        super(new DataHeader(record.getHeader().getSequenceNum(),
                             record.getHeader().getTypeCode(),
                             record.getHeader().isContinuation()));
        RECORD_SIZE = record.RECORD_SIZE;
        getHeader().setActivityFlags(record.getHeader().getActivityFlags());
        getHeader().setChannelIdentifier(record.getHeader().getChannelIdentifier());
        getHeader().setDataBlocketteOffset((short)record.getHeader().getDataBlocketteOffset());
        getHeader().setDataOffset((short)record.getHeader().getDataOffset());
        getHeader().setDataQualityFlags(record.getHeader().getDataQualityFlags());
        getHeader().setIOClockFlags(record.getHeader().getIOClockFlags());
        getHeader().setLocationIdentifier(record.getHeader().getLocationIdentifier());
        getHeader().setNetworkCode(record.getHeader().getNetworkCode());
        getHeader().setNumSamples((short)record.getHeader().getNumSamples());
        getHeader().setSampleRateFactor((short)record.getHeader().getSampleRateFactor());
        getHeader().setSampleRateMultiplier((short)record.getHeader().getSampleRateMultiplier());
        getHeader().setStartBtime(record.getHeader().getStartBtime());
        getHeader().setStationIdentifier(record.getHeader().getStationIdentifier());
        getHeader().setTimeCorrection(record.getHeader().getTimeCorrection());
        try {
            setData(record.getData());
            for (int j = 0; j < record.getBlockettes().length; j++) {
                blockettes.add(record.getBlockettes()[j]);
            }
        } catch(SeedFormatException e) {
            throw new RuntimeException("Shouldn't happen as record was valid and we are copying it", e);
        }
    }

    /**
     * Adds a blockette to the record. The number of blockettes in the header is
     * incremented automatically.
     */
    public void addBlockette(Blockette b) throws SeedFormatException {
        if (b == null) {
            throw new IllegalArgumentException("Blockette cannot be null");
        }
        if (b instanceof BlocketteUnknown) {
            b = new DataBlocketteUnknown(((BlocketteUnknown)b).info, b.getType(), ((BlocketteUnknown)b).getSwapBytes());
        }
        if (b instanceof DataBlockette) {
            super.addBlockette(b);
            getHeader().setNumBlockettes((byte)(getHeader().getNumBlockettes() + 1));
        } else {
            throw new SeedFormatException("Cannot add non-data blockettes to a DataRecord " + b.getType());
        }
        if (b instanceof Blockette1000) {
            setRecordSize(((Blockette1000)b).getLogicalRecordLength());
        }
        recheckDataOffset();
    }

    protected void recheckDataOffset() throws SeedFormatException {
        int size = getHeader().getSize();
        Blockette[] blocks = getBlockettes();
        for (int i = 0; i < blocks.length; i++) {
            size += blocks[i].getSize();
        }
        if (data != null) {
            size += data.length;
        }
        if (size > RECORD_SIZE) {
            int headerSize = size;
            if (data != null) {
                headerSize = size - data.length;
            }
            throw new SeedFormatException("Can't fit blockettes and data in record " + headerSize + " + "
                    + (data == null ? 0 : data.length) + " > " + RECORD_SIZE);
        }
        if (data != null) {
            // shift the data to end of blockette so pad happens between
            // blockettes and data
            getHeader().setDataOffset((short)(RECORD_SIZE - data.length));
        }
    }

    /**
     * returns the data from this data header unparsed, as a byte array in
     * the format from blockette 1000. The return type is byte[], so the caller
     * must decode the data based on its format.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Decompress the data in this record according to the compression type in
     * the header.
     * 
     * @return
     * @throws SeedFormatException if no blockette 1000 present
     * @throws UnsupportedCompressionType
     * @throws CodecException
     */
    public DecompressedData decompress() throws SeedFormatException, UnsupportedCompressionType, CodecException {
        // in case of record with only blockettes, ex detection blockette, which often have compression type
        // set to 0, which messes up the decompresser even though it doesn't matter since there is no data.
        if (getHeader().getNumSamples() == 0) {
            return new DecompressedData(new int[0]);
        }
        Blockette1000 b1000 = (Blockette1000)getUniqueBlockette(1000);
        if (b1000 == null) {
            throw new MissingBlockette1000(getHeader());
        }
        Codec codec = new Codec();
        return codec.decompress(b1000.getEncodingFormat(),
                                getData(),
                                getHeader().getNumSamples(),
                                b1000.isLittleEndian());
    }

    public void setData(byte[] data) throws SeedFormatException {
        this.data = data;
        recheckDataOffset();
    }

    public int getDataSize() {
        return data.length;
    }
    

    public float getSampleRate() {
        float sampleRate;
        Blockette[] blocketts = getBlockettes(100);
        if (blocketts.length != 0) {
            Blockette100 b100 = (Blockette100)blocketts[0];
            sampleRate = b100.getActualSampleRate();
        } else {
            sampleRate = getHeader().calcSampleRateFromMultipilerFactor();
        }
        return sampleRate;
    }

    /**
     * return a Btime structure containing the derived end time for this record
     * Note this is not the time of the last sample, but rather the predicted
     * begin time of the next record, ie begin + numSample*period instead of
     * begin + (numSample-1)*period.
     * 
     * Note that this will use the more accurate sample rate in a blockette100 if it exists.
     */
    private Btime getEndBtime() {
        Btime startBtime = getHeader().getStartBtime();
        // get the number of ten thousandths of seconds of data
        double numTenThousandths = (((double)getHeader().getNumSamples() / getSampleRate()) * 10000.0);
        // return the time structure projected by the number of ten thousandths
        // of seconds
        return getHeader().projectTime(startBtime, numTenThousandths);
    }

    /** returns the predicted start time of the next record, ie begin + numSample*period
     * 
     * Note that this will use the more accurate sample rate in a blockette100 if it exists.
     */
    public Btime getPredictedNextStartBtime() {
        return getEndBtime();
    }
    
    public BtimeRange getBtimeRange() {
        return new BtimeRange(getHeader().getStartBtime(), getLastSampleBtime());
    }
    
    /**
     * return a Btime structure containing the derived last sample time for this
     * record.
     * 
     * Note that this will use the more accurate sample rate in a blockette100 if it exists.
     */
    public Btime getLastSampleBtime() {
        Btime startBtime = getStartBtime();
        if (getHeader().getNumSamples() == 0) {
            return startBtime;
        }
        // get the number of ten thousandths of seconds of data
        double numTenThousandths = (((double)(getHeader().getNumSamples() - 1) / getSampleRate()) * 10000.0);
        // return the time structure projected by the number of ten thousandths
        // of seconds
        return DataHeader.projectTime(startBtime, numTenThousandths);
    }

    /** Gets start Btime from header, convenience method. */
    public Btime getStartBtime() {
        return getHeader().getStartBtime();
    }

    /** Gets start time from header, convenience method. */
    public String getStartTime() {
        return getHeader().getStartTime();
    }


    /**
     * get the value of end time. derived from Start time, sample rate, and
     * number of samples. Note this is not the time of the last sample, but
     * rather the predicted begin time of the next record.
     * 
     * Note that this will use the more accurate sample rate in a blockette100 if it exists.
     * 
     * @return the value of end time
     */
    public String getEndTime() {
        // get time structure
        Btime endStruct = getEndBtime();
        // zero padding format of output numbers
        DecimalFormat twoZero = new DecimalFormat("00");
        DecimalFormat threeZero = new DecimalFormat("000");
        DecimalFormat fourZero = new DecimalFormat("0000");
        // return string in standard jday format
        return new String(fourZero.format(endStruct.year) + ","
                + threeZero.format(endStruct.jday) + ","
                + twoZero.format(endStruct.hour) + ":"
                + twoZero.format(endStruct.min) + ":"
                + twoZero.format(endStruct.sec) + "."
                + fourZero.format(endStruct.tenthMilli));
    }

    /**
     * get the value of end time. derived from Start time, sample rate, and
     * number of samples.
     * 
     * Note that this will use the more accurate sample rate in a blockette100 if it exists.
     * 
     * @return the value of end time
     */
    public String getLastSampleTime() {
        // get time structure
        Btime endStruct = getLastSampleBtime();
        // zero padding format of output numbers
        DecimalFormat twoZero = new DecimalFormat("00");
        DecimalFormat threeZero = new DecimalFormat("000");
        DecimalFormat fourZero = new DecimalFormat("0000");
        // return string in standard jday format
        return new String(fourZero.format(endStruct.year) + ","
                + threeZero.format(endStruct.jday) + ","
                + twoZero.format(endStruct.hour) + ":"
                + twoZero.format(endStruct.min) + ":"
                + twoZero.format(endStruct.sec) + "."
                + fourZero.format(endStruct.tenthMilli));
    }

    public DataHeader getHeader() {
        return (DataHeader)header;
    }

    public byte[] toByteArray() {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(byteStream);
            write(dos);
            dos.close();
            return byteStream.toByteArray();
        } catch(IOException e) {
            // shouldn't happen
            throw new RuntimeException("Caught IOException, should not happen.", e);
        }
    }

    public void write(DataOutputStream dos) throws IOException {
        Blockette[] blocks = getBlockettes();
        getHeader().setNumBlockettes((byte)blocks.length);
        if (blocks.length != 0) {
            getHeader().setDataBlocketteOffset((byte)48);
        }
        getHeader().write(dos);
        DataBlockette dataB;
        short blockettesSize = getHeader().getSize();
        for (int i = 0; i < blocks.length; i++) {
            dataB = (DataBlockette)blocks[i];
            blockettesSize += (short)dataB.getSize();
            if (i != blocks.length - 1) {
                dos.write(dataB.toBytes(blockettesSize));
            } else {
                dos.write(dataB.toBytes((short)0));
            }
        } // end of for ()
        for (int i = blockettesSize; i < getHeader().getDataOffset(); i++) {
            dos.write(ZERO_BYTE);
        }
        dos.write(data);
        int remainBytes = RECORD_SIZE - getHeader().getDataOffset() - data.length;
        for (int i = 0; i < remainBytes; i++) {
            dos.write(ZERO_BYTE);
        } // end of for ()
    }

    /**
     * @deprecated Confusing method name, use printData(PrintWriter) for textual
     *             output and write(DataOutputStream) for binary output.
     * 
     * @param out
     */
    @Deprecated
    public void writeData(PrintWriter out) {
        printData(out);
    }

    public void printData(PrintWriter out) {
        byte[] d = getData();
        DecimalFormat byteFormat = new DecimalFormat("000");
        int i;
        for (i = 0; i < d.length; i++) {
            out.write(byteFormat.format(0xff & d[i]) + " ");
            if (i % 4 == 3) {out.write("  ");}
            if (i % 16 == 15 && i != 0) {
                out.write("\n");
            }
        }
        if (i % 16 != 15 && i != 0) {
            out.write("\n");
        }
    }

    public static SeedRecord readDataRecord(DataInput inStream, DataHeader header, int defaultRecordSize)
            throws IOException, SeedFormatException {
        try {
            boolean swapBytes = header.flagByteSwap();
            DataRecord dataRec = new DataRecord(header);
            // read garbage between header and blockettes
            if (header.getDataBlocketteOffset() != 0) {
                if (header.getDataBlocketteOffset() < header.getSize()) {
                    throw new SeedFormatException("dataBlocketteOffset is smaller than header size: "+header.getDataBlocketteOffset());
                }
                if (header.getDataBlocketteOffset() > 4096 && header.getDataBlocketteOffset() > defaultRecordSize) {
                    throw new SeedFormatException("dataBlocketteOffset is large: "+header.getDataBlocketteOffset());
                }
                byte[] garbage = new byte[header.getDataBlocketteOffset() - header.getSize()];
                if (garbage.length != 0) {
                    inStream.readFully(garbage);
                }
            }
            byte[] blocketteBytes;
            int currOffset = header.getDataBlocketteOffset();
            if (header.getDataBlocketteOffset() == 0) {
                currOffset = header.getSize();
            }
            int type, nextOffset;
            int recordSize = 0;
            for (int i = 0; i < header.getNumBlockettes(); i++) {
                // get blockette type (first 2 bytes)
                byte hibyteType = inStream.readByte();
                byte lowbyteType = inStream.readByte();
                type = Utility.uBytesToInt(hibyteType, lowbyteType, swapBytes);
                byte hibyteOffset = inStream.readByte();
                byte lowbyteOffset = inStream.readByte();
                nextOffset = Utility.uBytesToInt(hibyteOffset,
                                                 lowbyteOffset,
                                                 swapBytes);
                // account for the 4 bytes above
                currOffset += 4;
                if (nextOffset != 0) {
                    blocketteBytes = new byte[nextOffset - currOffset];
                } else if (header.getNumSamples() !=0 && header.getDataOffset() > currOffset) {
                    blocketteBytes = new byte[header.getDataOffset() - currOffset];
                } else if (header.getNumSamples() == 0 && i == header.getNumBlockettes()-1 && recordSize > 0) {
                    // weird case where no data, only blockettes and so try to load all bytes as the last
                    // blockette and trim to fit after reading
                    blocketteBytes = new byte[recordSize-currOffset];
                } else {
                    blocketteBytes = new byte[0];
                }
                inStream.readFully(blocketteBytes);
                if (nextOffset != 0) {
                    currOffset = nextOffset;
                } else {
                    currOffset += blocketteBytes.length;
                }
                // fix so blockette has full bytes
                byte[] fullBlocketteBytes = new byte[blocketteBytes.length + 4];
                System.arraycopy(blocketteBytes,
                                 0,
                                 fullBlocketteBytes,
                                 4,
                                 blocketteBytes.length);
                fullBlocketteBytes[0] = hibyteType;
                fullBlocketteBytes[1] = lowbyteType;
                fullBlocketteBytes[2] = hibyteOffset;
                fullBlocketteBytes[3] = lowbyteOffset;

                Blockette b = SeedRecord.getBlocketteFactory().parseBlockette(type, fullBlocketteBytes, swapBytes);
                if (b.getType() == 1000) {
                    // might need this in the case of b2000 as its length is dynamic
                    // and might be no data so data offset is not useful
                    recordSize = ((Blockette1000)b).getDataRecordLength();
                }
                dataRec.blockettes.add(b);
                if (nextOffset == 0) {
                    break;
                }
            }
            try {
                recordSize = ((Blockette1000)dataRec.getUniqueBlockette(1000)).getDataRecordLength();
            } catch(MissingBlockette1000 e) {
                if (defaultRecordSize == 0) {
                    // no default
                    throw e;
                }
                // otherwise use default
                recordSize = defaultRecordSize;
            }
            dataRec.RECORD_SIZE = recordSize;
            // read garbage between blockettes and data
            if (header.getDataOffset() != 0) {
                byte[] garbage = new byte[header.getDataOffset() - currOffset];
                if (garbage.length != 0) {
                    inStream.readFully(garbage);
                }
            }
            byte[] timeseries;
            if (header.getDataOffset() == 0) {
                // data record with no data, so gobble up the rest of the record
                timeseries = new byte[recordSize - currOffset];
            } else {
                if (recordSize < header.getDataOffset()) {
                    throw new SeedFormatException("recordSize < header.getDataOffset(): " + recordSize + " < "
                            + header.getDataOffset());
                }
                timeseries = new byte[recordSize - header.getDataOffset()];
            }
            inStream.readFully(timeseries);
            dataRec.setData(timeseries);
            return dataRec;
        } catch(SeedFormatException e) {
            e.setHeader(header);
            throw e;
        }
    }

    public void setRecordSize(int recordSize) throws SeedFormatException {
        int tmp = RECORD_SIZE;
        RECORD_SIZE = recordSize;
        try {
            recheckDataOffset();
        } catch(SeedFormatException e) {
            RECORD_SIZE = tmp;
            throw e;
        }
    }

    private static final ThreadLocal<DecimalFormat> decimalFormat = new ThreadLocal<DecimalFormat>() {  
        @Override  
        protected DecimalFormat initialValue() {  
            return (new DecimalFormat("#####.####", new DecimalFormatSymbols(Locale.US)));  
        }  
    };

    public static String oneLineSummaryKey() {
        return "Type Codes Start Duration Npts";
        
    }
    public String oneLineSummary() {
        String s = getHeader().getTypeCode()+" "+ getHeader().getCodes() + " " 
                + getStartTime() + "  " + decimalFormat.get().format(getHeader().getNumSamples()/ getSampleRate() ) +" "+getHeader().getNumSamples();
        return s;
    }
    
    public String toString() {
        String s = super.toString();
        s += DEFAULT_INDENT+DEFAULT_INDENT+data.length + " bytes of data";
        return s;
    }

    protected byte[] data;

    byte ZERO_BYTE = 0;
} // DataRecord

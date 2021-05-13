package com.spike.data.readers.mysql.innodb.tablespace;

import com.google.common.collect.Lists;
import com.spike.data.readers.common.annotation.DataFileFormatDescription;
import com.spike.data.readers.common.reader.ChunkedDataFileReader;
import com.spike.data.readers.common.types.DataFileField;
import io.netty.buffer.ByteBufUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class MySQLFrmFileReader extends ChunkedDataFileReader<MySQLFrmFileReader.MySQLFrmFileDescription> {
    private static final Logger LOG = LoggerFactory.getLogger(MySQLFrmFileReader.class);


    public MySQLFrmFileReader(int chunkSize, String filePath) throws FileNotFoundException {
        super(chunkSize, filePath);
    }

    @Override
    public MySQLFrmFileDescription readFully() throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Read frm file start");
        }

        // header
        final List<DataFileField> header = Lists.newArrayList();
        header.add(DataFileField.builder().offset(0).length(1).value(ByteBufUtil.hexDump(readByte(1))).description("Always").build());
        header.add(DataFileField.builder().offset(1).length(1).value(ByteBufUtil.hexDump(readByte(1))).description("Always").build());


        // key information

        // comment

        // column information

        if (LOG.isDebugEnabled()) {
            LOG.debug("Read frm file end.");
        }
        return MySQLFrmFileDescription.builder()
                .header(header)
                .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @DataFileFormatDescription
    public static class MySQLFrmFileDescription {
        private List<DataFileField> header;
        private Object keyInformation;
        private String comment;
        private List<DataFileField> columnInformation;
    }
}

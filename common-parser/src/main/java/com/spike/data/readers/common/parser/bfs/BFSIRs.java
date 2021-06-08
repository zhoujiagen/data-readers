package com.spike.data.readers.common.parser.bfs;

import java.util.List;
import java.util.Optional;

public interface BFSIRs {
    class BFSFileUnit {
        private String fileName;
        private BFSModels.BFSFile bfsFile;

        private Optional<List<BFSFileUnit>> includes;

        //---------------------------------------------------------------------------
        // getter/setter
        //---------------------------------------------------------------------------

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public BFSModels.BFSFile getBfsFile() {
            return bfsFile;
        }

        public void setBfsFile(BFSModels.BFSFile bfsFile) {
            this.bfsFile = bfsFile;
        }

        public Optional<List<BFSFileUnit>> getIncludes() {
            return includes;
        }

        public void setIncludes(Optional<List<BFSFileUnit>> includes) {
            this.includes = includes;
        }
    }
}

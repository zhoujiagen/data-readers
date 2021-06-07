package com.spike.data.readers.common.parser.bfs;

public interface BFSIRs {
    class BFSBlockDefinition {
        private BFSModels.BFSBlockDef definition;
        private String outer;
        private String fileName;

        public String getName() {
            return definition.getBlockName();
        }

        public String getOuter() {
            return outer;
        }

        public void setOuter(String outer) {
            this.outer = outer;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public BFSModels.BFSBlockDef getDefinition() {
            return definition;
        }

        public void setDefinition(BFSModels.BFSBlockDef definition) {
            this.definition = definition;
        }
    }
}

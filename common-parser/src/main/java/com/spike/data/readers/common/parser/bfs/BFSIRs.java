package com.spike.data.readers.common.parser.bfs;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

interface BFSIRs {
    class BFSFileUnit extends BFSModels.Base {
        private String fileName;
        private Optional<List<BFSModels.Include>> includes;
        private Optional<List<BFSModels.Define>> defines;
        private List<BFSModels.Specification> specifications;

        //---------------------------------------------------------------------------
        // getter/setter
        //---------------------------------------------------------------------------

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Optional<List<BFSModels.Include>> getIncludes() {
            return includes;
        }

        public void setIncludes(Optional<List<BFSModels.Include>> includes) {
            this.includes = includes;
        }

        public Optional<List<BFSModels.Define>> getDefines() {
            return defines;
        }

        public void setDefines(Optional<List<BFSModels.Define>> defines) {
            this.defines = defines;
        }

        public List<BFSModels.Specification> getSpecifications() {
            return specifications;
        }

        public void setSpecifications(List<BFSModels.Specification> specifications) {
            this.specifications = specifications;
        }

    }

    class ParseContext {
        private static final Logger LOG = LoggerFactory.getLogger(ParseContext.class);

        Map<String, BFSFileUnit> bfsFileUnitMap = Maps.newHashMap();
        Map<String, BFSModels.Specification> specificationMap = Maps.newHashMap();

        //---------------------------------------------------------------------------
        // query
        //---------------------------------------------------------------------------

        Optional<BFSModels.Specification> findSpecification(String name) {
            return Optional.ofNullable(specificationMap.get(name));
        }

        //---------------------------------------------------------------------------
        // register
        //---------------------------------------------------------------------------

        public void registerBFSFileUnit(Path fromPath, BFSIRs.BFSFileUnit bfsFileUnit) {
            final String fileName = bfsFileUnit.getFileName();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Register BFSFileUnit {} from {}", fileName, fromPath.getFileName().toString());
            }

            if (bfsFileUnitMap.containsKey(fileName)) {
                LOG.warn("BFSFileUnit already exist: {}", fileName);
            }
            bfsFileUnitMap.put(fileName, bfsFileUnit);
        }

        public void registerSpecification(BFSModels.Specification specification) {
            if (specification instanceof BFSModels.BlockDef) {
                BFSModels.BlockDef blockDef = (BFSModels.BlockDef) specification;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Register Specification/BlockDef: " + blockDef.getBlockName());
                }
                specificationMap.put(blockDef.getBlockName(), blockDef);
            } else if (specification instanceof BFSModels.BlockDecl) {
                BFSModels.BlockDecl blockDecl = (BFSModels.BlockDecl) specification;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Register Specification/BlockDecl: " + blockDecl.getBlockName());
                }
                specificationMap.put(blockDecl.getBlockName(), blockDecl);
            } else {
                throw new RuntimeException("Invalid Specification: " + specification.getClass().getName());
            }
        }
    }

    ParseContext PARSE_CONTEXT = new ParseContext();

}

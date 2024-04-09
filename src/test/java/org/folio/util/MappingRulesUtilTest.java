package org.folio.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.junit.Assert;


public class MappingRulesUtilTest {

    @Test
    public void shouldUpdateCancelledLCCNIdentifierTo010Field() {
        ObjectNode rulesToCompare = FileWorker.getJsonObject("rules/expectedRules.json");
        String rule = "010";
        ObjectNode baseVersion = FileWorker.getJsonObject("rules/baseRules.json");

        assert baseVersion != null;
        MappingRulesUtil.updateMappingRules(rule, baseVersion);
        Assert.assertEquals(rulesToCompare, baseVersion);
    }
}

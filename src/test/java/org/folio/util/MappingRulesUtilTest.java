package org.folio.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.folio.exception.MarcRulesNotFoundException;
import org.junit.Test;
import org.junit.Assert;


public class MappingRulesUtilTest {

    private static final String BASE_RULES_JSON = "rules/baseRules.json";
    private static final String EXPECTED_RULES_JSON = "rules/expectedRules.json";
    private static final String rule = "010";
    private static final String BASE_RULES_WITHOUT_010_FIELD_JSON = "rules/baseRules_without_010_field.json";


    @Test
    public void shouldUpdateCanceledLCCNIdentifierTo010Field() throws MarcRulesNotFoundException {
        ObjectNode rulesToCompare = FileWorker.getJsonObject(EXPECTED_RULES_JSON);
        ObjectNode baseVersion = FileWorker.getJsonObject(BASE_RULES_JSON);

        assert baseVersion != null;
        MappingRulesUtil.updateMappingRules(rule, baseVersion);
        Assert.assertEquals(rulesToCompare, baseVersion);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionIf010FieldMissing() throws MarcRulesNotFoundException {
        String rule = "010";
        ObjectNode baseVersion = FileWorker.getJsonObject(BASE_RULES_WITHOUT_010_FIELD_JSON);

        assert baseVersion != null;
        MappingRulesUtil.updateMappingRules(rule, baseVersion);
    }
}

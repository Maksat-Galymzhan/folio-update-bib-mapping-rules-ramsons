package org.folio.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MappingRulesUtil {

    private static final String TARGET = "target";
    private static final String ENTITY = "entity";
    private static final String Z_SUBFIELD = "z";
    private static final String SUBFIELD = "subfield";

    public static void updateMappingRules(String marcFieldRule, ObjectNode targetMappingRules) {
        ObjectNode marcBibTypeId = Objects.requireNonNull(FileWorker.getJsonObject("rules/marcbib010$z_type_id.json"));
        ObjectNode marcBibValue = Objects.requireNonNull(FileWorker.getJsonObject("rules/marcbib010$z_value.json"));

        JsonNode marcRulesNode = targetMappingRules.get(marcFieldRule);
        if (marcRulesNode == null || !marcRulesNode.isArray() || marcRulesNode.isEmpty()) {
            log.warn("No rules found for MARC field \"{}\"", marcFieldRule);
        }

        addTarget(marcRulesNode, marcBibTypeId);
        addTarget(marcRulesNode, marcBibValue);
    }

    private static void addTarget(JsonNode marcRulesNode, ObjectNode marcBibTypeId) {
        for (JsonNode entityRule : marcRulesNode) {
            if (entityRule.get(ENTITY).isArray()) {
                ArrayNode rules = (ArrayNode) entityRule.get(ENTITY);
                if (!hasTarget(rules, marcBibTypeId)) {
                    rules.add(marcBibTypeId);
                }
            }
        }
    }

    private static boolean hasTarget(ArrayNode rules, ObjectNode targetName) {
        for (JsonNode rule : rules) {
            if (rule.get(TARGET).asText().equals(targetName.get(TARGET).asText())) {
                ArrayNode subfields = (ArrayNode) rule.get(SUBFIELD);
                if (subfields.size() == 1 && subfields.get(0).asText().equals(Z_SUBFIELD)) {
                    log.info("Mapping rules for 010$z already exist {}", rule.asText());
                    return true;
                }
            }
        }
        return false;
    }
}
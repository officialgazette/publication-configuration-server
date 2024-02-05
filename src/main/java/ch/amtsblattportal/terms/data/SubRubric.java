package ch.amtsblattportal.terms.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({ "code", "name", "exampleUrl", "info", "terms" })
public class SubRubric {

  @JsonProperty @Getter @Setter String code, exampleUrl;
  @JsonProperty @Getter @Setter MultiLang name;
  @JsonProperty @Getter @Setter SubRubricInfo info;
  @JsonProperty @Getter @Setter List<SubRubricTerm> terms = new ArrayList<>();

  // Make sure the subrubric code matches the expectations for example configurations used in root configuration
  void enforceExampleTenantCode() {
    this.code = this.code.replaceAll( "-.{2}", "-" + Tenant.EXAMPLE_CANTON );
  }

}

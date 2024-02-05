package ch.amtsblattportal.terms.data;

import java.util.List;

public interface TermConfig extends Comparable<TermConfig> {

  public static final String TITLE_DELIMITER =" – ";

  public String getKey();
  public String getValueType();
  public MultiLang getTerm();
  public boolean isPrimaryElement();
  public boolean isMandatoryIn();
  public boolean isNotEditableIn();
  public boolean isExcludeDisplayTitle();
  public String getDelimiterTitle();
  public String getType();
  public List<String> getParents();
  public TermConfig getTermConfig();
  public int getOrder();

}

package org.github.mkeskells.btd.coder;

import java.util.List;
import java.util.Optional;

public class Column {
  private final String columnName;
  private final boolean mandatory;
  private final boolean nullable;
  private final boolean allowOverwrite;
  private final Optional<Validator> validator;
  private final List<Encoding<?>> highPriorityEncodings;
  private final List<Encoding<?>> lowPriorityEncodings;
  private final int maxEqualsCheck;
  private final boolean includeStandardEncoders;

  public Column(String columnName, boolean mandatory, boolean nullable,
                boolean allowOverwrite, Optional<Validator> validator, int maxEqualsCheck,
                List<Encoding<?>> highPriorityEncodings, List<Encoding<?>> lowPriorityEncodings, boolean includeStandardEncoders) {
    this.columnName = columnName;
    this.mandatory = mandatory;
    this.nullable = nullable;
    this.allowOverwrite = allowOverwrite;
    this.validator = validator;
    this.highPriorityEncodings = highPriorityEncodings;
    this.lowPriorityEncodings = lowPriorityEncodings;
    this.maxEqualsCheck = maxEqualsCheck;
    this.includeStandardEncoders = includeStandardEncoders;
  }

  public String getColumnName() {
    return columnName;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public boolean isNullable() {
    return nullable;
  }

  public Optional<Validator> getValidator() {
    return validator;
  }

  public List<Encoding<?>> getHighPriorityEncodings() {
    return highPriorityEncodings;
  }

  public List<Encoding<?>> getLowPriorityEncodings() {
    return lowPriorityEncodings;
  }

  public boolean shouldIncludeStandardEncoders() {
    return includeStandardEncoders;
  }

  public int getMaxEqualsCheck() {
    return maxEqualsCheck;
  }

  public boolean allowsOverwrite() {
    return allowOverwrite;
  }
}

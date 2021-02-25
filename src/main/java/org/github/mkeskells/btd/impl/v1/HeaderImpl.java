package org.github.mkeskells.btd.impl.v1;

import org.github.mkeskells.btd.Header;
import org.github.mkeskells.btd.coder.Coder;
import org.github.mkeskells.btd.coder.Column;

import java.util.List;
import java.util.stream.Collectors;

public class HeaderImpl implements Header {
  private final Coder coder;

  public HeaderImpl(Coder coder) {
    this.coder = coder;
  }

  private List<Column> columns() {
    return coder.getColumns();
  }

  @Override
  public int getHeaderCount() {
    return columns().size();
  }

  @Override
  public String getHeaderValue(int columnId) {
    return columns().get(columnId).getColumnName();
  }

  @Override
  public List<String> getHeaders() {
    return columns().stream().map(Column::getColumnName).collect(Collectors.toList());
  }
}

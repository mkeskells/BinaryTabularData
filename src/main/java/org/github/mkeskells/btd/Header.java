package org.github.mkeskells.btd;

import java.util.List;

public interface Header {
  int getHeaderCount();

  String getHeaderValue(int columnId);

  List<String> getHeaders();
}

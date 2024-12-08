package com.flyjingfish.openimagelib.listener;

import com.flyjingfish.openimagelib.OpenImageDetail;
import com.flyjingfish.openimagelib.beans.OpenImageUrl;
import java.util.List;

public interface OnSelectMediaListener {
  /**
   * @param openImageUrl 正在看的所传入的数据
   * @param position 正在看的位置下标
   */
  void onSelect(List<OpenImageDetail> allOpenImageDetail, OpenImageUrl openImageUrl, int position);
}

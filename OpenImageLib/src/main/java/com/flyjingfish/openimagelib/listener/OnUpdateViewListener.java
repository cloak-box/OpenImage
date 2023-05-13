package com.flyjingfish.openimagelib.listener;

import com.flyjingfish.openimagelib.beans.OpenImageUrl;
import com.flyjingfish.openimagelib.enums.UpdateViewType;

import java.util.Collection;

public interface OnUpdateViewListener {
    /**
     * 新加数据后回调此方法，你必须在继承自{@link com.flyjingfish.openimagelib.OpenImageActivity}的自定义activity中调用{@link com.flyjingfish.openimagelib.OpenImageFragmentStateAdapter#addData(Collection)},
     * 或{@link com.flyjingfish.openimagelib.OpenImageFragmentStateAdapter#addFrontData(Collection)}，才会回调此方法
     *
     * @param data 新增数据
     * @param updateViewType 更新数据类型{@link UpdateViewType#FORWARD}表示你要把数据加到列表前边，{@link UpdateViewType#BACKWARD}表示你要把数据加到列表后边，
     * {@link UpdateViewType#NONE}表示不要更新前一页面列表
     */
    void onUpdate(Collection<? extends OpenImageUrl> data,UpdateViewType updateViewType);
}

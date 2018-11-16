package com.sunland.securitycheck.bean;

import com.sunland.netmodule.def.bean.result.ResultBase;

import java.util.List;

public class NameListResponseBean extends ResultBase {
    // TODO: 2018/11/7/007
    private int totalCount;
    private int totalPage;
    private List<TSummitPersion> tSummitPersions;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<TSummitPersion> gettSummitPersions() {
        return tSummitPersions;
    }

    public void settSummitPersions(List<TSummitPersion> tSummitPersions) {
        this.tSummitPersions = tSummitPersions;
    }
}

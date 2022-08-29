package org.smartregister.chw.cdp.domain;

import java.io.Serializable;

public class OutletObject implements Serializable {

    private String outletName;
    private String outletType;
    private String outletWardName;
    private String outletId;

    public OutletObject() {
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletType() {
        return outletType;
    }

    public void setOutletType(String outletType) {
        this.outletType = outletType;
    }

    public String getOutletWardName() {
        return outletWardName;
    }

    public void setOutletWardName(String outletWardName) {
        this.outletWardName = outletWardName;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

}
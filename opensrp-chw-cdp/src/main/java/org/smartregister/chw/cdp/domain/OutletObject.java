package org.smartregister.chw.cdp.domain;

import java.io.Serializable;

public class OutletObject implements Serializable {

    private String outletName;
    private String outletType;
    private String otherOutletType;
    private String outletWardName;
    private String outletId;
    private String focalPersonName;
    private String focalPersonNumber;
    private String baseEntityId;

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

    public String getOtherOutletType() {
        return otherOutletType;
    }

    public void setOtherOutletType(String otherOutletType) {
        this.otherOutletType = otherOutletType;
    }

    public String getFocalPersonName() {
        return focalPersonName;
    }

    public void setFocalPersonName(String focalPersonName) {
        this.focalPersonName = focalPersonName;
    }

    public String getFocalPersonNumber() {
        return focalPersonNumber;
    }

    public void setFocalPersonNumber(String focalPersonNumber) {
        this.focalPersonNumber = focalPersonNumber;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }
}

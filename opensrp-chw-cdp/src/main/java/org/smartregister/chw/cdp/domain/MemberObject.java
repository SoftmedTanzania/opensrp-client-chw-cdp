package org.smartregister.chw.cdp.domain;

import java.io.Serializable;
import java.util.Date;

public class MemberObject implements Serializable {

    private String familyHeadName;
    private String familyHeadPhoneNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String gender;
    private String uniqueId;
    private String age;
    private String relationalid;
    private String details;
    private String dateChwCDPTest;
    private String feverCDPChw;
    private String feverDuration;
    private String dateHfCDPTest;
    private Date cdpTestDate;
    private String cdpTreat;
    private String famLlin;
    private String llin2Days;
    private String llinCondition;
    private String cdpEduChw;
    private String baseEntityId;
    private String relationalId;
    private String primaryCareGiver;
    private String primaryCareGiverName;
    private String primaryCareGiverPhone;
    private String familyHead;
    private String familyBaseEntityId;
    private String familyName;
    private String phoneNumber;
    private String gestAge;
    private String deliveryDate;
    private String ancMember;
    private String pncMember;
    private String cdpFollowUpDate;

    public MemberObject() {
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getRelationalid() {
        return relationalid;
    }

    public void setRelationalid(String relationalid) {
        this.relationalid = relationalid;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDateChwCDPTest() {
        return dateChwCDPTest;
    }

    public void setDateChwCDPTest(String dateChwCDPTest) {
        this.dateChwCDPTest = dateChwCDPTest;
    }

    public String getFeverCDPChw() {
        return feverCDPChw;
    }

    public void setFeverCDPChw(String feverCDPChw) {
        this.feverCDPChw = feverCDPChw;
    }

    public String getFeverDuration() {
        return feverDuration;
    }

    public void setFeverDuration(String feverDuration) {
        this.feverDuration = feverDuration;
    }

    public String getDateHfCDPTest() {
        return dateHfCDPTest;
    }

    public void setDateHfCDPTest(String dateHfCDPTest) {
        this.dateHfCDPTest = dateHfCDPTest;
    }

    public Date getCDPTestDate() {
        return cdpTestDate;
    }

    public void setCDPTestDate(Date cdpTestDate) {
        this.cdpTestDate = cdpTestDate;
    }

    public String getCDPTreat() {
        return cdpTreat;
    }

    public void setCDPTreat(String cdpTreat) {
        this.cdpTreat = cdpTreat;
    }

    public String getFamLlin() {
        return famLlin;
    }

    public void setFamLlin(String famLlin) {
        this.famLlin = famLlin;
    }

    public String getLlin2Days() {
        return llin2Days;
    }

    public void setLlin2Days(String llin2Days) {
        this.llin2Days = llin2Days;
    }

    public String getLlinCondition() {
        return llinCondition;
    }

    public void setLlinCondition(String llinCondition) {
        this.llinCondition = llinCondition;
    }

    public String getCDPEduChw() {
        return cdpEduChw;
    }

    public void setCDPEduChw(String cdpEduChw) {
        this.cdpEduChw = cdpEduChw;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }

    public String getRelationalId() {
        return relationalId;
    }

    public void setRelationalId(String relationalId) {
        this.relationalId = relationalId;
    }

    public String getFamilyBaseEntityId() {
        return familyBaseEntityId;
    }

    public void setFamilyBaseEntityId(String familyBaseEntityId) {
        this.familyBaseEntityId = familyBaseEntityId;
    }

    public String getPrimaryCareGiver() {
        return primaryCareGiver;
    }

    public void setPrimaryCareGiver(String primaryCareGiver) {
        this.primaryCareGiver = primaryCareGiver;
    }

    public String getFamilyHead() {
        return familyHead;
    }

    public void setFamilyHead(String familyHead) {
        this.familyHead = familyHead;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGestAge() {
        return gestAge;
    }

    public void setGestAge(String gestAge) {
        this.gestAge = gestAge;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }


    public String getFamilyHeadName() {
        return familyHeadName;
    }

    public void setFamilyHeadName(String familyHeadName) {
        this.familyHeadName = familyHeadName;
    }

    public String getFamilyHeadPhoneNumber() {
        return familyHeadPhoneNumber;
    }

    public void setFamilyHeadPhoneNumber(String familyHeadPhoneNumber) {
        this.familyHeadPhoneNumber = familyHeadPhoneNumber;
    }

    public String getPrimaryCareGiverName() {
        return primaryCareGiverName;
    }

    public void setPrimaryCareGiverName(String primaryCareGiverName) {
        this.primaryCareGiverName = primaryCareGiverName;
    }

    public String getPrimaryCareGiverPhone() {
        return primaryCareGiverPhone;
    }

    public void setPrimaryCareGiverPhone(String primaryCareGiverPhone) {
        this.primaryCareGiverPhone = primaryCareGiverPhone;
    }

    public String getAncMember() {
        return ancMember;
    }

    public void setAncMember(String ancMember) {
        this.ancMember = ancMember;
    }

    public String getPncMember() {
        return pncMember;
    }

    public void setPncMember(String pncMember) {
        this.pncMember = pncMember;
    }

    public String getCDPFollowUpDate() {
        return cdpFollowUpDate;
    }

    public void setCDPFollowUpDate(String cdpFollowUpDate) {
        this.cdpFollowUpDate = cdpFollowUpDate;
    }
}

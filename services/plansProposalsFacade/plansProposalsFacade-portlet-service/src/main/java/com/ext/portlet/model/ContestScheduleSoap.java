package com.ext.portlet.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link com.ext.portlet.service.http.ContestScheduleServiceSoap}.
 *
 * @author Brian Wing Shun Chan
 * @see com.ext.portlet.service.http.ContestScheduleServiceSoap
 * @generated
 */
public class ContestScheduleSoap implements Serializable {
    private long _ContestSchedulePK;
    private long _ContestPK;
    private String _name;
    private String _description;
    private String _status;
    private boolean _invisible;

    public ContestScheduleSoap() {
    }

    public static ContestScheduleSoap toSoapModel(ContestSchedule model) {
        ContestScheduleSoap soapModel = new ContestScheduleSoap();

        soapModel.setContestSchedulePK(model.getContestSchedulePK());
        soapModel.setContestPK(model.getContestPK());
        soapModel.setName(model.getName());
        soapModel.setDescription(model.getDescription());
        soapModel.setStatus(model.getStatus());
        soapModel.setInvisible(model.getInvisible());

        return soapModel;
    }

    public static ContestScheduleSoap[] toSoapModels(ContestSchedule[] models) {
        ContestScheduleSoap[] soapModels = new ContestScheduleSoap[models.length];

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModel(models[i]);
        }

        return soapModels;
    }

    public static ContestScheduleSoap[][] toSoapModels(
        ContestSchedule[][] models) {
        ContestScheduleSoap[][] soapModels = null;

        if (models.length > 0) {
            soapModels = new ContestScheduleSoap[models.length][models[0].length];
        } else {
            soapModels = new ContestScheduleSoap[0][0];
        }

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModels(models[i]);
        }

        return soapModels;
    }

    public static ContestScheduleSoap[] toSoapModels(
        List<ContestSchedule> models) {
        List<ContestScheduleSoap> soapModels = new ArrayList<ContestScheduleSoap>(models.size());

        for (ContestSchedule model : models) {
            soapModels.add(toSoapModel(model));
        }

        return soapModels.toArray(new ContestScheduleSoap[soapModels.size()]);
    }

    public long getPrimaryKey() {
        return _ContestSchedulePK;
    }

    public void setPrimaryKey(long pk) {
        setContestSchedulePK(pk);
    }

    public long getContestSchedulePK() {
        return _ContestSchedulePK;
    }

    public void setContestSchedulePK(long ContestSchedulePK) {
        _ContestSchedulePK = ContestSchedulePK;
    }

    public long getContestPK() {
        return _ContestPK;
    }

    public void setContestPK(long ContestPK) {
        _ContestPK = ContestPK;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        _status = status;
    }

    public boolean getInvisible() {
        return _invisible;
    }

    public boolean isInvisible() {
        return _invisible;
    }

    public void setInvisible(boolean invisible) {
        _invisible = invisible;
    }
}
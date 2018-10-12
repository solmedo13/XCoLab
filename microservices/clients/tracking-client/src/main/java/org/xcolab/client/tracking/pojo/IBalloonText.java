package org.xcolab.client.tracking.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.xcolab.client.tracking.pojo.tables.pojos.BalloonText;

import java.io.Serializable;

@JsonDeserialize(as = BalloonText.class)
public interface IBalloonText extends Serializable {

    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    String getTextBeforeForm();

    void setTextBeforeForm(String textBeforeForm);

    String getTextBeforeShareButtons();

    void setTextBeforeShareButtons(String textBeforeShareButtons);

    String getEmailTemplate();

    void setEmailTemplate(String emailTemplate);

    String getEmailSubjectTemplate();

    void setEmailSubjectTemplate(String emailSubjectTemplate);

    String getShareTitle();

    void setShareTitle(String shareTitle);

    String getShareDescription();

    void setShareDescription(String shareDescription);

    Boolean getEnabled();

    void setEnabled(Boolean enabled);

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();
}
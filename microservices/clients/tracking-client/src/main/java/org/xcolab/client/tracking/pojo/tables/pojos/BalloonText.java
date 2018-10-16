package org.xcolab.client.tracking.pojo.tables.pojos;

import org.xcolab.client.tracking.pojo.IBalloonText;

public class BalloonText implements IBalloonText {

    private static final long serialVersionUID = -1825617241;

    private Long id;
    private String name;
    private String textBeforeForm;
    private String textBeforeShareButtons;
    private String emailTemplate;
    private String emailSubjectTemplate;
    private String shareTitle;
    private String shareDescription;
    private Boolean enabled;

    public BalloonText() {}

    public BalloonText(BalloonText value) {
        this.id = value.id;
        this.name = value.name;
        this.textBeforeForm = value.textBeforeForm;
        this.textBeforeShareButtons = value.textBeforeShareButtons;
        this.emailTemplate = value.emailTemplate;
        this.emailSubjectTemplate = value.emailSubjectTemplate;
        this.shareTitle = value.shareTitle;
        this.shareDescription = value.shareDescription;
        this.enabled = value.enabled;
    }

    public BalloonText(Long id, String name, String textBeforeForm, String textBeforeShareButtons,
            String emailTemplate, String emailSubjectTemplate, String shareTitle,
            String shareDescription, Boolean enabled) {
        this.id = id;
        this.name = name;
        this.textBeforeForm = textBeforeForm;
        this.textBeforeShareButtons = textBeforeShareButtons;
        this.emailTemplate = emailTemplate;
        this.emailSubjectTemplate = emailSubjectTemplate;
        this.shareTitle = shareTitle;
        this.shareDescription = shareDescription;
        this.enabled = enabled;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTextBeforeForm() {
        return this.textBeforeForm;
    }

    @Override
    public void setTextBeforeForm(String textBeforeForm) {
        this.textBeforeForm = textBeforeForm;
    }

    @Override
    public String getTextBeforeShareButtons() {
        return this.textBeforeShareButtons;
    }

    @Override
    public void setTextBeforeShareButtons(String textBeforeShareButtons) {
        this.textBeforeShareButtons = textBeforeShareButtons;
    }

    @Override
    public String getEmailTemplate() {
        return this.emailTemplate;
    }

    @Override
    public void setEmailTemplate(String emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    @Override
    public String getEmailSubjectTemplate() {
        return this.emailSubjectTemplate;
    }

    @Override
    public void setEmailSubjectTemplate(String emailSubjectTemplate) {
        this.emailSubjectTemplate = emailSubjectTemplate;
    }

    @Override
    public String getShareTitle() {
        return this.shareTitle;
    }

    @Override
    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    @Override
    public String getShareDescription() {
        return this.shareDescription;
    }

    @Override
    public void setShareDescription(String shareDescription) {
        this.shareDescription = shareDescription;
    }

    @Override
    public Boolean getEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        final BalloonText other = (BalloonText) obj;
        if (id == null) {
            if (other.id != null) { return false; }
        } else if (!id.equals(other.id)) { return false; }
        if (name == null) {
            if (other.name != null) { return false; }
        } else if (!name.equals(other.name)) { return false; }
        if (textBeforeForm == null) {
            if (other.textBeforeForm != null) { return false; }
        } else if (!textBeforeForm.equals(other.textBeforeForm)) { return false; }
        if (textBeforeShareButtons == null) {
            if (other.textBeforeShareButtons != null) { return false; }
        } else if (!textBeforeShareButtons.equals(other.textBeforeShareButtons)) { return false; }
        if (emailTemplate == null) {
            if (other.emailTemplate != null) { return false; }
        } else if (!emailTemplate.equals(other.emailTemplate)) { return false; }
        if (emailSubjectTemplate == null) {
            if (other.emailSubjectTemplate != null) { return false; }
        } else if (!emailSubjectTemplate.equals(other.emailSubjectTemplate)) { return false; }
        if (shareTitle == null) {
            if (other.shareTitle != null) { return false; }
        } else if (!shareTitle.equals(other.shareTitle)) { return false; }
        if (shareDescription == null) {
            if (other.shareDescription != null) { return false; }
        } else if (!shareDescription.equals(other.shareDescription)) { return false; }
        if (enabled == null) {
            if (other.enabled != null) { return false; }
        } else if (!enabled.equals(other.enabled)) { return false; }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((textBeforeForm == null) ? 0 : textBeforeForm.hashCode());
        result = prime * result + ((textBeforeShareButtons == null) ? 0
                : textBeforeShareButtons.hashCode());
        result = prime * result + ((emailTemplate == null) ? 0 : emailTemplate.hashCode());
        result = prime * result + ((emailSubjectTemplate == null) ? 0
                : emailSubjectTemplate.hashCode());
        result = prime * result + ((shareTitle == null) ? 0 : shareTitle.hashCode());
        result = prime * result + ((shareDescription == null) ? 0 : shareDescription.hashCode());
        result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BalloonText (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(textBeforeForm);
        sb.append(", ").append(textBeforeShareButtons);
        sb.append(", ").append(emailTemplate);
        sb.append(", ").append(emailSubjectTemplate);
        sb.append(", ").append(shareTitle);
        sb.append(", ").append(shareDescription);
        sb.append(", ").append(enabled);

        sb.append(")");
        return sb.toString();
    }
}

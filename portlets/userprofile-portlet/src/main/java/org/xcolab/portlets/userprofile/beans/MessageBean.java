package org.xcolab.portlets.userprofile.beans;

import com.ext.portlet.model.Message;
import com.ext.portlet.model.MessageRecipientStatus;
import com.ext.portlet.service.MessageLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MessageBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<User> recipients = new ArrayList<>();
    @NotBlank
    private String messageSubject;
    @NotBlank
    private String messageText;
    private String messageHoneypot;
    private int messageHoneypotPosition;
    private Message message;
    private boolean selected;

    public MessageBean() {
        messageHoneypotPosition = ((new Random()).nextInt(10)) % 2;
    }

    public MessageBean(Message message) throws PortalException, SystemException {
        this.message = message;
        for (MessageRecipientStatus recipient : MessageLocalServiceUtil.getRecipients(message)) {
            recipients.add(UserLocalServiceUtil.getUser(recipient.getUserId()));
        }
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public void setMessageSubject(String messageSubject) {
        this.messageSubject = messageSubject;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageHoneypot() {
        return messageHoneypot;
    }

    public void setMessageHoneypot(String messageHoneypot) {
        this.messageHoneypot = messageHoneypot;
    }

    public int getMessageHoneypotPosition() {
        return messageHoneypotPosition;
    }

    public String getSubject() {
        return message.getSubject();
    }

    public String getContent() {
        return message.getContent();
    }

    public Date getCreateDate() {
        return message.getCreateDate();
    }

    public long getDaysAgo() {
        final int millisecondsInDay = 1000 * 60 * 60 * 24;
        long createDay = message.getCreateDate().getTime() / millisecondsInDay;
        long daysNow = new Date().getTime() / millisecondsInDay;
        return daysNow - createDay;
    }

    public User getFrom() throws PortalException, SystemException {
        return UserLocalServiceUtil.getUser(message.getFromId());
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Message getMessage() {
        return message;
    }

    public List<User> getTo() {
        return recipients;
    }

    public void addRecipientUser(User recipientUser) {
        recipients.add(recipientUser);
    }

    public Long getMessageId() {
        return message.getMessageId();
    }

}

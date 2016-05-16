package org.xcolab.activityEntry.discussion;

public class DiscussionForumCommentAddedActivityEntry extends DiscussionBaseActivityEntry {




    @Override
    public Integer getSecondaryType() {
        return 4;
    }

    @Override
    public String getBody() {

        String template = "%s added a comment to %s in %s";

        return String.format(template, getUserLink(), getDiscussionMessageLink(),
                getDiscussionCategoryLink());
    }

    @Override
    public String getTitle() {
        return getUserLink()+" added a forum comment";
    }

    @Override
    public String getName() {
        return "Forum Comment";
    }
}

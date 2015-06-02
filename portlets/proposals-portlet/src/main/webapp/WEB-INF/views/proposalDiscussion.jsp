<jsp:root xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:proposalsPortlet="urn:jsptagdir:/WEB-INF/tags/proposalsPortlet"
	xmlns:discussions="http://climatecolab.org/tags/xcollab_discussions_1.0"
	xmlns:discussionsTagFiles="urn:jsptagdir:/WEB-INF/tags/discussions"
	xmlns:addthis="http://www.addthis.com/help/api-spec"
	xmlns:portlet="http://java.sun.com/portlet_2_0" version="2.0">
<jsp:directive.include file="./init.jspx" />

	<jsp:directive.include file="./proposalDetails/header.jspx" />
	<div id="content">
		<c:choose>
			<c:when test="${showDiscussion and proposalsPermissions.canPublicRating}">
				<h2>Proposal rating function for every colab user</h2>
				<div class="addpropbox evaluation">
					<jsp:directive.include file="./proposalDetails/proposalJudging.jspx"/>
				</div>

				<c:forEach var="ratingWrapper" items="${judgeAverageRating}">
					<h2>${ratingWrapper.contestPhase}</h2>
					<div class="addpropbox evaluation">
					<proposalsPortlet:proposalRatingEvaluation showRating="true" showPhase="false"
															   wrapper="${ratingWrapper}"/>
					</div>
				</c:forEach>

				<h2>Discussion area</h2>
				<c:if test="${proposalsPermissions.canEdit}">
				<p>After you have integrated the Judges' feedback into your proposal, please write a short summary of the changes made in the discussion area below. This summary will serve as a guide for the selection of Finalists. </p>
				</c:if>
				<discussionsTagFiles:discussionComments discussionId="${discussionId }" />
			</c:when>
			<c:otherwise>
				The results of the judging period will be visible here.
			</c:otherwise>
		</c:choose>

	</div>
	

</jsp:root>
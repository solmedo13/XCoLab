<jsp:root xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:portlet="http://java.sun.com/portlet_2_0" version="2.0">
	<jsp:directive.include file="./init.jspx" />
	<div id="content">
		<jsp:directive.include file="./usersIndex/header.jspx" />

		<c:choose>
			<c:when test="${viewType == 'GRID' }">
				<jsp:directive.include file="./usersIndex/grid.jspx" />
			</c:when>
			<c:when test="${viewType == 'OUTLINE' }">
				<jsp:directive.include file="./usersIndex/list.jspx" />
			</c:when>
			<c:otherwise>
				<jsp:directive.include file="./usersIndex/list.jspx" />
			</c:otherwise>
		</c:choose>
		
	</div>
	<script>
	<![CDATA[
		// set contest view type, see https://issues.liferay.com/browse/LPS-25733 
		setContestsViewTypeCookie('${viewType}');
		
		jQuery(".show-active").hover(
				function() {
					var self = jQuery(this);
					if (! self.hasClass('mouseIn')) {
						jQuery(this).addClass('mouseIn visible');
						jQuery("#priorActiveAllWidget").slideDown();
					}
				},
				function() {
					var self = jQuery(this);
					self.removeClass('mouseIn');
					setTimeout(function() {
						if (! self.hasClass('mouseIn') && self.hasClass('visible')) {
							jQuery("#priorActiveAllWidget").slideUp();
							self.removeClass('visible');
						}
					}, 500);
				}
		);
	]]>
	</script>
</jsp:root>